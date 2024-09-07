package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleAIMode;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.api.data.DataSync;
import com.pixelmonmod.pixelmon.api.data.PixelmonDataSerializers;
import com.pixelmonmod.pixelmon.api.economy.BankAccount;
import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.Moveset;
import com.pixelmonmod.pixelmon.api.registries.PixelmonItems;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.util.helpers.DropItemHelper;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.HiddenPower;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ClearTrainerPokemonPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCEditDataPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.StoreTrainerPokemonPacket;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.*;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import com.pixelmonmod.pixelmon.enums.*;
import com.pixelmonmod.pixelmon.tools.Quadstate;
import com.pixelmonmod.pixelmon.world.generation.gyms.MovesetDefinition;
import com.pixelmonmod.pixelmon.world.generation.gyms.PokemonDefinition;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class SpecialNpcTrainerB extends NPCTrainer {
    private NpcTrainerPartyStorage party;
    private String trainerId;
    private ItemStack[] winnings;
    private boolean startRotationSet;


    private static final DataSync<NPCTrainer, EnumEncounterMode> SYNC_ENCOUNTER_MODE;
    private static final DataSync<NPCTrainer, EnumMegaItemsUnlocked> SYNC_MEGA_ITEM;
    private static final DataSync<NPCTrainer, EnumOldGenMode> SYNC_OLD_GEN;
    private static final DataSync<NPCTrainer, BossTier> SYNC_BOSS_MODE;
    private static final DataSync<NPCTrainer, Integer> SYNC_TRAINER_LEVEL;
    private static final DataSync<NPCTrainer, BattleAIMode> SYNC_BATTLE_AI;
    private static final DataSync<NPCTrainer, Integer> SYNC_ENGAGE_DISTANCE;
    public boolean usingDefaultName;
    public boolean usingDefaultGreeting;
    public boolean usingDefaultWin;
    public boolean usingDefaultLose;
    public String greeting;
    public String winMessage;
    public String loseMessage;
    public int winMoney;
    public int pokemonLevel;
    private float startRotationYaw;
    public boolean isGymLeader;
    public transient boolean canEngage;
    public HashMap<UUID, Long> playerEncounters;
    public ArrayList<String> winCommands;
    public ArrayList<String> loseCommands;
    public ArrayList<String> forfeitCommands;
    public ArrayList<String> preBattleCommands;
    public BattleRules battleRules;

    private EnumEncounterMode encounterMode;
    private EnumMegaItemsUnlocked megaItem;
    private EnumOldGenMode oldGenMode;
    private BossTier bossTier;
    private BattleAIMode battleAIMode;
    private int trainerLevel;
    private int engageDistance;
    public BattleController battleController;



    public SpecialNpcTrainerB(EntityType<NPCTrainer> type, World par1World) {
        super(type, par1World);
        this.party = new NpcTrainerPartyStorage(this);
        this.usingDefaultName = true;
        this.usingDefaultGreeting = true;
        this.usingDefaultWin = true;
        this.usingDefaultLose = true;
        this.greeting = "";
        this.winMessage = "";
        this.loseMessage = "";
        this.trainerId = "";
        this.winnings = new ItemStack[0];
        this.startRotationSet = false;
        this.isGymLeader = false;
        this.canEngage = true;
        this.playerEncounters = new HashMap();
        this.winCommands = new ArrayList();
        this.loseCommands = new ArrayList();
        this.forfeitCommands = new ArrayList();
        this.preBattleCommands = new ArrayList();
        this.battleRules = new BattleRules();
    }

    public SpecialNpcTrainerB(World world, NpcTrainerPartyStorage party, String trainerId, ItemStack[] winnings, boolean startRotationSet) {
        super(world);
        this.party = party;
        this.trainerId = trainerId;
        this.winnings = winnings;
        this.startRotationSet = startRotationSet;
    }


    public SpecialNpcTrainerB(World par1World) {
        super(par1World);
        this.party = new NpcTrainerPartyStorage(this);
        this.usingDefaultName = true;
        this.usingDefaultGreeting = true;
        this.usingDefaultWin = true;
        this.usingDefaultLose = true;
        this.greeting = "";
        this.winMessage = "";
        this.loseMessage = "";
        this.trainerId = "";
        this.winnings = new ItemStack[0];
        this.startRotationSet = false;
        this.isGymLeader = false;
        this.canEngage = true;
        this.playerEncounters = new HashMap();
        this.winCommands = new ArrayList();
        this.loseCommands = new ArrayList();
        this.forfeitCommands = new ArrayList();
        this.preBattleCommands = new ArrayList();
        this.battleRules = new BattleRules();
    }

    @Override
    public NpcTrainerPartyStorage getPokemonStorage() {
        return this.party;
    }



    @Override
    public void init(BaseTrainer trainer) {
        super.init(trainer.name);
        this.party = new NpcTrainerPartyStorage(this);
        TrainerData info = ServerNPCRegistry.trainers.getRandomData(trainer);
        if (info == null) {
            BaseTrainer b = ServerNPCRegistry.trainers.getRandomBaseWithData();
            info = ServerNPCRegistry.trainers.getRandomData(b.name);
        }

        this.chatIndex = info.getRandomChat();
        if (this.usingDefaultName) {
            this.setName("" + info.getRandomName());
        }

        this.trainerId = info.id;
        this.winMoney = info.winnings;
        this.pokemonLevel = info.getRandomLevel();
        if (this.pokemonLevel == 0) {
            this.pokemonLevel = 1;
        }

        SYNC_TRAINER_LEVEL.set(this, this.pokemonLevel);
        this.setBaseTrainer(info.trainerType);
        if (info.trainerType.textures.size() > 1) {
            this.setTextureIndex(this.level.random.nextInt(info.trainerType.textures.size()));
        }

        this.loadPokemon(info.getRandomParty());
    }

    @Override
    public String getName(String langCode) {
        if (this.usingDefaultName) {
            try {
                int index = Integer.parseInt(this.getName().getString());
                ITrainerData translatedData = this.getTranslatedData(langCode);
                return translatedData == null ? this.getName().getString() : translatedData.getName(index);
            } catch (NumberFormatException var4) {
                return this.getName().getString();
            }
        } else {
            return this.getName().getString();
        }
    }

    @Override
    public void clearGreetings() {
        this.usingDefaultGreeting = false;
        this.usingDefaultWin = false;
        this.usingDefaultLose = false;
        this.greeting = null;
        this.winMessage = null;
        this.loseMessage = null;
    }

    @Override
    public String getGreeting(String langCode) {
        return this.usingDefaultGreeting ? this.getChat(langCode).opening : this.greeting;
    }

    @Override
    public String getWinMessage(String langCode) {
        return this.usingDefaultWin ? this.getChat(langCode).win : this.winMessage;
    }

    @Override
    public String getLoseMessage(String langCode) {
        return this.usingDefaultLose ? this.getChat(langCode).lose : this.loseMessage;
    }

    private ITrainerData getTranslatedData(String langCode) {
        return ServerNPCRegistry.getTranslatedData(langCode, this.getBaseTrainer(), this.trainerId);
    }

    @Override
    public TrainerChat getChat(String langCode) {
        ITrainerData data = this.getTranslatedData(langCode);
        return data == null ? new TrainerChat("", "", "") : data.getChat(this.chatIndex);
    }

    @Override
    public int getWinMoney() {
        return this.winMoney;
    }

    @Override
    public void setEncounterMode(EnumEncounterMode mode) {
        SYNC_ENCOUNTER_MODE.set(this, mode);
        this.playerEncounters.clear();
    }

    @Override
    public void setMegaItem(EnumMegaItemsUnlocked megaItem) {
        SYNC_MEGA_ITEM.set(this, megaItem);
    }

    @Override
    public void setOldGenMode(EnumOldGenMode mode) {
        SYNC_OLD_GEN.set(this, mode);
    }

    @Override
    public EnumEncounterMode getEncounterMode() {
        return this.encounterMode;
    }

    @Override
    public EnumMegaItemsUnlocked getMegaItem() {
        return this.megaItem;
    }

    @Override
    public EnumOldGenMode getOldGen() {
        return this.oldGenMode;
    }

    @Override
    public void setEngageDistance(int engageDistance) {
        SYNC_ENGAGE_DISTANCE.set(this, engageDistance);
    }

    @Override
    public int getEngageDistance() {
        return this.engageDistance;
    }

    @Override
    public void setTrainerType(BaseTrainer model, PlayerEntity player) {
        this.init(model);
        this.setTextureIndex(0);
        this.setBaseTrainer(model);
        ServerPlayerEntity playerMP = (ServerPlayerEntity)player;
        NetworkHelper.sendPacket(new ClearTrainerPokemonPacket(), playerMP);
        Iterator var4 = this.party.getTeam().iterator();

        while(var4.hasNext()) {
            Pokemon pokemon = (Pokemon)var4.next();
            NetworkHelper.sendPacket(new StoreTrainerPokemonPacket(pokemon), playerMP);
        }

        String loc = playerMP.getLanguage();
        this.setName(ServerNPCRegistry.trainers.getTranslatedRandomName(loc, model, this.trainerId));
        SetTrainerData p = new SetTrainerData(this, loc);
        NetworkHelper.sendPacket(new SetNPCEditDataPacket(p), playerMP);
    }

    @Override
    public void tickLeash() {
        if (this.isLeashed()) {
            this.dropLeash(true, true);
        }

    }

    @Override
    public void push(double par1, double par3, double par5) {
        if (this.isPushable()) {
            super.push(par1, par3, par5);
        }

    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        EnumTrainerAI ai = this.getAIMode();
        return ai != EnumTrainerAI.StandStill && ai != EnumTrainerAI.StillAndEngage;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.startRotationSet && this.getAIMode() == EnumTrainerAI.StillAndEngage) {
            this.yRot = this.startRotationYaw;
            this.yHeadRot = this.startRotationYaw;
        }

    }

    @Override
    protected void checkForRarityDespawn() {
        if (this.battleController == null) {
            super.checkForRarityDespawn();
        }
    }

    @Override
    public PixelmonEntity releasePokemon(UUID newPokemonUUID) {
        return this.party.find(newPokemonUUID).getOrSpawnPixelmon(this);
    }

    @Override
    public void loadPokemon(ArrayList<Pokemon> pokemonList) {
        for(int i = 0; i < 6; ++i) {
            this.party.set(i, (Pokemon)null);
        }

        if (pokemonList != null && !pokemonList.isEmpty()) {
            Iterator var4 = pokemonList.iterator();

            while(var4.hasNext()) {
                Pokemon pokemon = (Pokemon)var4.next();
                pokemon.setLevel(Math.max(1, RandomHelper.getRandomNumberBetween(this.pokemonLevel - 1, this.pokemonLevel + 1)));
                this.party.add(pokemon);
            }
        } else {
            this.party.add(PokemonSpecificationProxy.create(new String[]{((Species) PixelmonSpecies.STARLY.getValueUnsafe()).getName(), "lvl:5"}).create());
        }

    }

    private void initializePokemon(PokemonDefinition definition, int level, boolean isDouble) {
        MovesetDefinition moves = (MovesetDefinition)RandomHelper.getRandomElementFromList(definition.getMovesets());
        if (level >= moves.minLevel && level >= definition.getMinLevel() && level <= definition.getMaxLevel()) {
            if (!moves.doubleOnly || isDouble) {
                Pokemon pokemon = PokemonFactory.create(PokemonSpecificationProxy.create(new String[]{definition.getPokemon().getName(), "form:" + moves.form, "lvl:" + Math.max(1, Math.min(PixelmonServerConfig.maxLevel, RandomHelper.getRandomNumberBetween(level - 1, level + 1)))}));
                if (this.isGymLeader) {
                    pokemon.getEVs().setStat(BattleStatsType.ATTACK, moves.evAtk);
                    pokemon.getEVs().setStat(BattleStatsType.DEFENSE, moves.evDef);
                    pokemon.getEVs().setStat(BattleStatsType.HP, moves.evHP);
                    pokemon.getEVs().setStat(BattleStatsType.SPECIAL_ATTACK, moves.evSpAtk);
                    pokemon.getEVs().setStat(BattleStatsType.SPECIAL_DEFENSE, moves.evSpDef);
                    pokemon.getEVs().setStat(BattleStatsType.SPEED, moves.evSpeed);
                    pokemon.getEVs().setStat(BattleStatsType.ATTACK, moves.ivAtk);
                    pokemon.getEVs().setStat(BattleStatsType.DEFENSE, moves.ivDef);
                    pokemon.getEVs().setStat(BattleStatsType.HP, moves.ivHP);
                    pokemon.getEVs().setStat(BattleStatsType.SPECIAL_ATTACK, moves.ivSpAtk);
                    pokemon.getEVs().setStat(BattleStatsType.SPECIAL_DEFENSE, moves.ivSpDef);
                    pokemon.getEVs().setStat(BattleStatsType.SPEED, moves.ivSpeed);
                    if (moves.nature != null && moves.nature.length > 0) {
                        pokemon.setNature((Nature)RandomHelper.getRandomElementFromArray(moves.nature));
                    }
                }

                pokemon.setHealth(pokemon.getMaxHealth());
                if (moves.ability != null && moves.ability.length > 0) {
                    pokemon.setAbility((Ability)RandomHelper.getRandomElementFromArray(moves.ability));
                } else {
                    List<Ability> randomAbilities = new ArrayList();
                    Ability[] var7 = pokemon.getForm().getAbilities().getAll();
                    int var8 = var7.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        Ability ability = var7[var9];
                        if (ability != null) {
                            randomAbilities.add(ability);
                        }
                    }

                    if (!randomAbilities.isEmpty()) {
                        pokemon.setAbility((Ability)RandomHelper.getRandomElementFromList(randomAbilities));
                    }
                }

                if (moves.heldItem != null && moves.heldItem.length > 0) {
                    BaseShopItem item = ServerNPCRegistry.shopkeepers.getItem((String)RandomHelper.getRandomElementFromArray(moves.heldItem));
                    if (item != null) {
                        item.getItem().setCount(1);
                        pokemon.setHeldItem(item.getItem().copy());
                    }
                }

                pokemon.setFriendship(255);
                Moveset moveset = pokemon.getMoveset();
                moveset.clear();
                this.addGymTrainerMove(moves.move1, pokemon, moves.ivsDefined);
                this.addGymTrainerMove(moves.move2, pokemon, moves.ivsDefined);
                this.addGymTrainerMove(moves.move3, pokemon, moves.ivsDefined);
                this.addGymTrainerMove(moves.move4, pokemon, moves.ivsDefined);
                this.party.add(pokemon);
                int pos = ((StoragePosition)this.party.find(pokemon.getUUID()).getStorageAndPosition().getB()).order;
                if (moves.lead && pos != 0) {
                    this.party.swap(pos, 0);
                }

                this.calculateGen();
            }
        }
    }

    private void addGymTrainerMove(String[] possibleMoves, Pokemon pokemon, boolean ivsDefined) {
        if (possibleMoves != null && possibleMoves.length > 0) {
            Moveset moveset = pokemon.getMoveset();
            int randomIndex = this.level.random.nextInt(possibleMoves.length);
            String selectedMove = possibleMoves[randomIndex];
            Element hiddenPowerType = null;
            if (selectedMove.contains("Hidden Power")) {
                if (!ivsDefined) {
                    hiddenPowerType = Element.parseType(selectedMove.replace("Hidden Power ", ""));
                }

                selectedMove = "Hidden Power";
            }

            Attack attack = new Attack(selectedMove);
            if (attack != null && !moveset.contains(attack)) {
                moveset.add(attack);
                if (hiddenPowerType != null) {
                    pokemon.getIVs().copyIVs(HiddenPower.getOptimalIVs(hiddenPowerType));
                }
            } else {
                String[] reducedPossible = (String[]) ArrayUtils.remove(possibleMoves, randomIndex);
                this.addGymTrainerMove(reducedPossible, pokemon, ivsDefined);
            }
        }

    }

    @Override
    public void startBattle(BattleParticipant battleParticipant) {
        this.calculateGen();
        if (battleParticipant instanceof PlayerParticipant) {
            PlayerParticipant player = (PlayerParticipant)battleParticipant;
            EnumEncounterMode encounterMode = this.getEncounterMode();
            if (encounterMode == EnumEncounterMode.OncePerMCDay) {
                this.playerEncounters.put(player.player.getUUID(), this.level.getGameTime());
            } else if (encounterMode == EnumEncounterMode.OncePerDay) {
                this.playerEncounters.put(player.player.getUUID(), System.currentTimeMillis());
            }
        }

        this.healAllPokemon();
    }

    @Override
    public Quadstate calculateGen() {
        return this.party.isOldGen(this.level.dimension());
    }

    @Override
    public void loseBattle(ArrayList<BattleParticipant> opponents) {
        if (opponents.get(0) instanceof PlayerParticipant) {
            String langCode = ((ServerPlayerEntity)((BattleParticipant)opponents.get(0)).getEntity()).getLanguage();
            if (this.getLoseMessage(langCode) != null) {
                ChatHandler.sendBattleMessage(opponents, this.getLoseMessage(langCode), new Object[0]);
            }
        }

        if (opponents.size() == 1 && opponents.get(0) instanceof PlayerParticipant) {
            ServerPlayerEntity player = (ServerPlayerEntity)((BattleParticipant)opponents.get(0)).getEntity();
            Pixelmon.EVENT_BUS.post(new BeatTrainerEvent(player, this));
            if (this.getEncounterMode() == EnumEncounterMode.Once) {
                this.remove();
                this.removeAfterChangingDimensions();
            } else {
                this.healAllPokemon();
                if (this.getEncounterMode() == EnumEncounterMode.OncePerPlayer) {
                    this.playerEncounters.put(player.getUUID(), this.level.getGameTime());
                }
            }

            if (player.level.getPlayerByUUID(player.getUUID()) == null) {
                return;
            }

            int calculatedWinMoney;
            if (this.winMoney > 0) {
                calculatedWinMoney = this.winMoney * this.party.getAverageLevel();
                if (!this.removeWhenFarAway(0.0)) {
                    calculatedWinMoney = this.winMoney;
                }

                PlayerParticipant playerParticipant = (PlayerParticipant)((PlayerParticipant)opponents.get(0));
                calculatedWinMoney *= playerParticipant.getPrizeMoneyMultiplier();
                BankAccount account = (BankAccount) BankAccountProxy.getBankAccount(playerParticipant.player).orElse(null);
                if (account != null) {
                    account.add(calculatedWinMoney);
                    if (playerParticipant.bc == null) {
                        ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.entitytrainer.winnings", new Object[]{"" + calculatedWinMoney, this.getName(player.getLanguage())});
                    } else {
                        ChatHandler.sendBattleMessage(playerParticipant.getEntity(), "pixelmon.entitytrainer.winnings", new Object[]{"" + calculatedWinMoney, this.getName(player.getLanguage())});
                    }
                }
            }

            int number2;
            if (this.winnings.length > 0) {
                ArrayList<DroppedItem> drops = new ArrayList();
                number2 = 0;
                ItemStack[] var12 = this.winnings;
                int var6 = var12.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    ItemStack item = var12[var7];
                    if (item.getCount() == 0) {
                        item.setCount(1);
                    }

                    drops.add(new DroppedItem(item.copy(), number2++));
                }

                DropItemQueryList.register(this, drops, player);
            }

            if (this.getBaseTrainer() != null && this.getBaseTrainer().name.equals("Fisherman")) {
                calculatedWinMoney = RandomHelper.getRandomNumberBetween(1, 100);
                number2 = RandomHelper.getRandomNumberBetween(1, 1000);
                if (calculatedWinMoney == 43) {
                    DropItemHelper.giveItemStack(player, new ItemStack(PixelmonItems.good_rod), false);
                    ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.entitytrainer.goodrod", new Object[0]);
                } else if (number2 == 564) {
                    DropItemHelper.giveItemStack(player, new ItemStack(PixelmonItems.super_rod), false);
                    ChatHandler.sendFormattedChat(player, TextFormatting.GREEN, "pixelmon.entitytrainer.superrod", new Object[0]);
                }
            }
        }

    }

    @Override
    public void winBattle(ArrayList<BattleParticipant> opponents) {
        if (opponents.get(0) instanceof PlayerParticipant) {
            String langCode = ((ServerPlayerEntity)((BattleParticipant)opponents.get(0)).getEntity()).getLanguage();
            if (this.getWinMessage(langCode) != null) {
                ChatHandler.sendBattleMessage(opponents, this.getWinMessage(langCode), new Object[0]);
            }
        }

        if (opponents.size() == 1 && opponents.get(0) instanceof PlayerParticipant) {
            ServerPlayerEntity player = (ServerPlayerEntity)((BattleParticipant)opponents.get(0)).getEntity();
            Pixelmon.EVENT_BUS.post(new LostToTrainerEvent(player, this));
        }

    }

    @Override
    public void healAllPokemon() {
        this.party.getTeam().forEach(Pokemon::heal);
    }

    @Override
    public void restoreAllFriendship() {
        this.party.getTeam().forEach((pokemon) -> {
            pokemon.setFriendship(255);
        });
    }

    @Override
    public UUID getNextPokemonUUID() {
        Pokemon pokemon = this.party.findOne((p) -> {
            return !p.isEgg() && !p.getPixelmonEntity().isPresent();
        });
        return pokemon == null ? null : pokemon.getUUID();
    }

    @Override
    public int getTrainerLevel() {
        return this.trainerLevel;
    }

    @Override
    public boolean interactWithNPC(PlayerEntity player, Hand hand) {
        Pixelmon.EVENT_BUS.post(new NPCEvent.Interact(this, EnumNPCType.Trainer, player));
        return false;
    }


    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        CompoundNBT pokemonNbt = new CompoundNBT();
        this.party.writeToNBT(pokemonNbt);
        nbt.put("pokeStore", pokemonNbt);
        nbt.putString("BossTier", this.getBossTier().getID());
        if (this.greeting != null) {
            nbt.putString("Greeting", this.greeting);
        }

        if (this.winMessage != null) {
            nbt.putString("WinMessage", this.winMessage);
        }

        if (this.loseMessage != null) {
            nbt.putString("LoseMessage", this.loseMessage);
        }

        CompoundNBT tmpWinnings = new CompoundNBT();

        CompoundNBT commandsNbt;
        for(int i = 0; i < this.winnings.length; ++i) {
            commandsNbt = new CompoundNBT();
            if (this.winnings[i] != null) {
                this.winnings[i].save(commandsNbt);
                tmpWinnings.put("item" + i, commandsNbt);
            }
        }

        nbt.putInt("NPCLevel", this.pokemonLevel);
        nbt.putInt("WinMoney", this.winMoney);
        nbt.put("WinningsTag", tmpWinnings);
        EnumEncounterMode mode = this.getEncounterMode();
        nbt.putShort("EncMode", (short)mode.ordinal());
        nbt.putInt("EngageDistance", this.getEngageDistance());
        if (mode != EnumEncounterMode.Once && mode != EnumEncounterMode.Unlimited) {
            ListNBT list = new ListNBT();

            CompoundNBT compound;
            for(Iterator var6 = this.playerEncounters.entrySet().iterator(); var6.hasNext(); list.add(compound)) {
                Map.Entry<UUID, Long> pair = (Map.Entry)var6.next();
                compound = new CompoundNBT();
                compound.putUUID("UUID", (UUID)pair.getKey());
                if (mode != EnumEncounterMode.OncePerPlayer) {
                    compound.putLong("time", (Long)pair.getValue());
                }
            }

            nbt.put("Encounters", list);
        }

        nbt.putShort("BattleAIMode", (short)this.getBattleAIMode().ordinal());
        nbt.putBoolean("DefaultName", this.usingDefaultName);
        nbt.putBoolean("DefaultWin", this.usingDefaultWin);
        nbt.putBoolean("DefaultLose", this.usingDefaultLose);
        nbt.putBoolean("DefaultGreet", this.usingDefaultGreeting);
        nbt.putString("TrainerIndex", this.trainerId);
        nbt.putInt("ChatIndex", this.chatIndex);
        nbt.putInt("hasMegaItem", this.getMegaItem().ordinal());
        if (this.getOldGen() != null) {
            nbt.putInt("oldGen", this.getOldGen().ordinal());
        }

        if (this.getAIMode() == EnumTrainerAI.StillAndEngage) {
            nbt.putFloat("TrainerRotation", this.startRotationYaw);
        }

        if (nbt.contains("EngageDistance")) {
            this.setEngageDistance(nbt.getInt("EngageDistance"));
        }

        nbt.putBoolean("GymLeader", this.isGymLeader);
        this.battleRules.writeToNBT(nbt);
        commandsNbt = nbt.getCompound("Commands");
        ListNBT winList = new ListNBT();
        Iterator var15 = this.winCommands.iterator();

        while(var15.hasNext()) {
            String winCommand = (String)var15.next();
            winList.add(StringNBT.valueOf(winCommand));
        }

        commandsNbt.put("winCommands", winList);
        ListNBT loseList = new ListNBT();
        Iterator var18 = this.loseCommands.iterator();

        while(var18.hasNext()) {
            String loseCommand = (String)var18.next();
            loseList.add(StringNBT.valueOf(loseCommand));
        }

        commandsNbt.put("loseCommands", loseList);
        ListNBT forfeitList = new ListNBT();
        Iterator var20 = this.forfeitCommands.iterator();

        while(var20.hasNext()) {
            String forfeitCommand = (String)var20.next();
            forfeitList.add(StringNBT.valueOf(forfeitCommand));
        }

        commandsNbt.put("forfeitCommands", forfeitList);
        ListNBT preBattleList = new ListNBT();
        Iterator var22 = this.preBattleCommands.iterator();

        while(var22.hasNext()) {
            String preBattleCommand = (String)var22.next();
            preBattleList.add(StringNBT.valueOf(preBattleCommand));
        }

        commandsNbt.put("preBattleCommands", preBattleList);
        nbt.put("Commands", commandsNbt);
    }

    @Override
    public boolean canStartBattle(PlayerEntity opponent, boolean printMessages) {
        if (opponent == null) {
            return true;
        } else if (!this.canEngage) {
            return false;
        } else if (this.battleController != null) {
            if (printMessages) {
                ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.inbattle", new Object[0]);
            }

            return false;
        } else {
            Long lastEncounter = (Long)this.playerEncounters.get(opponent.getUUID());
            if (lastEncounter != null) {
                EnumEncounterMode mode = this.getEncounterMode();
                long curTime;
                long oldTime;
                long curDay;
                long oldDay;
                if (mode == EnumEncounterMode.OncePerDay) {
                    curTime = System.currentTimeMillis();
                    oldTime = lastEncounter;
                    curDay = curTime / 86400000L;
                    oldDay = oldTime / 86400000L;
                    if (curDay <= oldDay) {
                        if (printMessages) {
                            ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.onceday", new Object[0]);
                        }

                        return false;
                    }
                } else if (mode == EnumEncounterMode.OncePerMCDay) {
                    curTime = this.level.getGameTime();
                    oldTime = lastEncounter;
                    curDay = curTime / 24000L;
                    oldDay = oldTime / 24000L;
                    if (curDay <= oldDay) {
                        if (printMessages) {
                            ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.oncemcday", new Object[0]);
                        }

                        return false;
                    }
                } else if (mode == EnumEncounterMode.OncePerPlayer) {
                    if (printMessages) {
                        ChatHandler.sendChat(opponent, "pixelmon.entitytrainer.onceplayer", new Object[0]);
                    }

                    return false;
                }
            }

            return true;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        if (nbt.contains("DefaultName")) {
            this.usingDefaultName = nbt.getBoolean("DefaultName");
            this.usingDefaultWin = nbt.getBoolean("DefaultWin");
            this.usingDefaultLose = nbt.getBoolean("DefaultLose");
            this.usingDefaultGreeting = nbt.getBoolean("DefaultGreet");
            this.trainerId = nbt.getString("TrainerIndex");
            this.chatIndex = nbt.getInt("ChatIndex");
        }

        super.readAdditionalSaveData(nbt);
        if (nbt.contains("BossTier")) {
            this.setBossTier(BossTierRegistry.getBossTierOrNotBoss(nbt.getString("BossTier")));
        }

        this.party.readFromNBT(nbt.getCompound("pokeStore"));
        if (nbt.contains("Greeting")) {
            this.greeting = nbt.getString("Greeting");
            this.winMessage = nbt.getString("WinMessage");
            this.loseMessage = nbt.getString("LoseMessage");
        }

        if (nbt.contains("hasMegaItem")) {
            this.setMegaItem(EnumMegaItemsUnlocked.values()[nbt.getInt("hasMegaItem")]);
        }

        if (nbt.contains("oldGen")) {
            this.setOldGenMode(EnumOldGenMode.values()[nbt.getInt("oldGen")]);
        }

        CompoundNBT cmdNbt;
        int numEncounters;
        String uuidStr;
        int i;
        if (nbt.contains("WinningsTag")) {
            cmdNbt = nbt.getCompound("WinningsTag");
            this.winnings = new ItemStack[cmdNbt.getAllKeys().size()];
            numEncounters = 0;

            for(Iterator var4 = cmdNbt.getAllKeys().iterator(); var4.hasNext(); this.winnings[numEncounters++] = ItemStack.of(cmdNbt.getCompound(uuid.toString()))) {
                uuidStr = (String)var4.next();
            }
        } else if (nbt.contains("Winnings")) {
            int[] testArray = nbt.getIntArray("Winnings");
            ArrayList<Item> array = new ArrayList();
            int[] var16 = testArray;
            int var19 = testArray.length;

            for(int var6 = 0; var6 < var19; ++var6) {
                int aTestArray = var16[var6];
                array.add(Item.byId(aTestArray));
            }

            this.winnings = new ItemStack[array.size()];

            for(i = 0; i < array.size(); ++i) {
                this.winnings[i] = new ItemStack((IItemProvider)array.get(i));
            }
        }

        if (nbt.contains("TrainerRotation")) {
            this.setStartRotationYaw(nbt.getFloat("TrainerRotation"));
        }

        ListNBT preBattle;
        if (nbt.contains("EncMode")) {
            EnumEncounterMode mode = EnumEncounterMode.getFromIndex(nbt.getShort("EncMode"));
            this.setEncounterMode(mode);
            this.playerEncounters.clear();
            if (mode != EnumEncounterMode.Once && mode != EnumEncounterMode.Unlimited && nbt.contains("numEncounters")) {
                numEncounters = nbt.getInt("numEncounters");

                for(i = 0; i < numEncounters; ++i) {
                    uuid = UUID.fromString(nbt.getString("encPl" + i));
                    long time = 0L;
                    if (mode != EnumEncounterMode.OncePerPlayer) {
                        time = nbt.getLong("encTi" + i);
                        long curDay;
                        long oldDay;
                        if (mode == EnumEncounterMode.OncePerDay) {
                            curDay = System.currentTimeMillis() / 86400000L;
                            oldDay = time / 86400000L;
                            if (curDay <= oldDay) {
                                this.playerEncounters.put(UUID.fromString(uuid.toString()), time);
                            }
                        } else if (mode == EnumEncounterMode.OncePerMCDay) {
                            curDay = this.level.getGameTime() / 24000L;
                            oldDay = time / 24000L;
                            if (curDay <= oldDay) {
                                this.playerEncounters.put(UUID.fromString(uuid.toString()), time);
                            }
                        }
                    } else {
                        this.playerEncounters.put(UUID.fromString(uuid.toString()), 0L);
                    }
                }
            }

            if (mode != EnumEncounterMode.Once && mode != EnumEncounterMode.Unlimited && nbt.contains("Encounters")) {
                preBattle = nbt.getList("Encounters", 10);

                for(i = 0; i < preBattle.size(); ++i) {
                    CompoundNBT compound = preBattle.getCompound(i);
                    UUID uuid = compound.getUUID("UUID");
                    long time = mode != EnumEncounterMode.OncePerPlayer ? compound.getLong("time") : 0L;
                    long curDay;
                    long oldDay;
                    if (mode == EnumEncounterMode.OncePerDay) {
                        curDay = System.currentTimeMillis() / 86400000L;
                        oldDay = time / 86400000L;
                        if (curDay <= oldDay) {
                            this.playerEncounters.put(uuid, time);
                        }
                    } else if (mode == EnumEncounterMode.OncePerMCDay) {
                        curDay = this.level.getGameTime() / 24000L;
                        oldDay = time / 24000L;
                        if (curDay <= oldDay) {
                            this.playerEncounters.put(uuid, time);
                        }
                    } else {
                        this.playerEncounters.put(uuid, 0L);
                    }
                }
            }
        } else {
            this.setEncounterMode(EnumEncounterMode.Once);
        }

        if (nbt.contains("BattleAIMode")) {
            this.setBattleAIMode(BattleAIMode.getFromIndex(nbt.getShort("BattleAIMode")));
        }

        if (nbt.contains("NPCLevel")) {
            this.pokemonLevel = nbt.getInt("NPCLevel");
            SYNC_TRAINER_LEVEL.set(this, this.pokemonLevel);
            this.updateTrainerLevel();
        }

        if (nbt.contains("Commands")) {
            cmdNbt = nbt.getCompound("Commands");
            if (cmdNbt.contains("winCommands")) {
                preBattle = cmdNbt.getList("winCommands", 8);

                for(i = 0; i < preBattle.size(); ++i) {
                    this.winCommands.add(preBattle.getString(i));
                }
            }

            if (cmdNbt.contains("loseCommands")) {
                preBattle = cmdNbt.getList("loseCommands", 8);

                for(i = 0; i < preBattle.size(); ++i) {
                    this.loseCommands.add(preBattle.getString(i));
                }
            }

            if (cmdNbt.contains("forfeitCommands")) {
                preBattle = cmdNbt.getList("forfeitCommands", 8);

                for(i = 0; i < preBattle.size(); ++i) {
                    this.forfeitCommands.add(preBattle.getString(i));
                }
            }

            if (cmdNbt.contains("preBattleCommands")) {
                preBattle = cmdNbt.getList("preBattleCommands", 8);

                for(i = 0; i < preBattle.size(); ++i) {
                    this.preBattleCommands.add(preBattle.getString(i));
                }
            }
        }

        if (nbt.contains("WinMoney")) {
            this.winMoney = nbt.getInt("WinMoney");
        }

        if (nbt.contains("GymLeader")) {
            this.isGymLeader = nbt.getBoolean("GymLeader");
        }

        this.battleRules.readFromNBT(nbt);
        if (nbt.contains("BattleType")) {
            this.battleRules.set(BattleRuleRegistry.BATTLE_TYPE, BattleType.values()[nbt.getShort("BattleType")]);
            nbt.remove("BattleType");
        }

        this.updateTrainerLevel();
    }

    @Override
    public void randomisePokemon(PlayerEntity player) {
        BaseTrainer base = this.getBaseTrainer();
        ArrayList randomParty;
        if (base.name.equals("Steve")) {
            int partySize = RandomHelper.getRandomNumberBetween(1, 6);
            randomParty = new ArrayList(partySize);

            for(int i = 0; i < partySize; ++i) {
                randomParty.add(PokemonSpecificationProxy.create(new String[]{PixelmonSpecies.getRandomSpecies().getName()}).create());
            }

            this.pokemonLevel = RandomHelper.getRandomNumberBetween(2, 99);
        } else {
            TrainerData data = ServerNPCRegistry.trainers.getRandomData(base);
            if (data == null) {
                Pixelmon.LOGGER.error(base.name + " has no trainer data set.");
                return;
            }

            randomParty = data.getRandomParty();
        }

        this.loadPokemon(randomParty);
        this.updateTrainerLevel();
        NetworkHelper.sendPacket(new ClearTrainerPokemonPacket(), (ServerPlayerEntity)player);
        Iterator var7 = this.party.getTeam().iterator();

        while(var7.hasNext()) {
            Pokemon pokemon = (Pokemon)var7.next();
            NetworkHelper.sendPacket(new StoreTrainerPokemonPacket(pokemon), (ServerPlayerEntity)player);
        }

    }

    @Override
    public BossTier getBossTier() {
        return this.bossTier == null ? BossTierRegistry.NOT_BOSS : this.bossTier;
    }

    @Override
    public void setBossTier(BossTier tier) {
        if (tier == null) {
            tier = BossTierRegistry.NOT_BOSS;
        }

        SYNC_BOSS_MODE.set(this, tier);
    }

    @Override
    public BattleAIMode getBattleAIMode() {
        return this.battleAIMode;
    }

    @Override
    public void setBattleAIMode(BattleAIMode mode) {
        if (mode != null) {
            SYNC_BATTLE_AI.set(this, mode);
        }

    }

    @Override
    public BattleType getBattleType() {
        return (BattleType)this.battleRules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE);
    }

    @Override
    public void updateTrainerLevel() {
        int lvlTotal = 0;
        int count = 0;

        for(Iterator var3 = this.party.getTeam().iterator(); var3.hasNext(); ++count) {
            Pokemon pokemon = (Pokemon)var3.next();
            lvlTotal += pokemon.getPokemonLevel();
        }

        if (count < 1) {
            count = 1;
        }

        SYNC_TRAINER_LEVEL.set(this, lvlTotal / count);
    }

    @Override
    public void initDefaultAI() {
        this.getNavigation().setCanFloat(true);
    }

    @Override
    public void update(SetTrainerData p) {
        if (!p.greeting.equals("")) {
            this.greeting = p.greeting;
            this.usingDefaultGreeting = false;
        }

        if (!p.lose.equals("")) {
            this.loseMessage = p.lose;
            this.usingDefaultLose = false;
        }

        if (!p.win.equals("")) {
            this.winMessage = p.win;
            this.usingDefaultWin = false;
        }

        this.winMoney = p.winMoney;
        if (p.rules != null) {
            this.battleRules = p.rules;
        }

        if (p.name != null && !p.name.isEmpty()) {
            this.setName(p.name);
        }

    }

    @Override
    public void setStartRotationYaw(float f) {
        this.startRotationSet = true;
        this.startRotationYaw = f;
        this.yRot = f;
        this.yHeadRot = f;
    }

    @Override
    public String getDisplayText() {
        String s = "boss";
        if (this.getBossTier() != null && this.getBossTier().isNotBoss()) {
            s = "" + this.getTrainerLevel();
        }

        return s;
    }

    @Override
    public String getSubTitleText() {
        return I18n.get("gui.screenpokechecker.lvl", new Object[0]);
    }

    @Override
    public void setBattleController(BattleController battleController) {
        this.battleController = battleController;
    }

    @Override
    public BattleController getBattleController() {
        return this.battleController;
    }


    @Override
    public MobEntity getEntity() {
        return this;
    }

    @Override
    public void setAttackTargetPix(LivingEntity entity) {
        this.setTarget(entity);
    }

    @Override
    public void updateDrops(ItemStack[] drops) {
        this.winnings = drops;
    }

    @Override
    public ItemStack[] getWinnings() {
        return this.winnings;
    }

    @Override
    public void setPokemonLevel(int pokemonLevel) {
        this.pokemonLevel = pokemonLevel;
        SYNC_TRAINER_LEVEL.set(this, pokemonLevel);
        BaseTrainer base = this.getBaseTrainer();
        TrainerData data = ServerNPCRegistry.trainers.getRandomData(base);
        this.loadPokemon(data.getRandomParty());
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> key) {
        super.onSyncedDataUpdated(key);
        DataSync<?, ?> dataSync = this.dataSyncManager.getSync(key);
        if (dataSync != null) {
            dataSync.onSyncedDataUpdated(key, this);
        }

    }


    static {
        SYNC_ENCOUNTER_MODE = new DataSync<>(SpecialNpcTrainerB.class, PixelmonDataSerializers.ENCOUNTER_MODE, (npcTrainer, o) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            specialNpcTrainer.encounterMode = (EnumEncounterMode) o;
        });
        SYNC_MEGA_ITEM = new DataSync<>(SpecialNpcTrainerB.class, PixelmonDataSerializers.MEGA_ITEM, (npcTrainer, enumMegaItemsUnlocked) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            ((SpecialNpcTrainerB) npcTrainer).megaItem = (EnumMegaItemsUnlocked) enumMegaItemsUnlocked;
        });
        SYNC_OLD_GEN = new DataSync<>(SpecialNpcTrainerB.class, PixelmonDataSerializers.OLD_GEN_MODE, (npcTrainer, enumOldGenMode) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            specialNpcTrainer.oldGenMode = (EnumOldGenMode) enumOldGenMode;
        });
        SYNC_BOSS_MODE = new DataSync<>(SpecialNpcTrainerB.class, PixelmonDataSerializers.BOSS_MODE, (npcTrainer, bossTier) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            specialNpcTrainer.bossTier = (BossTier) bossTier;
        });
        SYNC_TRAINER_LEVEL = new DataSync<>(SpecialNpcTrainerB.class, DataSerializers.INT, (npcTrainer, integer) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            specialNpcTrainer.trainerLevel = (int) integer;
        });
        SYNC_BATTLE_AI = new DataSync<>(SpecialNpcTrainerB.class, PixelmonDataSerializers.BATTLE_AI_MODE, (npcTrainer, battleAIMode) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            specialNpcTrainer.battleAIMode = (BattleAIMode) battleAIMode;
        });
        SYNC_ENGAGE_DISTANCE = new DataSync<>(SpecialNpcTrainerB.class, DataSerializers.INT, (npcTrainer, integer) -> {
            SpecialNpcTrainerB specialNpcTrainer = (SpecialNpcTrainerB) npcTrainer;
            specialNpcTrainer.engageDistance = (int) integer;
        });
    }



}

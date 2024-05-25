package es.boffmedia.teras.objects.quests;

import es.boffmedia.teras.Teras;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.IDialogHandler;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Availability;

import java.util.*;

public class PlayerQuests {
    HashMap<Integer, QuestDataBase> quests;
    HashMap<Integer, String> categories;


    transient IQuest[] mActivas;
    transient IQuest[] mCompletadas;
    public PlayerQuests(UUID uuid){
        NpcAPI npcApi = NpcAPI.Instance();

        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        PlayerWrapper wrapper = new PlayerWrapper(player);

        quests = new HashMap<>();
        categories = new HashMap<>();

        mActivas = wrapper.getActiveQuests();
        mCompletadas = wrapper.getFinishedQuests();


        /*
        for (IQuest activeQuest : wrapper.getActiveQuests()) {
            QuestDataBase questData = new QuestDataBase(activeQuest);
            questData.setStatus(QuestStatus.ACTIVE);
            questData.setObjectives(activeQuest, wrapper);
            questData.setRewards(activeQuest.getRewards().getItems());
            quests.put(questData.getId(), questData);
        }

        for (IQuest finishedQuest : wrapper.getFinishedQuests()) {
            QuestDataBase questData = new QuestDataBase(finishedQuest, true);
            questData.setStatus(QuestStatus.COMPLETED);
            questData.setObjectives(finishedQuest, wrapper);
            questData.setRewards(finishedQuest.getRewards().getItems());
            quests.put(questData.getId(), questData);
        }

        for (IQuest quest : QuestController.instance.quests.values()) {
            if(quest.getCategory() == null) continue;
            QuestDataBase questData = new QuestDataBase(quest, true);
            questData.setStatus(QuestStatus.NOT_STARTED);
            questData.setObjectives(quest, wrapper);
            questData.setRewards(quest.getRewards().getItems());
            if(!quests.containsKey(questData.getId())) {
                quests.put(questData.getId(), questData);
            }
        }*/

        IDialogHandler dialogHandler = npcApi.getDialogs();

        dialogHandler.categories().forEach(category -> {
            Teras.getLogger().info("Category: " + category.getName());
            category.dialogs().forEach(dialog -> {
                if(dialog.getQuest() !=null){
                    IQuest quest = dialog.getQuest();
                    QuestRequirement requirement = new QuestRequirement();

                    Availability availability = (Availability) dialog.getAvailability();
                    for (int i = 0; i < 4; i++) {
                        requirement.addQuest(availability.getQuest(i));
                        requirement.addDialog(availability.getDialog(i));
                    }

                    requirement.setTime(availability.getDaytime());
                    requirement.setLevel(availability.getMinPlayerLevel());

                    requirement.addFactionRequirement(availability.factionId, availability.factionAvailable, availability.factionStance);
                    requirement.addFactionRequirement(availability.faction2Id, availability.faction2Available, availability.faction2Stance);

                    requirement.addScoreboardRequirement(availability.scoreboardObjective, availability.scoreboardType, availability.scoreboardValue);
                    requirement.addScoreboardRequirement(availability.scoreboard2Objective, availability.scoreboard2Type, availability.scoreboard2Value);

                    requirement.setAvailable(availability.isAvailable(wrapper));


                    addPlayerQuest(quest, wrapper, requirement);
                } else {
                    Teras.getLogger().info("Skipping Dialog: " + dialog.getText());
                }
            });
        });



    }
    public PlayerQuests(UUID uuid, boolean load){
        NpcAPI npcApi = NpcAPI.Instance();
        IQuestHandler questHandler = npcApi.getQuests();
        IDialogHandler dialogHandler = npcApi.getDialogs();

        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        PlayerWrapper wrapper = new PlayerWrapper(player);
        
        quests = new HashMap<>();
        categories = new HashMap<>();

        mActivas = wrapper.getActiveQuests();
         mCompletadas = wrapper.getFinishedQuests();

        dialogHandler.categories().forEach(category -> {
            Teras.getLogger().info("Category: " + category.getName());
            category.dialogs().forEach(dialog -> {
                if(dialog.getQuest() !=null){
                    IQuest quest = dialog.getQuest();
                    QuestRequirement requirement = new QuestRequirement();
                    Availability availability = (Availability) dialog.getAvailability();

                    for (int i = 0; i < 4; i++) {
                        requirement.addQuest(availability.getQuest(i));
                        requirement.addDialog(availability.getDialog(i));
                    }
                    addQuest(quest, wrapper, requirement);
                } else {
                    Teras.getLogger().info("Skipping Dialog: " + dialog.getText());
                }
            });
        });

        Teras.getLogger().info("Quests: " + quests.size());
        Teras.getLogger().info("Categories: " + categories.size());
    }

    public void addPlayerQuest(IQuest quest, PlayerWrapper wrapper, QuestRequirement requirements) {
        boolean available = requirements.isAvailable();
        QuestDataBase questData = new QuestDataBase(quest);

        ArrayList<QuestObjective> questObjectives = new ArrayList<>();
        ArrayList<QuestReward> questRewards = new ArrayList<>();

        if(Arrays.stream(mActivas).anyMatch(mActiva -> mActiva.getId() == quest.getId())) {
            questData.setStatus(QuestStatus.ACTIVE);
        } else if(Arrays.stream(mCompletadas).anyMatch(mCompleta -> mCompleta.getId() == quest.getId())) {
            questData.setStatus(QuestStatus.COMPLETED);
        } else if(available){
            questData.setStatus(QuestStatus.AVAILABLE);
        } else {
            questData.setStatus(QuestStatus.LOCKED);
        }

        questData.setObjectives(quest, wrapper);
        questData.setRewards(quest.getRewards().getItems());
        quests.put(questData.getId(), questData);


    }

    public void addQuest(IQuest quest, PlayerWrapper wrapper, QuestRequirement requirements){
        boolean available = requirements.isAvailable();
        QuestData questData = new QuestData(quest);

        ArrayList<QuestObjective> questObjectives = new ArrayList<>();
        ArrayList<QuestReward> questRewards = new ArrayList<>();

        if(Arrays.stream(mActivas).anyMatch(mActiva -> mActiva.getId() == quest.getId())) {
            questData.setStatus(QuestStatus.ACTIVE);
        } else if(Arrays.stream(mCompletadas).anyMatch(mCompleta -> mCompleta.getId() == quest.getId())) {
            questData.setStatus(QuestStatus.COMPLETED);
        } else if(available){
            questData.setStatus(QuestStatus.AVAILABLE);
        } else {
            questData.setStatus(QuestStatus.LOCKED);
        }


        IQuestObjective[] objectives = new IQuestObjective[0];

        try{
            objectives = quest.getObjectives(wrapper);
        } catch (Exception e){
            Teras.getLogger().info("Error: " + e.getMessage());
        }
        IItemStack[] rewards = quest.getRewards().getItems();



        for (IQuestObjective objective : objectives) {
            Teras.getLogger().info("Objective: " + objective.getText());
            QuestObjective questObjective = new QuestObjective(objective.getText(), objective.getProgress(), objective.getMaxProgress());
            questObjectives.add(questObjective);
        }

        for (IItemStack reward : rewards) {
            Teras.getLogger().info("Reward: " + reward.getName());
            if(reward.getName().equals("minecraft:air")) continue;
            QuestReward questReward = new QuestReward(reward.getName(), reward.getStackSize());
            questRewards.add(questReward);

        }


        Teras.getLogger().info("- - - - - - - - - -");
        questData.setCompleteText(quest.getCompleteText());
        questData.setLogText(quest.getLogText());
        questData.setRepeatable(quest.getIsRepeatable());
        questData.setNextQuest(quest.getNextQuest() != null ? quest.getNextQuest().getId() : -1);
        questData.setCategory(quest.getCategory().getName());



        questData.setObjectives(questObjectives);
        questData.setRewards(questRewards);
        questData.setRequirements(requirements);



        quests.put(questData.getId(), questData);
        categories.put(questData.getId(), questData.getCategory());

    }

    public HashMap<Integer, QuestDataBase> getQuests() {
        return quests;
    }

    public void setQuests(HashMap<Integer, QuestDataBase> quests) {
        this.quests = quests;
    }

    public HashMap<Integer, String> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<Integer, String> categories) {
        this.categories = categories;
    }
}

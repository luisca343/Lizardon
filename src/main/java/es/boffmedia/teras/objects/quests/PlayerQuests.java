package es.boffmedia.teras.objects.quests;

import es.boffmedia.teras.Teras;
import es.boffmedia.teras.util.FileHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.IDialogHandler;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.*;

public class PlayerQuests {
    HashMap<Integer, QuestData> quests;
    HashMap<Integer, String> categories;


    transient IQuest[] mActivas;
    transient IQuest[] mCompletadas;

    public PlayerQuests(UUID uuid){
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
                    boolean available = dialog.getAvailability().isAvailable(wrapper);
                    addQuest(quest, wrapper, available);
                } else {
                    Teras.getLogger().info("Skipping Dialog: " + dialog.getText());
                }
            });
        });


        /*
        questHandler.categories().forEach(category -> {
            Teras.getLogger().info("Category: " + category.getName());

            category.quests().forEach(quest -> {
                addQuest(quest, wrapper);
            });
        });
        */

        Teras.getLogger().info("Quests: " + quests.size());
        Teras.getLogger().info("Categories: " + categories.size());


        /*
        Map<Integer, QuestData> datosMisionMap;
        NpcAPI npcApi = NpcAPI.Instance();


        datosMisionMap = (Map<Integer, QuestData>) FileHelper.readFile("config/teras/misiones.json", new TypeToken< Map<Integer, QuestData>>(){}.getType());
        if(datosMisionMap == null){
            datosMisionMap = new HashMap<>();
        }

        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);


        PlayerWrapper wrapper = new PlayerWrapper(player);
        IQuest[] mActivas = wrapper.getActiveQuests();
        IQuest[] mCompletadas = wrapper.getFinishedQuests();
        ArrayList<QuestData> misiones = new ArrayList<>();

        for(IQuest mActiva : mActivas){
            QuestData datos = datosMisionMap.get(mActiva.getId());
            datos.setStatus(QuestStatus.ACTIVE);

            IQuestObjective[] objetivos = mActiva.getObjectives(wrapper);
            ArrayList<QuestObjective> questObjectives = new ArrayList<>();
            if (objetivos.length > 0 ){
                for (IQuestObjective objetivo : objetivos) {
                    QuestObjective questObjective = new QuestObjective(objetivo.getText(), objetivo.getProgress(), objetivo.getMaxProgress());
                    questObjectives.add(questObjective);
                }
            }

            datos.setObjectives(questObjectives);
            misiones.add(datos);
        }

        for(IQuest mCompleta : mCompletadas){
            QuestData datos = datosMisionMap.get(mCompleta.getId());
            datos.setStatus(QuestStatus.COMPLETED);

            misiones.add(datos);
        }

        HashMap<String, Integer> categorias = new HashMap<>();

        for(IQuestCategory category : npcApi.getQuests().categories()){
            categorias.put(category.getName(), category.quests().size());
        }

        this.setQuests(misiones);
        this.setCategories(categorias);*/
    }

    public void addQuest(IQuest quest, PlayerWrapper wrapper, boolean available){
        QuestData questData = new QuestData(quest);
        Teras.getLogger().info("Quest: " + quest.getName());
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



        quests.put(questData.getId(), questData);
        categories.put(questData.getId(), questData.getCategory());

    }

    public HashMap<Integer, QuestData> getQuests() {
        return quests;
    }

    public void setQuests(HashMap<Integer, QuestData> quests) {
        this.quests = quests;
    }

    public HashMap<Integer, String> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<Integer, String> categories) {
        this.categories = categories;
    }
}

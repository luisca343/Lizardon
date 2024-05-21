package es.boffmedia.teras.objects.quests;

import es.boffmedia.teras.objects_old.misiones.QuestData;
import es.boffmedia.teras.util.FileHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerQuests {
    ArrayList<QuestData> quests;
    HashMap<String, Integer> categories;
    public PlayerQuests(UUID uuid){
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
        this.setCategories(categorias);
    }

    public ArrayList<QuestData> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<QuestData> quests) {
        this.quests = quests;
    }

    public HashMap<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, Integer> categories) {
        this.categories = categories;
    }
}

package es.allblue.lizardon.event;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.misiones.DatosMision;
import es.allblue.lizardon.util.FileHelper;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noppes.npcs.api.event.DialogEvent;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class CustomNPCsEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onReadDialogue(DialogEvent.OpenEvent event){

        Lizardon.getLogger().info("Se ha leido el dialogo");
    }

    @SubscribeEvent
    public static void openDialog(DialogEvent.OpenEvent event){
        IQuest quest = event.dialog.getQuest();
        ServerPlayerEntity player = event.player.getMCEntity();

        PlayerWrapper wrapper = new PlayerWrapper(player);

        if(quest == null || !wrapper.canQuestBeAccepted(quest.getId())) return;

        String npcName = event.npc.getName();
        Map<Integer, DatosMision> listaMisiones = (Map<Integer, DatosMision>) FileHelper.readFile("config/lizardon/misiones.json", new TypeToken<Map<Integer, DatosMision>>(){}.getType());
        if(listaMisiones == null){
            listaMisiones = new HashMap<>();
        }

        DatosMision datosMision = new DatosMision();
        DatosMision datosMisionArchivo = new DatosMision();

        if(listaMisiones.containsKey(quest.getId())){
            datosMisionArchivo = listaMisiones.get(quest.getId());
        }
        /* Datos de la mision */

        datosMision.setId(quest.getId());
        datosMision.setNombre(quest.getName());
        datosMision.setTextoLog(quest.getLogText());
        datosMision.setTextoCompletar(quest.getCompleteText());
        datosMision.setRepetible(quest.getIsRepeatable());
        datosMision.setTipo(quest.getType());
        datosMision.setSiguienteMision(-1);
        if(quest.getNextQuest() != null){
            datosMision.setSiguienteMision(quest.getNextQuest().getId());
        }
        datosMision.setCategoria(quest.getCategory().getName());

        /* Datos del NPC */
        datosMision.setNombreNPC(event.npc.getName());
        datosMision.setX(event.npc.getX());
        datosMision.setY(event.npc.getY());
        datosMision.setZ(event.npc.getZ());

        String rutaSkin = event.npc.getEntityNbt().getString("Texture");
        String[] partes = rutaSkin.split("/");
        datosMision.setSkin(partes[partes.length-1]);

        // Comparamos los datos de la mision actual con los datos de la mision guardada
        if(!datosMision.equals(datosMisionArchivo)){
            listaMisiones.put(quest.getId(), datosMision);
            FileHelper.writeFile("config/lizardon/misiones.json", listaMisiones);
        }
    }

    @SubscribeEvent
    public static void misionCumplida(QuestEvent.QuestCompletedEvent event){
        ServerPlayerEntity jugador = (ServerPlayerEntity) event.player.getMCEntity();
    }
}

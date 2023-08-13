package es.allblue.lizardon.event;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.DatosNPC;
import es.allblue.lizardon.util.FileHelper;
import es.allblue.lizardon.util.MessageUtil;
import io.leangen.geantyref.TypeToken;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.event.DialogEvent;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.handler.data.IQuest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CustomNPCsEvents {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onReadDialogue(DialogEvent.OpenEvent event){

        Lizardon.getLogger().info("Se ha leido el dialogo");
    }

    @SubscribeEvent
    public static void read(DialogEvent.OpenEvent event){
        String npcName = event.npc.getName();
        Map<String, DatosNPC> datosNpc = (Map<String, DatosNPC>) FileHelper.readFile("config/lizardon/npcs.json", new TypeToken<Map<String, DatosNPC>>(){}.getType());
        if(datosNpc == null){
            datosNpc = new HashMap<>();
        }

        DatosNPC datosNPC = new DatosNPC();
        if(datosNpc.containsKey(npcName)){
            datosNpc.get(npcName);
        }
        datosNPC.setNombre(npcName);
        datosNPC.setX(event.npc.getX());
        datosNPC.setY(event.npc.getY());
        datosNPC.setZ(event.npc.getZ());

        String rutaSkin = event.npc.getEntityNbt().getString("Texture");
        String[] partes = rutaSkin.split("/");
        datosNPC.setSkin(partes[partes.length-1]);

        datosNpc.put(npcName, datosNPC);
        FileHelper.writeFile("config/lizardon/npcs.json", datosNpc);

        Map<String, String> datosQuest = (Map<String, String>) FileHelper.readFile("config/lizardon/quests.json", new TypeToken<Map<String, String>>(){}.getType());
        if(datosQuest == null){
            datosQuest = new HashMap<>();
        }

        IQuest quest = event.dialog.getQuest();
        if(!datosQuest.containsKey(quest.getName())){
            datosQuest.put(quest.getName(), npcName);
            FileHelper.writeFile("config/lizardon/quests.json", datosQuest);
        }

        Lizardon.getLogger().info("Se ha leido el dialogo");
    }


    @SubscribeEvent
    public static void test(NpcEvent.InteractEvent event){
        /*
        Lizardon.getLogger().info("Se ha interactuado con un NPC");
        Map<String, DatosNPC> datosNpc = (Map<String, DatosNPC>) FileHelper.readFile("config/lizardon/npcs.json", new TypeToken<Map<String, DatosNPC>>(){}.getType());

        if(datosNpc == null) datosNpc = new HashMap<>();
        DatosNPC datos = new DatosNPC();
        datos.setX(event.npc.getX());
        datos.setY(event.npc.getY());
        datos.setZ(event.npc.getZ());

        ICustomNpc npc = event.npc;
        datos.setNombre(npc.getName());
        String rutaSkin = npc.getEntityNbt().getString("Texture");
        String[] partes = rutaSkin.split("/");
        datos.setSkin(partes[partes.length-1]);

        datosNpc.put(npc.getName(), datos);


        FileHelper.writeFile("config/lizardon/npcs.json", datosNpc);*/
    }

    @SubscribeEvent
    public static void misionCumplida(QuestEvent.QuestCompletedEvent event){
        ServerPlayerEntity jugador = (ServerPlayerEntity) event.player.getMCEntity();
        jugador.sendMessage(new StringTextComponent("Has completado '" + event.quest.getName()+"'"), UUID.randomUUID());
        Lizardon.getLogger().info("FINALIZADA");
    }
}

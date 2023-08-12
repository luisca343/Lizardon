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
    public static void test(NpcEvent.InteractEvent event){
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


        FileHelper.writeFile("config/lizardon/npcs.json", datosNpc);
    }

    @SubscribeEvent
    public static void misionCumplida(QuestEvent.QuestCompletedEvent event){
        ServerPlayerEntity jugador = (ServerPlayerEntity) event.player.getMCEntity();
        jugador.sendMessage(new StringTextComponent("Has completado '" + event.quest.getName()+"'"), UUID.randomUUID());
        Lizardon.getLogger().info("FINALIZADA");
    }
}

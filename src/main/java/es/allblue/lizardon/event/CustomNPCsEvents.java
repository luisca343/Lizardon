package es.allblue.lizardon.event;

import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.commands.TestCommand;
import es.allblue.lizardon.objects.DatosNPC;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.QuestEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "lizardon")
public class CustomNPCsEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new TestCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void test(NpcEvent.InteractEvent event){
        Lizardon.getLogger().info("Se ha interactuado con un NPC");
        Gson gson = new Gson();
        String nombreArchivo = "npcs.json";
        try{
            Reader reader = Files.newBufferedReader(Lizardon.PROXY.getRuta(nombreArchivo));
            Map<String, DatosNPC> datosNpc = gson.fromJson(reader, Map.class);
            if(datosNpc == null) datosNpc = new HashMap<>();
            DatosNPC datos = new DatosNPC();
            ICustomNpc npc = event.npc;
            datos.setNombre(npc.getName());
            String rutaSkin = npc.getEntityNbt().getString("Texture");

            String[] partes = rutaSkin.split("/");
            datos.setSkin(partes[partes.length-1]);

            datosNpc.put(npc.getName(), datos);
            FileWriter writer = new FileWriter(Lizardon.PROXY.getRuta(nombreArchivo).toString());
            gson.toJson(datosNpc, writer);
            writer.flush();
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void misionCumplida(QuestEvent.QuestCompletedEvent event){
        ServerPlayerEntity jugador = (ServerPlayerEntity) event.player.getMCEntity();
        jugador.sendMessage(new StringTextComponent("Has completado '" + event.quest.getName()+"'"), UUID.randomUUID());
        Lizardon.getLogger().info("FINALIZADA");
    }
}

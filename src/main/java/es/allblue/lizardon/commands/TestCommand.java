package es.allblue.lizardon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreenPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPCPacket;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class TestCommand {
    private static VoicechatServerApi SERVER_API;

    public TestCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("lizartest")
                .executes((command) -> {
                    return testCommand(command.getSource());
        }));
    }

    public int testCommand(CommandSource source) throws CommandSyntaxException {
        try{
            UUID uuid = UUID.fromString("TORRE_BATALLA_001");
            NetworkHelper.sendPacket(new ClientChangeOpenPCPacket(uuid), source.getPlayerOrException());
            OpenScreenPacket.open( source.getPlayerOrException(), EnumGuiScreen.PC, new int[0]);
        }catch(Exception e){
            source.getPlayerOrException().sendMessage(new StringTextComponent("Ha ocurrido un error"), UUID.randomUUID());
        }

        return 1;
    }
}

package es.allblue.lizardon.commands;

import es.allblue.lizardon.Lizardon;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class LizardonReload extends CommandBase {
    @Override
    public String getName() {
        return "lizardonreload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.lizardonreload.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Lizardon.cargarConfig();
        sender.sendMessage(new TextComponentString("Configuración recargada"));
    }
}
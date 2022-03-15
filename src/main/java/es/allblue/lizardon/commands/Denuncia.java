package es.allblue.lizardon.commands;

import es.allblue.lizardon.objects.requests.DenunciaJSON;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Denuncia extends CommandBase {
    @Override
    public String getName() {
        return "denunciar";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.denunciar.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            DenunciaJSON denuncia = new DenunciaJSON(args[0], args[1]);

            String res = RestApi.post("denunciar", denuncia);
        }
    }

package es.allblue.lizardon.commands;

import es.allblue.lizardon.objects.requests.GimnasioJSON;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Gimnasio extends CommandBase {
    @Override
    public String getName() {
        return "regcombate";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.regcombate.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GimnasioJSON gimnasio = new GimnasioJSON(args[0], args[1]);

        String res = RestApi.post("regcombate", gimnasio);
    }
}

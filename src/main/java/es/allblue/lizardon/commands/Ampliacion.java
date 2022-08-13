package es.allblue.lizardon.commands;


import es.allblue.lizardon.objects.requests.AmpliacionJSON;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Ampliacion extends CommandBase {
    @Override
    public String getName() {
        return "amp";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.amp.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            AmpliacionJSON amp = new AmpliacionJSON(args[0], args[1], args[2], args[3]);

            String res = RestApi.post("amp", amp);
        }
    }

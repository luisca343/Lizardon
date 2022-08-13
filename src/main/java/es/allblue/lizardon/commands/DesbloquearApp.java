package es.allblue.lizardon.commands;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.requests.DesbloquearAppJSON;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class DesbloquearApp extends CommandBase {
    @Override
    public String getName() {
        return "instalarapp";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.instalarapp.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            String user = args[0];
            String app = args[1];

            DesbloquearAppJSON desbloquearApp = new DesbloquearAppJSON(app, user, Lizardon.NOMBRE_MUNDO);
            String res = RestApi.post("instalarapp", desbloquearApp);
        }
    }

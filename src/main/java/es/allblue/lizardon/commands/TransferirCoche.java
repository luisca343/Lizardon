package es.allblue.lizardon.commands;

import es.allblue.lizardon.objects.requests.VehiculoJSON;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.UUID;

public class TransferirCoche extends CommandBase {
    @Override
    public String getName() {
        return "transferircoche";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.transferircoche.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack stack = player.getHeldItemMainhand();
            Item item = stack.getItem();
            if (item.getRegistryName().toString().equals("lizardon:coche") && stack.hasTagCompound()) {
                if (!player.world.isRemote) {
                    NBTTagCompound nbt = stack.serializeNBT();
                    NBTTagCompound tag = nbt.getCompoundTag("tag");
                    if(tag.hasKey("ownerUUID")){
                        String nombre = args[0];
                        String uuid = server.getPlayerList().getPlayerByUsername(nombre).getUniqueID().toString();
                        tag.setString("ownerUUID", uuid);
                        sender.sendMessage(new TextComponentString("Se ha transferido el vehículo a "+args[0]));
                    }else{
                        sender.sendMessage(new TextComponentString("No se han encontrado datos de vehículos."));
                    }
                }
            } else {
                sender.sendMessage(new TextComponentString("No se han encontrado datos de vehículos."));
            }
        } else {
            sender.sendMessage(new TextComponentString("Equipa la llave del coche en tu mano principal."));
        }
    }
}

//item.getItem().getRegistryName().toString().equals("lizardon:coche_mision") && !item.hasTagCompound()
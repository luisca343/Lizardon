package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecompensasMina implements Serializable {
    ArrayList<String> objetos;
    HashMap<String, Integer> recompensas;

    public RecompensasMina(ArrayList<String> recompensas) {
        this.objetos = recompensas;
    }

    public ArrayList<String> getObjetos() {
        return objetos;
    }

    public void setObjetos(ArrayList<String> objetos) {
        this.objetos = objetos;
    }


    public HashMap<String, Integer> getRecompensas() {
        return recompensas;
    }

    public void setRecompensas(HashMap<String, Integer> recompensas) {
        this.recompensas = recompensas;
    }

    public void ejecutar(EntityPlayerMP player) {
        recompensas = new HashMap<>();
        for (int i = 0; i < objetos.size(); i++) {
            String nombre = objetos.get(i);
            if (!recompensas.containsKey("")) {
                recompensas.put(nombre, 1);
            } else {
                recompensas.put(nombre, recompensas.get(nombre) + 1);
            }
            Lizardon.getLogger().info(nombre);
            //player.addItemStackToInventory(stack);
        }
        /*
        EntityPlayerMP player = Lizardon.server.getPlayerList().getPlayerByUsername(Minecraft.getMinecraft().player.getName());
        World world = player.getEntityWorld();*/


        //Lizardon.NET_HANDLER.sendToServer(new MsgRecompensasMina(this));


        World world = player.getEntityWorld();

        String mensaje = "§2§lObjetos obtenidos:§r§a\n";
        for (Map.Entry me : recompensas.entrySet()) {
            String nombre = (String) me.getKey();
            int cantidad = (int) me.getValue();

            mensaje += cantidad + " " + nombre + "\n";

            String[] partes = nombre.split(":");

            Item item = Item.REGISTRY.getObject(new ResourceLocation(partes[0], partes[1]));
            ItemStack stack = new ItemStack(item, 1);


            world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack));
        }

        player.sendMessage(new TextComponentString(mensaje));
    }


}

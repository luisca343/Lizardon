package es.allblue.lizardon.objects.requests;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DarObjeto {
    String objeto;
    int cantidad;

    public DarObjeto(String objeto, int cantidad) {
        this.objeto = objeto;
        this.cantidad = cantidad;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void ejecutar(EntityPlayerMP player) {
        World world = player.getEntityWorld();
        ItemStack stack;

        if (objeto.equals("llave_gacha")) {
            Item item = Item.REGISTRY.getObject(new ResourceLocation("pixelmon", "ultra_deep_sea_key"));
            stack = new ItemStack(item, cantidad);

            stack.setItemDamage(0);
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "§bLlave Gacha");
            nbt.setTag("display", display);
            nbt.setString("HCKEYUUID", "38927e62-ccfb-4705-9c69-38947a75ae93");
            nbt.setTag("ForgeCaps", new NBTTagCompound());
            nbt.setString("HCKEYID", "LOCALKEY_Gacha");
            stack.setTagCompound(nbt);

        } else if (objeto.equals("llave_gacha_shiny")) {

            Item item = Item.REGISTRY.getObject(new ResourceLocation("pixelmon", "ultra_forest_key"));
            stack = new ItemStack(item, cantidad);

            stack.setItemDamage(0);
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "§aLlave Gacha Shiny");
            nbt.setTag("display", display);
            nbt.setString("HCKEYUUID", "30e24a74-d990-4bc4-ac43-06548fbfaa8a");
            nbt.setTag("ForgeCaps", new NBTTagCompound());
            nbt.setString("HCKEYID", "LOCALKEY_ShinyGacha");
            stack.setTagCompound(nbt);

        } else if (objeto.equals("llave_poke_gacha")) {

            Item item = Item.REGISTRY.getObject(new ResourceLocation("pixelmon", "ultra_ruin_key"));
            stack = new ItemStack(item, cantidad);

            stack.setItemDamage(0);
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound display = new NBTTagCompound();
            display.setString("Name", "§cLlave Poke Gacha");
            nbt.setTag("display", display);
            nbt.setString("HCKEYUUID", "8261b079-b7dd-4c9f-9ad6-2023bc133f2a");
            nbt.setTag("ForgeCaps", new NBTTagCompound());
            nbt.setString("HCKEYID", "LOCALKEY_UltimateCrate");
            stack.setTagCompound(nbt);

        } else {
            String[] partes = objeto.split(":");
            Item item = Item.REGISTRY.getObject(new ResourceLocation(partes[0], partes[1]));
            stack = new ItemStack(item, cantidad);
        }


        world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack));
    }
}

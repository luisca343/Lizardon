package es.allblue.lizardon.items;

import es.allblue.lizardon.blocks.BloqueLizardon;
import es.allblue.lizardon.init.BlockInit;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ComidaLizardon extends Item {
    private String nombre;
    public ComidaLizardon(String nombre, int nutricion, int saturacion) {
        super(new Properties().tab(LizardonItemGroup.LIZARDON_GROUP).food(new Food.Builder()
                .nutrition(nutricion)
                .saturationMod(saturacion)
                .build()));
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

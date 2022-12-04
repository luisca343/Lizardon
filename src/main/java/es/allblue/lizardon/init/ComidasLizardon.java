package es.allblue.lizardon.init;

import es.allblue.lizardon.items.LizardonItemGroup;
import es.allblue.lizardon.objects.ObjColocable;
import net.minecraft.block.Block;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.ArrayList;

public class ComidasLizardon {
    static ArrayList<ObjColocable> objetos = new ArrayList<>();

    private static void initObjetos() {
        objetos.add(nuevaComida("caca_de_waifu", 2,2,true, Block.box(3, 0, 4, 12, 4.25, 13)));
    }

    public static ArrayList<ObjColocable> getComidas(){
        initObjetos();
        return objetos;
    }

    static ObjColocable nuevaComida(String nombre, int nutricion, int saturacion, boolean rapido, VoxelShape hitbox){
        return new ObjColocable(nombre, getPropertiesComida(nutricion, saturacion, rapido), hitbox, UseAction.EAT);
    }

    static ObjColocable nuevaBebida(String nombre, int nutricion, int saturacion, boolean rapido, VoxelShape hitbox){
        return new ObjColocable(nombre, getPropertiesComida(nutricion, saturacion, rapido), hitbox, UseAction.DRINK);
    }

    static Item.Properties getPropertiesComida(int nutricion, int saturacion, boolean rapido){
        Food.Builder builder = new Food.Builder()
                .nutrition(nutricion)
                .saturationMod(saturacion);
        if(rapido) builder.fast();
        return new Item.Properties().tab(LizardonItemGroup.LIZARDON_GROUP).food(builder.build());
    }
}

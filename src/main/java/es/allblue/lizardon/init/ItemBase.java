package es.allblue.lizardon.init;

import es.allblue.lizardon.items.LizardonItemGroup;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    public ItemBase(String nombre) {
        super(new Properties().tab(LizardonItemGroup.LIZARDON_GROUP));
    }
}

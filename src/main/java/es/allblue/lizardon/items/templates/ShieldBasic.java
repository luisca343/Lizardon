package es.allblue.lizardon.items.templates;

import es.allblue.lizardon.Lizardon;
import net.minecraft.item.ItemShield;

public class ShieldBasic extends ItemShield {

    public ShieldBasic(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Lizardon.CREATIVE_TAB);
    }
}

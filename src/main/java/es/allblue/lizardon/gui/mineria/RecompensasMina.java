package es.allblue.lizardon.gui.mineria;

import com.pixelmonmod.pixelmon.Pixelmon;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.init.ItemsInit;
import es.allblue.lizardon.items.templates.ItemBasic;
import es.allblue.lizardon.objects.mineria.RecompensaMina;
import es.allblue.lizardon.util.LootCollection;
import net.minecraft.item.Item;

import java.util.ArrayList;


public class RecompensasMina {
    static LootCollection<RecompensaMina> recompensas = ItemsInit.recompensas;

    public static RecompensaMina getRecompensa(){
        return recompensas.next();
    }
}

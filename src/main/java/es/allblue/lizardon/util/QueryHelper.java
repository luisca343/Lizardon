package es.allblue.lizardon.util;

import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IJSQueryCallback;

public class QueryHelper {

    public static boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        System.out.println(query);
        callback.success("Esto ha funcionado :3");
        return false;
    }

}

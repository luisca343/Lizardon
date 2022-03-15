/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package es.allblue.lizardon.smartrotom.net.client;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.smartrotom.net.Message;
import es.allblue.lizardon.smartrotom.utilities.NameUUIDPair;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@Message(messageId = 6, side = Side.CLIENT)
public class CMessageACResult implements IMessage, Runnable {

    private NameUUIDPair[] result;

    public CMessageACResult() {
    }

    public CMessageACResult(NameUUIDPair[] pairs) {
        result = pairs;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int cnt = buf.readByte();
        result = new NameUUIDPair[cnt];

        for(int i = 0; i < cnt; i++)
            result[i] = new NameUUIDPair(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(result.length);

        for(NameUUIDPair pair : result)
            pair.writeTo(buf);
    }

    @Override
    public void run() {
        Lizardon.getProxy().onAutocompleteResult(result);
    }

}

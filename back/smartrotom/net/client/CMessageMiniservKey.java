/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package es.allblue.lizardon.smartrotom.net.client;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.smartrotom.miniserv.client.Client;
import es.allblue.lizardon.smartrotom.net.Message;
import es.allblue.lizardon.smartrotom.utilities.Log;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@Message(messageId = 11, side = Side.CLIENT)
public class CMessageMiniservKey implements IMessage, Runnable {

    private byte[] encryptedKey;

    public CMessageMiniservKey() {
    }

    public CMessageMiniservKey(byte[] key) {
        encryptedKey = key;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        encryptedKey = new byte[buf.readShort() & 0xFFFF];
        buf.readBytes(encryptedKey);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(encryptedKey.length);
        buf.writeBytes(encryptedKey);
    }

    @Override
    public void run() {
        if(Client.getInstance().decryptKey(encryptedKey)) {
            Log.info("Successfully received and decrypted key, starting miniserv client...");
            Lizardon.getProxy().startMiniservClient();
        }
    }

}

/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package es.allblue.lizardon.smartrotom.miniserv.client;
import es.allblue.lizardon.smartrotom.miniserv.OutgoingPacket;
import es.allblue.lizardon.smartrotom.miniserv.PacketID;

public class ClientTaskGetQuota extends ClientTask<ClientTaskGetQuota> {

    private long quota;
    private long maxQuota;

    public ClientTaskGetQuota() {
        runCallbackOnMcThread = true;
    }

    @Override
    public void start() {
        OutgoingPacket pkt = new OutgoingPacket();
        pkt.writeByte(PacketID.QUOTA.ordinal());
        client.sendPacket(pkt);
    }

    @Override
    public void abort() {
    }

    public void onQuotaData(long q, long m) {
        quota = q;
        maxQuota = m;
        client.nextTask();
    }

    public long getMaxQuota() {
        return maxQuota;
    }

    public long getQuota() {
        return quota;
    }

}

package es.allblue.lizardon.net;

import es.allblue.lizardon.net.server.SMessagePadCtrl;
import es.allblue.lizardon.net.server.SMessageTest;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber
public class Messages {

    private static final String PROTOCOL_VERSION = "1";
    private static int index = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("lizardon", "packetsystem"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void registryNetworkPackets (FMLCommonSetupEvent event) {
        INSTANCE.registerMessage(index++, SMessagePadCtrl.class, SMessagePadCtrl::encode, SMessagePadCtrl::decode, SMessagePadCtrl::handle);
        INSTANCE.registerMessage(index++, SMessageTest.class, SMessageTest::encode, SMessageTest::decode, SMessageTest::handle);
        /*
        INSTANCE.registerMessage(index++, CMessageACResult.class, CMessageACResult::encode, CMessageACResult::decode, CMessageACResult::handle);
        INSTANCE.registerMessage(index++, CMessageAddScreen.class, CMessageAddScreen::encode, CMessageAddScreen::decode, CMessageAddScreen::handle);
        INSTANCE.registerMessage(index++, CMessageCloseGui.class, CMessageCloseGui::encode, CMessageCloseGui::decode, CMessageCloseGui::handle);
        INSTANCE.registerMessage(index++, CMessageJSResponse.class, CMessageJSResponse::encode, CMessageJSResponse::decode, CMessageJSResponse::handle);
        INSTANCE.registerMessage(index++, CMessageMiniservKey.class, CMessageMiniservKey::encode, CMessageMiniservKey::decode, CMessageMiniservKey::handle);
        INSTANCE.registerMessage(index++, CMessageScreenUpdate.class, CMessageScreenUpdate::encode, CMessageScreenUpdate::decode, CMessageScreenUpdate::handle);
        INSTANCE.registerMessage(index++, CMessageServerInfo.class, CMessageServerInfo::encode, CMessageServerInfo::decode, CMessageServerInfo::handle);
        INSTANCE.registerMessage(index++, SMessageACQuery.class, SMessageACQuery::encode, SMessageACQuery::decode, SMessageACQuery::handle);
        INSTANCE.registerMessage(index++, SMessageMiniservConnect.class, SMessageMiniservConnect::encode, SMessageMiniservConnect::decode, SMessageMiniservConnect::handle);
        INSTANCE.registerMessage(index++, SMessageRedstoneCtrl.class, SMessageRedstoneCtrl::encode, SMessageRedstoneCtrl::decode, SMessageRedstoneCtrl::handle);
        INSTANCE.registerMessage(index++, SMessageRequestTEData.class, SMessageRequestTEData::encode, SMessageRequestTEData::decode, SMessageRequestTEData::handle);
        INSTANCE.registerMessage(index++, SMessageScreenCtrl.class, SMessageScreenCtrl::encode, SMessageScreenCtrl::decode, SMessageScreenCtrl::handle);
        INSTANCE.registerMessage(index++, CMessageOpenGui.class, CMessageOpenGui::encode, CMessageOpenGui::decode, CMessageOpenGui::handle);
    */
    }
}

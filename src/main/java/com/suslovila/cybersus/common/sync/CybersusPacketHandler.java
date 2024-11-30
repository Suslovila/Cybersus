package com.suslovila.cybersus.common.sync;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.sync.implant.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class CybersusPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Cybersus.NAME.toLowerCase());

    public static void init() {
        int idx = 12;
        INSTANCE.registerMessage(PacketImplantSync.Handler.class, PacketImplantSync.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketAllExtendedPlayerSync.Handler.class, PacketAllExtendedPlayerSync.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketOneExtendedPlayerSync.Handler.class, PacketOneExtendedPlayerSync.class, idx++, Side.CLIENT);

        INSTANCE.registerMessage(PacketEnableImplantSync.Handler.class, PacketEnableImplantSync.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketItemPortableContainer.Handler.class, PacketItemPortableContainer.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketOpenImplantGui.Handler.class, PacketOpenImplantGui.class, idx++, Side.SERVER);

        INSTANCE.registerMessage(PacketRuneInstallerButtonClicked.Handler.class, PacketRuneInstallerButtonClicked.class, idx++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncProcess.Handler.class, PacketSyncProcess.class, idx++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncAllProcess.Handler.class, PacketSyncAllProcess.class, idx++, Side.CLIENT);

    }

}

package ink.magma.riaserverapi;

import ink.magma.riaserverapi.api.ApiServer;
import ink.magma.riaserverapi.platform.PlatformState;
import net.md_5.bungee.api.plugin.Plugin;

public final class RIAServerAPI_Bungee extends Plugin {
    public static RIAServerAPI_Bungee instance;

    @Override
    public void onEnable() {
        PlatformState.setPlatform(PlatformState.Platform.BUNGEE);
        instance = this;

        ApiServer.init();
    }
}

package ink.magma.riasminecraftbridge;

import ink.magma.riasminecraftbridge.platform.PlatformState;
import ink.magma.riasminecraftbridge.socket.bridge.APIServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class RIAMinecraftBridge_Bungee extends Plugin {
    public static RIAMinecraftBridge_Bungee instance;
    private APIServer apiServer;

    @Override
    public void onEnable() {
        PlatformState.setPlatform(PlatformState.Platform.BUNGEE);
        instance = this;

        apiServer = new APIServer();
    }

    @Override
    public void onDisable() {
        apiServer.stop();
    }
}

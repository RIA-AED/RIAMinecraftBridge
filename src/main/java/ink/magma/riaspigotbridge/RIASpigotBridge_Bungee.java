package ink.magma.riaspigotbridge;

import ink.magma.riaspigotbridge.platform.PlatformState;
import ink.magma.riaspigotbridge.socket.bridge.ClientMain;
import net.md_5.bungee.api.plugin.Plugin;

public final class RIASpigotBridge_Bungee extends Plugin {
    public static RIASpigotBridge_Bungee instance;

    @Override
    public void onEnable() {
        PlatformState.setPlatform(PlatformState.Platform.BUNGEE);
        instance = this;

        ClientMain.init();
    }

    @Override
    public void onDisable() {
        ClientMain.stop();
    }
}

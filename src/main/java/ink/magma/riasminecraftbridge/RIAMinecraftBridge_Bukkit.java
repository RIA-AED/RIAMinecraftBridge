package ink.magma.riasminecraftbridge;

import ink.magma.riasminecraftbridge.platform.PlatformState;
import ink.magma.riasminecraftbridge.socket.bridge.ClientMain;
import org.bukkit.plugin.java.JavaPlugin;

public final class RIAMinecraftBridge_Bukkit extends JavaPlugin {
    public static RIAMinecraftBridge_Bukkit instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PlatformState.setPlatform(PlatformState.Platform.BUKKIT);
        instance = this;

        ClientMain.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ClientMain.stop();
    }
}

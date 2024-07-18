package ink.magma.riaspigotbridge;

import ink.magma.riaspigotbridge.platform.PlatformState;
import ink.magma.riaspigotbridge.socket.bridge.ClientMain;
import org.bukkit.plugin.java.JavaPlugin;

public final class RIASpigotBridge_Bukkit extends JavaPlugin {
    public static RIASpigotBridge_Bukkit instance;

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

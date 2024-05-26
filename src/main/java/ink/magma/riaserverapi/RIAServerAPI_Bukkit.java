package ink.magma.riaserverapi;

import ink.magma.riaserverapi.api.ApiServer;
import ink.magma.riaserverapi.platform.PlatformState;
import org.bukkit.plugin.java.JavaPlugin;

public final class RIAServerAPI_Bukkit extends JavaPlugin {
    public static RIAServerAPI_Bukkit instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PlatformState.setPlatform(PlatformState.Platform.BUKKIT);
        instance = this;

        ApiServer.init();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

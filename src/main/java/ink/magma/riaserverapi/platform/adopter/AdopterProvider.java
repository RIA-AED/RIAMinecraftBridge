package ink.magma.riaserverapi.platform.adopter;

import ink.magma.riaserverapi.platform.PlatformState;

public class AdopterProvider {
    private static PlatformAdopter instance = null;

    public static PlatformAdopter get() {
        if (instance == null) {
            if (PlatformState.getPlatform().equals(PlatformState.Platform.BUKKIT)) {
                instance = new BukkitAdopter();
            }
            if (PlatformState.getPlatform().equals(PlatformState.Platform.BUNGEE)) {
                instance = new BungeeAdopter();
            }
        }
        return instance;
    }
}

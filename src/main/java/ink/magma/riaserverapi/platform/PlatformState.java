package ink.magma.riaserverapi.platform;

public class PlatformState {
    static private Platform platform;

    public static void setPlatform(Platform platform) {
        PlatformState.platform = platform;
    }

    public static Platform getPlatform() {
        return platform;
    }

    public enum Platform {
        BUKKIT,
        BUNGEE,
    }
}

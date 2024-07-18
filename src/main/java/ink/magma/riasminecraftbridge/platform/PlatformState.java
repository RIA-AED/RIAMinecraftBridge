package ink.magma.riasminecraftbridge.platform;

public class PlatformState {
    static private Platform platform;

    public static Platform getPlatform() {
        return platform;
    }

    public static void setPlatform(Platform platform) {
        PlatformState.platform = platform;
    }

    public enum Platform {
        BUKKIT,
        BUNGEE,
    }
}

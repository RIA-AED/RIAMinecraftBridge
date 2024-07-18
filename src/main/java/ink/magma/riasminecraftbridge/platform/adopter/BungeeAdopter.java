package ink.magma.riasminecraftbridge.platform.adopter;

import ink.magma.riasminecraftbridge.RIAMinecraftBridge_Bungee;
import ink.magma.riasminecraftbridge.record.PlayerList;
import ink.magma.riasminecraftbridge.record.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BungeeAdopter implements PlatformAdopter {
    private final File latestLog;

    public BungeeAdopter() {
        File serverFolder = RIAMinecraftBridge_Bungee.instance.getProxy().getPluginsFolder().getParentFile();
        File logsFolder = new File(serverFolder, "logs");
        latestLog = new File(logsFolder, "latest.log");
    }

    @Override
    public File getPluginFolder() {
        return RIAMinecraftBridge_Bungee.instance.getDataFolder();
    }

    @Override
    public List<String> getConsoleLogs() {
        try {
            return Files.readAllLines(latestLog.toPath());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void dispatchCommand(String command) {
        RIAMinecraftBridge_Bungee.instance.getProxy().getPluginManager().dispatchCommand(RIAMinecraftBridge_Bungee.instance.getProxy().getConsole(), command);
    }

    @Override
    @Deprecated
    public UUID dispatchCommandByWrapper(String command) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    @Deprecated
    public List<String> getWrapperConsoleLogs(UUID uuid) {
        return null;
    }

    @Override
    public PlayerList getPlayerList() {
        PlayerList playerList = new PlayerList(new ArrayList<>());

        RIAMinecraftBridge_Bungee.instance.getProxy().getServers().forEach((serverName, serverInfo) -> {
            ArrayList<PlayerList.Player> playerInfoList = new ArrayList<>();

            serverInfo.getPlayers().forEach(player -> {
                playerInfoList.add(new PlayerList.Player(player.getName(), player.getUniqueId().toString()));
            });

            playerList.servers().add(new PlayerList.Server(serverName, playerInfoList));
        });

        return playerList;
    }

    @Override
    public void logInfo(String message) {
        RIAMinecraftBridge_Bungee.instance.getLogger().info(message);
    }

    @Override
    public void logWarn(String message) {
        RIAMinecraftBridge_Bungee.instance.getLogger().warning(message);
    }

    @Override
    public void logError(String message) {
        RIAMinecraftBridge_Bungee.instance.getLogger().severe(message);
    }

    @Override
    public Collection<Plugin> getPlugins() {
        Collection<net.md_5.bungee.api.plugin.Plugin> platformPlugins = RIAMinecraftBridge_Bungee.instance.getProxy().getPluginManager().getPlugins();
        if (platformPlugins != null) {
            return platformPlugins.stream()
                    .map(plugin -> new Plugin(
                            plugin.getDescription().getName(),
                            plugin.getDescription().getVersion(),
                            Collections.singleton(plugin.getDescription().getAuthor()),
                            plugin.getDescription().getDescription()
                    ))
                    .toList();
        }

        return List.of();
    }
}

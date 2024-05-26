package ink.magma.riaserverapi.platform.adopter;

import ink.magma.riaserverapi.RIAServerAPI_Bungee;
import ink.magma.riaserverapi.platform.player.list.PlayerList;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeeAdopter implements PlatformAdopter {
    @Override
    public File getPluginFolder() {
        return RIAServerAPI_Bungee.instance.getDataFolder();
    }

    private final File latestLog;

    public BungeeAdopter() {
        File serverFolder = RIAServerAPI_Bungee.instance.getProxy().getPluginsFolder().getParentFile();
        File logsFolder = new File(serverFolder, "logs");
        latestLog = new File(logsFolder, "latest.log");
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
        RIAServerAPI_Bungee.instance.getProxy().getPluginManager().dispatchCommand(RIAServerAPI_Bungee.instance.getProxy().getConsole(), command);
    }

    @Override
    public UUID dispatchCommandByWrapper(String command) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public List<String> getWrapperConsoleLogs(UUID uuid) {
        return null;
    }

    @Override
    public PlayerList getPlayerList() {
        PlayerList playerList = new PlayerList(new ArrayList<>());

        RIAServerAPI_Bungee.instance.getProxy().getServers().forEach((serverName, serverInfo) -> {
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
        RIAServerAPI_Bungee.instance.getLogger().info(message);
    }

    @Override
    public void logWarn(String message) {
        RIAServerAPI_Bungee.instance.getLogger().warning(message);
    }

    @Override
    public void logError(String message) {
        RIAServerAPI_Bungee.instance.getLogger().severe(message);
    }
}

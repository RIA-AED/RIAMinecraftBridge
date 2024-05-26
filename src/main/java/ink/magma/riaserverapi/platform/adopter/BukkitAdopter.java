package ink.magma.riaserverapi.platform.adopter;

import ink.magma.riaserverapi.RIAServerAPI_Bukkit;
import ink.magma.riaserverapi.platform.console.sender.fake.BukkitFakeCommandSender;
import ink.magma.riaserverapi.platform.player.list.PlayerList;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BukkitAdopter implements PlatformAdopter {
    private final File latestLog;
    private final Map<UUID, BukkitFakeCommandSender> consoleCommandSenderMap = new HashMap<>();

    @Override
    public File getPluginFolder() {
        return RIAServerAPI_Bukkit.instance.getDataFolder();
    }

    public BukkitAdopter() {
        File serverFolder = RIAServerAPI_Bukkit.instance.getServer().getWorldContainer().getParentFile();
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

    public UUID dispatchCommandByWrapper(String command) {
        BukkitFakeCommandSender console = new BukkitFakeCommandSender(Bukkit.getConsoleSender());

        // 为假控制台生成一个唯一标识，并将其保存
        UUID uuid = UUID.randomUUID();
        consoleCommandSenderMap.put(uuid, console);

        // 异步执行命令
        Bukkit.getScheduler().runTask(RIAServerAPI_Bukkit.instance, () -> {
            Bukkit.dispatchCommand(console, command);
        });

        return uuid;
    }

    public void dispatchCommand(String command) {
        Bukkit.getScheduler().runTask(RIAServerAPI_Bukkit.instance, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    @Override
    public @Nullable List<String> getWrapperConsoleLogs(UUID uuid) {
        var sender = consoleCommandSenderMap.get(uuid);
        if (sender != null) return sender.getLogs();
        return null;
    }

    @Override
    public PlayerList getPlayerList() {
        PlayerList.Server server = new PlayerList.Server(null, new ArrayList<>());

        RIAServerAPI_Bukkit.instance.getServer().getOnlinePlayers().forEach(player -> {
            PlayerList.Player playerRecord = new PlayerList.Player(player.getUniqueId().toString(), player.getName());
            server.players().add(playerRecord);
        });

        return new PlayerList(List.of(server));
    }

    @Override
    public void logInfo(String message) {
        RIAServerAPI_Bukkit.instance.getLogger().info(message);
    }

    @Override
    public void logError(String message) {
        RIAServerAPI_Bukkit.instance.getLogger().severe(message);
    }

    @Override
    public void logWarn(String message) {
        RIAServerAPI_Bukkit.instance.getLogger().warning(message);
    }
}

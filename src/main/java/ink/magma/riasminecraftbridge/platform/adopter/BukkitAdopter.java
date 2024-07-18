package ink.magma.riasminecraftbridge.platform.adopter;

import ink.magma.riasminecraftbridge.RIAMinecraftBridge_Bukkit;
import ink.magma.riasminecraftbridge.platform.adopter.console.sender.fake.BukkitFakeCommandSender;
import ink.magma.riasminecraftbridge.record.PlayerList;
import ink.magma.riasminecraftbridge.record.Plugin;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BukkitAdopter implements PlatformAdopter {
    private final File latestLog;
    private final Map<UUID, BukkitFakeCommandSender> consoleCommandSenderMap = new HashMap<>();

    public BukkitAdopter() {
        File serverFolder = RIAMinecraftBridge_Bukkit.instance.getServer().getWorldContainer().getParentFile();
        File logsFolder = new File(serverFolder, "logs");
        latestLog = new File(logsFolder, "latest.log");
    }

    @Override
    public File getPluginFolder() {
        return RIAMinecraftBridge_Bukkit.instance.getDataFolder();
    }

    @Override
    @Deprecated
    public List<String> getConsoleLogs() {
        try {
            return Files.readAllLines(latestLog.toPath());
        } catch (IOException e) {
            return null;
        }
    }

    @Deprecated
    public UUID dispatchCommandByWrapper(String command) {
        BukkitFakeCommandSender console = new BukkitFakeCommandSender(Bukkit.getConsoleSender());

        // 为假控制台生成一个唯一标识，并将其保存
        UUID uuid = UUID.randomUUID();
        consoleCommandSenderMap.put(uuid, console);

        // 异步执行命令
        Bukkit.getScheduler().runTask(RIAMinecraftBridge_Bukkit.instance, () -> {
            Bukkit.dispatchCommand(console, command);
        });

        return uuid;
    }

    public void dispatchCommand(String command) {
        Bukkit.getScheduler().runTask(RIAMinecraftBridge_Bukkit.instance, () -> {
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

        RIAMinecraftBridge_Bukkit.instance.getServer().getOnlinePlayers().forEach(player -> {
            PlayerList.Player playerRecord = new PlayerList.Player(player.getUniqueId().toString(), player.getName());
            server.players().add(playerRecord);
        });

        return new PlayerList(List.of(server));
    }

    @Override
    public void logInfo(String message) {
        RIAMinecraftBridge_Bukkit.instance.getLogger().info(message);
    }

    @Override
    public void logError(String message) {
        RIAMinecraftBridge_Bukkit.instance.getLogger().severe(message);
    }

    @Override
    public void logWarn(String message) {
        RIAMinecraftBridge_Bukkit.instance.getLogger().warning(message);
    }

    @Override
    public Collection<Plugin> getPlugins() {
        org.bukkit.plugin.Plugin[] platformPlugins = RIAMinecraftBridge_Bukkit.instance.getServer().getPluginManager().getPlugins();
        if (platformPlugins != null) {
            return Arrays.stream(platformPlugins)
                    .map(plugin -> new Plugin(
                            plugin.getName(),
                            plugin.getDescription().getVersion(),
                            plugin.getDescription().getAuthors(),
                            plugin.getDescription().getDescription()
                    ))
                    .toList();
        }
        return List.of();
    }
}

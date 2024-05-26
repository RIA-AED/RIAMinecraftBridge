package ink.magma.riaserverapi.platform.console.sender.fake;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

public class BukkitFakeCommandSender implements CommandSender {
    private final CommandSender sender;
    private final List<String> logs;
    private final Logger logger;

    public BukkitFakeCommandSender(CommandSender sender) {
        this.sender = sender;
        this.logs = new ArrayList<>();
        this.logger = Bukkit.getLogger();
    }

    public List<String> getLogs() {
        List<String> result = new ArrayList<>();

        logs.forEach(log -> {
            result.addAll(Arrays.asList(log.split("\n")));
        });

        return result;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        logs.add(ChatColor.stripColor(message));
        logger.info("[PluginConsole] " + message);
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            logs.add(ChatColor.stripColor(message));
            logger.info("[PluginConsole] " + message);
        }
        sender.sendMessage(messages);
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {
        logs.add(ChatColor.stripColor(message));
        logger.info("[PluginConsole] " + message);
        this.sender.sendMessage(sender, message);
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
        for (String message : messages) {
            logs.add(ChatColor.stripColor(message));
            logger.info("[PluginConsole] " + message);
        }
        this.sender.sendMessage(sender, messages);
    }


    @Override
    public @NotNull Server getServer() {
        return sender.getServer();
    }

    @Override
    public @NotNull String getName() {
        return sender.getName();
    }

    @NotNull
    @Override
    public Spigot spigot() {
        return sender.spigot();
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return sender.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return sender.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        return sender.hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return sender.hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return sender.addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return sender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        return sender.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return sender.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        sender.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        sender.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return sender.isOp();
    }

    @Override
    public void setOp(boolean value) {
        sender.setOp(value);
    }
}

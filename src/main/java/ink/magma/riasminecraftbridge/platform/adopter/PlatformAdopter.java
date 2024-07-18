package ink.magma.riasminecraftbridge.platform.adopter;

import ink.magma.riasminecraftbridge.record.PlayerList;
import ink.magma.riasminecraftbridge.record.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PlatformAdopter {
    /**
     * 获取插件的存放配置用文件夹
     */
    File getPluginFolder();

    /**
     * 获取服务端的控制台日志
     */
    List<String> getConsoleLogs();

    /**
     * 执行由虚拟发送者发送的控制台命令。
     * 相对于 dispatchCommand 方法，此方法执行时的 sender 被代理，可截获对此代理 sender 发送的相关消息。
     * 但本方法有兼容问题，仅支持部分规范的 Bukkit 插件。
     *
     * @param command 命令字符串，首个 "/" 需要省略
     * @throws UnsupportedOperationException 当平台不支持此功能时掷出
     */
    @Deprecated
    UUID dispatchCommandByWrapper(String command) throws UnsupportedOperationException;


    /**
     * 执行控制台命令
     *
     * @param command 命令字符串，首个 "/" 需要省略
     */
    void dispatchCommand(String command);

    /**
     * 获取 dispatchCommandByWrapper 方法执行后的控制台日志
     *
     * @param uuid 执行命令后返回的 UUID
     */
    @Nullable
    @Deprecated
    List<String> getWrapperConsoleLogs(UUID uuid);

    /**
     * 获取平台的玩家列表
     *
     * @return PlayerList 是插件描述玩家列表的数据类
     */
    PlayerList getPlayerList();

    /**
     * 输出日志
     *
     * @param message 日志
     */
    void logInfo(String message);

    /**
     * 输出警告日志
     *
     * @param message 日志
     */
    void logWarn(String message);

    /**
     * 输出错误日志
     *
     * @param message 日志
     */
    void logError(String message);

    /**
     * 获取平台的插件列表
     */
    Collection<Plugin> getPlugins();
}

package ink.magma.riasminecraftbridge.socket.bridge.module;

import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 本模块实现了在控制台执行命令、获取控制台日志。
 */
public class CommandConsole implements Module {
    private final PlatformAdopter platform;

    public CommandConsole() {
        platform = AdopterProvider.get();
    }

    public void dispatchConsoleCommand(String command) {
        // 控制台执行命令
        platform.dispatchCommand(command);
        platform.logInfo(MessageFormat.format(
                "Hub 通过 API 执行了控制台命令: {0}", command
        ));
    }

    public List<String> getConsoleLogs() {
        List<String> messages = new ArrayList<>();

        // 如果LogSaver可用，则返回日志消息，否则返回空列表
        if (platform == null) {
            messages.add("获取日志失败! LogReader 为 null");
            return messages;
        }

        messages = platform.getConsoleLogs();
        if (messages == null) {
            messages = new ArrayList<>();
            messages.add("获取日志失败! latest.log 无法读取");
        }
        return messages;
    }
}

package ink.magma.riaspigotbridge.socket.bridge.module;

import ink.magma.riaspigotbridge.platform.adopter.AdopterProvider;
import ink.magma.riaspigotbridge.platform.adopter.PlatformAdopter;
import ink.magma.riaspigotbridge.socket.bridge.ClientMain;
import ink.magma.riaspigotbridge.socket.bridge.module.define.Module;
import io.socket.client.Socket;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 本模块实现了在执行控制台命令，并可能获取执行结果。
 */
public class CommandConsole implements Module {
    private final PlatformAdopter platform;

    public CommandConsole() {
        platform = AdopterProvider.get();
        Socket socket = ClientMain.getSocket();

        socket.on("dispatchCommandEvent", (args) -> {
                    // 从请求体中获取命令和类型
                    String command = (String) args[0];
                    // 检查命令是否为空，如果为空则返回错误信息
                    if (command == null) return;

                    // 控制台执行命令
                    platform.dispatchCommand(command);
                    platform.logInfo(MessageFormat.format(
                            "Hub 通过 API 执行了控制台命令: {0}", command
                    ));
                })
                .on("getConsoleLogEvent", (args) -> {
                    socket.emit("getConsoleLogEvent", getMessage());
                });
    }

    private List<String> getMessage() {
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

    @Override
    public void disable() {

    }
}

package ink.magma.riaserverapi.api.module;

import com.google.gson.Gson;
import ink.magma.riaserverapi.api.route.response.MessageOnlyResponse;
import ink.magma.riaserverapi.api.transformer.JsonTransformer;
import ink.magma.riaserverapi.platform.adopter.AdopterProvider;
import ink.magma.riaserverapi.platform.adopter.PlatformAdopter;
import org.jetbrains.annotations.NotNull;
import spark.Route;
import spark.RouteGroup;
import spark.Spark;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 本模块实现了在 API 执行控制台命令，并可能获取执行结果。
 * 由于 Spigot 的兼容性问题，我们无法继承 Bukkit 的 CommandSender 类并实现原版指令的兼容。
 * <p>
 * Paper 为此类需求提供了支持，详见 <a href="https://github.com/PaperMC/Paper/blob/master/patches/server/0674-API-for-creating-command-sender-which-forwards-feedb.patch">Paper Patch 地址</a>。
 * 由于 FST 计划进行 1.20.1 更新，但未确定是否使用支持 Paper API 的服务器，因此我们暂时不支持 Paper API。
 */
public class CommandConsole implements Module {
    private final PlatformAdopter platform;

    public CommandConsole() {
        platform = AdopterProvider.get();
    }

    @Override
    public String name() {
        return "console";
    }

    @Override
    public RouteGroup routes() {
        return () -> {
            Spark.post("/dispatch", dispatchRoute(), new JsonTransformer());
            Spark.get("/logs", logsRoute(), new JsonTransformer());
        };
    }

    /**
     * 执行控制台命令的路由
     */
    private @NotNull Route dispatchRoute() {
        return (req, res) -> {
            // 将请求体解析为Map格式
            Map body = new Gson().fromJson(req.body(), Map.class);

            // 从请求体中获取命令和类型
            String command = (String) body.get("command");
            String type = (String) body.get("type");

            // 检查命令和类型是否为空，如果为空则返回错误信息
            if (command == null || type == null) {
                res.status(400);
                return new MessageOnlyResponse("参数不足");
            }

            // 使用假控制台模式执行命令
            if (type.equals(ConsoleExecuteType.CONSOLE_WRAPPER)) {
                try {
                    UUID uuid = platform.dispatchCommandByWrapper(command);
                    // 返回执行的控制台的 UUID
                    return Map.of("dispatched", true, "senderUUID", uuid.toString());
                } catch (UnsupportedOperationException e) {
                    return new MessageOnlyResponse("当前服务端不支持此操作");
                }
            }

            // 使用真实控制台模式执行命令，不返回执行结果
            if (type.equals(ConsoleExecuteType.CONSOLE)) {
                platform.dispatchCommand(command);
                return Map.of("dispatched", true);
            }

            // 如果类型不匹配，则返回错误信息
            res.status(400);
            return new MessageOnlyResponse("请求头 type 类型错误，请参考文档。命令未执行。");
        };
    }

    /**
     * 处理日志请求的路由。
     */
    private @NotNull Route logsRoute() {
        return (req, res) -> {
            // 参数处理
            String consoleUUID = req.queryParams("uuid");
            String type = req.queryParams("type");
            int limit = 100;

            try {
                if (req.queryParams("limit") != null) {
                    limit = Integer.parseInt(req.queryParams("limit"));
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return new MessageOnlyResponse("limit 参数必须为数字");
            }

            if (type == null ||
                type.equals(ConsoleExecuteType.CONSOLE_WRAPPER) && consoleUUID == null) {
                res.status(400);
                return new MessageOnlyResponse("参数错误");
            }


            // 处理假控制台模式的日志请求
            if (type.equals(ConsoleExecuteType.CONSOLE_WRAPPER)) {
                // 检查consoleUUID是否存在于map中
                List<String> logs = platform.getWrapperConsoleLogs(UUID.fromString(consoleUUID));
                // 如果日志不为空，则返回日志消息
                if (logs != null) return logs;

                // 未找到指定的consoleUUID
                res.status(404);
                return new MessageOnlyResponse("未找到该 consoleUUID");
            }

            // 处理真实控制台模式的日志请求
            if (type.equals(ConsoleExecuteType.CONSOLE)) {
                // 如果LogSaver可用，则返回日志消息，否则返回空列表
                if (platform == null) {
                    res.status(500);
                    return new MessageOnlyResponse("获取日志失败! LogReader 为 null");
                }

                List<String> messages = platform.getConsoleLogs();
                if (messages == null) {
                    res.status(500);
                    return new MessageOnlyResponse("获取日志失败! latest.log 无法读取");
                }

                // 截取最新的 limit 条日志
                if (limit >= messages.size()) return messages;
                return messages.subList(messages.size() - limit, messages.size());
            }

            // 当type参数错误时，返回错误信息
            res.status(400);
            return new MessageOnlyResponse("请求头 type 类型错误，请参考文档。");
        };
    }

    private static class ConsoleExecuteType {
        public static final String CONSOLE = "console";
        public static final String CONSOLE_WRAPPER = "console-wrapper";
    }
}

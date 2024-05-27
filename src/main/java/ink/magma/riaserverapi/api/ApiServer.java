package ink.magma.riaserverapi.api;

import ink.magma.riaserverapi.api.module.Module;
import ink.magma.riaserverapi.api.module.*;
import ink.magma.riaserverapi.api.route.NotSupportRoute;
import ink.magma.riaserverapi.api.transformer.JsonTransformer;
import ink.magma.riaserverapi.platform.PlatformState;
import ink.magma.riaserverapi.platform.adopter.AdopterProvider;
import ink.magma.riaserverapi.platform.setting.SettingDeployer;
import spark.Spark;

import java.util.ArrayList;
import java.util.Map;

public class ApiServer {

    public static void init() {
        int port = SettingDeployer.get().httpServer.port();
        Spark.port(port);

        AdopterProvider.get().logInfo("服务已在端口 " + port + " 上启动.");

        // 模块
        ArrayList<Module> modules = new ArrayList<>();

        // 由于 Proxy 没有 TPS 和 MSPT 的概念，因此直接不支持
        if (PlatformState.getPlatform().equals(PlatformState.Platform.BUKKIT)) {
            modules.add(new MCSpark());
        }

        modules.add(new CommandConsole());
        modules.add(new Player());
        modules.add(new PluginList());

        // 默认全局使用 JSON
        Spark.before((req, res) -> res.type("application/json"));
        Spark.get("/ping", (req, res) -> Map.of("message", "Pong!"), new JsonTransformer());

        // 404
        Spark.notFound(NotSupportRoute.ErrorResponse);
        Spark.internalServerError(new JsonTransformer().render(
                Map.of("message", "服务端发生意外错误。可能的解决方法：1. 参数是否符合业务；2. 联系服务端运维解决。")
        ));

        // 注册模块
        modules.forEach(Module::register);

        AdopterProvider.get().logInfo("模块已全部注册完毕.");
    }

    public static void stop() {
        Spark.stop();
    }
}

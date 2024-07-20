package ink.magma.riasminecraftbridge.socket.bridge;

import ink.magma.riasminecraftbridge.platform.PlatformState;
import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.platform.setting.SettingDeployer;
import ink.magma.riasminecraftbridge.socket.bridge.module.CommandConsole;
import ink.magma.riasminecraftbridge.socket.bridge.module.MCSpark;
import ink.magma.riasminecraftbridge.socket.bridge.module.Player;
import ink.magma.riasminecraftbridge.socket.bridge.module.PluginList;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;
import io.javalin.Javalin;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

public class APIServer {
    private static APIServer instance;
    private final Set<Module> modules;
    private final Javalin app;
    private final PlatformAdopter platform;

    public APIServer() {
        instance = this;
        platform = AdopterProvider.get();
        /* 模块启动逻辑 */
        platform.logInfo("正在注册模块...");
        modules = new HashSet<>();
        if (PlatformState.getPlatform().equals(PlatformState.Platform.BUKKIT)) {
            // 由于 Proxy 没有 TPS 和 MSPT 的概念，因此直接不支持
            modules.add(new MCSpark());
        }
        modules.add(new CommandConsole());
        modules.add(new Player());
        modules.add(new PluginList());
        platform.logInfo("模块已全部注册完毕.");


        /* HTTP API 启动逻辑 */
        Integer port = SettingDeployer.get().apiSettings.port();
        app = Javalin.create(config -> {
                    config.router.apiBuilder(new APIRouter().getRoutes());
                })
                .events(event -> {
                    event.serverStarted(() -> {
                        platform.logInfo("API 服务器已在 " + port + " 端口上启动.");
                    });
                    event.serverStopped(() -> {
                        platform.logInfo("API 服务器关闭成功.");
                    });
                })
                .after(ctx -> {
                    // HTTP 请求日志
                    platform.logInfo(MessageFormat.format(
                            "{0} [{1}] [{2}] {3}",
                            ctx.ip(), // IP
                            ctx.status(), // 状态码
                            ctx.method(), // 请求方法
                            ctx.url() // 请求路径
                    ));
                })
                .start(port);
    }

    public void stop() {
        modules.forEach(Module::disable);
        app.stop();
        instance = null;
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        for (Module module : modules) {
            if (clazz.isInstance(module)) {
                return clazz.cast(module);
            }
        }
        return null;
    }

    public static APIServer getInstance() {
        return instance;
    }
}

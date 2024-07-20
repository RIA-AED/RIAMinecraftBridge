package ink.magma.riasminecraftbridge.socket.bridge;

import ink.magma.riasminecraftbridge.socket.bridge.module.CommandConsole;
import ink.magma.riasminecraftbridge.socket.bridge.module.MCSpark;
import ink.magma.riasminecraftbridge.socket.bridge.module.Player;
import ink.magma.riasminecraftbridge.socket.bridge.module.PluginList;
import io.javalin.apibuilder.EndpointGroup;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;


public class APIRouter {

    public static final APIServer API_SERVER = APIServer.getInstance();

    public EndpointGroup getRoutes() {
        return () -> {
            path("spark", () -> {
                var module = API_SERVER.getModule(MCSpark.class);
                if (module == null) return;
                get("tps", ctx -> ctx.json(module.getTPS()));
                get("mspt", ctx -> ctx.json(module.getMSPT()));
                get("cpu", ctx -> ctx.json(module.getCPUUsage()));
            });

            path("console", () -> {
                var module = API_SERVER.getModule(CommandConsole.class);
                if (module == null) return;
                post("dispatch", ctx -> {
                    String command = ctx.queryParamAsClass("command", String.class).get();
                    module.dispatchConsoleCommand(command);
                    ctx.json(successResponse());
                });
                get("logs", ctx -> ctx.json(module.getConsoleLogs()));
            });

            path("player", () -> {
                var module = API_SERVER.getModule(Player.class);
                if (module == null) return;
                get("online", ctx -> ctx.json(module.getOnlinePlayers()));
            });

            path("plugin", () -> {
                var module = API_SERVER.getModule(PluginList.class);
                if (module == null) return;
                get("list", ctx -> ctx.json(module.getPluginList()));
            });

            get("ping", ctx -> ctx.json(Map.of(
                    "status", "success",
                    "message", "Pong!"
            )));
        };
    }

    public static Map<String, String> successResponse() {
        return Map.of("status", "success");
    }
}

package ink.magma.riasminecraftbridge.socket.bridge;

import ink.magma.riasminecraftbridge.platform.PlatformState;
import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.setting.SettingDeployer;
import ink.magma.riasminecraftbridge.socket.bridge.module.CommandConsole;
import ink.magma.riasminecraftbridge.socket.bridge.module.MCSpark;
import ink.magma.riasminecraftbridge.socket.bridge.module.Player;
import ink.magma.riasminecraftbridge.socket.bridge.module.PluginList;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URI;
import java.util.ArrayList;

public class ClientMain {
    private static Socket socket;
    private static ArrayList<Module> modules;

    public static Socket getSocket() {
        return socket;
    }

    public static void init() {
        // 连接到 Socket.io 服务端
        String baseURI = SettingDeployer.get().hubServer.baseURI();
        URI uri = URI.create(baseURI);
        IO.Options options = IO.Options.builder()
                .setPath("/socket.io/spigot-bridge")
                .build();
        socket = IO.socket(uri, options);
        socket.connect();

        AdopterProvider.get().logInfo("Hub 服务器设置为了 " + baseURI + " .");

        // 模块启动逻辑
        modules = new ArrayList<>();

        // 仅 Bukkit
        if (PlatformState.getPlatform().equals(PlatformState.Platform.BUKKIT)) {
            // 由于 Proxy 没有 TPS 和 MSPT 的概念，因此直接不支持
            modules.add(new MCSpark());
        }

        modules.add(new CommandConsole());
        modules.add(new Player());
        modules.add(new PluginList());

        AdopterProvider.get().logInfo("模块已全部注册完毕.");
    }

    public static void stop() {
        socket.close();
        modules.forEach(Module::disable);
    }
}

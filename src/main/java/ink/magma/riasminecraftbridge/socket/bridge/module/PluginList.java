package ink.magma.riasminecraftbridge.socket.bridge.module;

import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.record.Plugin;
import ink.magma.riasminecraftbridge.socket.bridge.ClientMain;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;
import ink.magma.riasminecraftbridge.utils.json.GsonIns;
import io.socket.client.Socket;

import java.util.Collection;

/**
 * 本模块实现了通过 Socket.io 获取服务器插件列表。
 */
public class PluginList implements Module {
    private final PlatformAdopter platform;

    public PluginList() {
        platform = AdopterProvider.get();
        Socket socket = ClientMain.getSocket();

        socket.on("getPluginListEvent", (args) -> {
            Collection<Plugin> pluginList = platform.getPlugins();
            socket.emit("getPluginListEvent", GsonIns.gson.toJson(pluginList));
        });
    }

    @Override
    public void disable() {
        // 可以在这里实现模块的停用逻辑
    }
}
package ink.magma.riaspigotbridge.socket.bridge.module;

import ink.magma.riaspigotbridge.platform.adopter.AdopterProvider;
import ink.magma.riaspigotbridge.platform.adopter.PlatformAdopter;
import ink.magma.riaspigotbridge.record.Plugin;
import ink.magma.riaspigotbridge.socket.bridge.ClientMain;
import ink.magma.riaspigotbridge.socket.bridge.module.define.Module;
import ink.magma.riaspigotbridge.utils.json.GsonIns;
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
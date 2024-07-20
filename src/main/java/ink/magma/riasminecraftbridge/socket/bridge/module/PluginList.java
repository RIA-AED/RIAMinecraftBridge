package ink.magma.riasminecraftbridge.socket.bridge.module;

import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.record.Plugin;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;

import java.util.Collection;

/**
 * 本模块实现了通过 API 获取服务器插件列表。
 */
public class PluginList implements Module {
    private final PlatformAdopter platform;

    public PluginList() {
        platform = AdopterProvider.get();
    }

    public Collection<Plugin> getPluginList() {
        return platform.getPlugins();
    }
}
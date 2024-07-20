package ink.magma.riasminecraftbridge.socket.bridge.module;

import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.record.PlayerList;
import ink.magma.riasminecraftbridge.socket.bridge.module.define.Module;

/**
 * 本模块实现了通过 API 获取在线玩家列表。
 */
public class Player implements Module {
    private final PlatformAdopter platform;

    public Player() {
        platform = AdopterProvider.get();
    }

    public PlayerList getOnlinePlayers() {
        return platform.getPlayerList();
    }
}
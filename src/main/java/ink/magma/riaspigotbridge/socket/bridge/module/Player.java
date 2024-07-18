package ink.magma.riaspigotbridge.socket.bridge.module;

import ink.magma.riaspigotbridge.platform.adopter.AdopterProvider;
import ink.magma.riaspigotbridge.platform.adopter.PlatformAdopter;
import ink.magma.riaspigotbridge.record.PlayerList;
import ink.magma.riaspigotbridge.socket.bridge.ClientMain;
import ink.magma.riaspigotbridge.socket.bridge.module.define.Module;
import ink.magma.riaspigotbridge.utils.json.GsonIns;
import io.socket.client.Socket;

/**
 * 本模块实现了通过 Socket.io 获取在线玩家列表。
 */
public class Player implements Module {
    private final PlatformAdopter platform;

    public Player() {
        platform = AdopterProvider.get();
        Socket socket = ClientMain.getSocket();

        socket.on("getOnlinePlayersEvent", (args) -> {
            PlayerList playerList = platform.getPlayerList();
            socket.emit("getOnlinePlayersEvent", GsonIns.gson.toJson(playerList));
        });
    }

    @Override
    public void disable() {
        // 可以在这里实现模块的停用逻辑
    }
}
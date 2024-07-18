package ink.magma.riaspigotbridge.socket.bridge.module.define;

/**
 * 描述了一种与 Hub 通信的模块
 * 此类模块基于 Socket.io 与服务器进行通信，以实现远程调用。
 */
public interface Module {
    void disable();
}

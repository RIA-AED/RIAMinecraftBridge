# RIAMinecraftBridge

本插件用于桥接 RIA 工作台（目前称为 Hub）和 Minecraft 服务端。

通过 Socket.io 实现连接，通信模型是常见的事件总线。

## 跨平台

本插件支持跨平台，目前支持 BungeeCord 和 Bukkit。

为插件增加平台支持仅需实现 `PlatformAdopter` 接口，然后调整 `PlatformState`，仿照 Bukkit 平台 `RIAMinecraftBridge_Bukkit`
类来管理生命周期即可。

## 事件理解

在 Hub（目前计划为 Node.js 实现）上，创建了一个 Socket.io 服务器。

在 Minecraft 服务端上，创建了一个 Socket.io 客户端。

Hub 和 Minecraft 服务器都可以触发和监听事件。

以下是一个查询服务器插件列表的例子：

```js
import { Server } from "socket.io";

const io = new Server({
  path: "/socket.io/spigot-bridge",
});

// 插件连接到此 Hub
io.on("connection", (socket) => {
  // 手动触发请求插件列表事件
  socket.emit("getPluginListEvent");
  // 监听返回的事件
  socket.once("getPluginListEvent", (data) => {
    console.log(data);
  });
});

io.listen(3233);
```

控制台输出：

```json
[
  {
    "name": "LuckPerms",
    "version": "5.4.58",
    "authors": [
      "Luck"
    ],
    "description": "A permissions plugin"
  },
  {
    "name": "PlaceholderAPI",
    "version": "2.11.2",
    "authors": [
      "HelpChat"
    ],
    "description": "An awesome placeholder provider!"
  }
]
```

Minecraft 服务器侧（即当前插件）实现案例：

```java
package ink.magma.riasminecraftbridge.socket.bridge.module;

import ink.magma.riasminecraftbridge.platform.adopter.AdopterProvider;
import ink.magma.riasminecraftbridge.platform.adopter.PlatformAdopter;
import ink.magma.riasminecraftbridge.record.Plugin;
import ink.magma.riasminecraftbridge.socket.bridge.APIServer;
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
```

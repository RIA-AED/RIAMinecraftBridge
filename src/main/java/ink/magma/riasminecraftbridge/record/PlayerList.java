package ink.magma.riasminecraftbridge.record;

import java.util.List;

public record PlayerList(List<Server> servers) {
    public record Server(String name, List<Player> players) {
    }

    public record Player(String uuid, String name) {
    }
}

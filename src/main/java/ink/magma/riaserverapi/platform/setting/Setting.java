package ink.magma.riaserverapi.platform.setting;

import de.exlll.configlib.Configuration;

@Configuration
public final class Setting {
    public HTTPServer httpServer = new HTTPServer(23600);

    public record HTTPServer(int port) {
    }
}

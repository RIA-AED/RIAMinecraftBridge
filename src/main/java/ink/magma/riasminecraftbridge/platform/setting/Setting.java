package ink.magma.riasminecraftbridge.platform.setting;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public final class Setting {
    @Comment("中心 Hub 服务器的链接信息")
    public HubServer hubServer = new HubServer("http://localhost:3233");

    public record HubServer(@Comment("需包含完整协议") String baseURI) {
    }
}

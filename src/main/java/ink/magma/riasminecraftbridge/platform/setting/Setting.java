package ink.magma.riasminecraftbridge.platform.setting;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public final class Setting {
    @Comment("接口的相关设置")
    public APISettings apiSettings = new APISettings(23600);

    public record APISettings(@Comment("服务启动的端口") Integer port) {
    }
}

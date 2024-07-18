package ink.magma.riaspigotbridge.platform.setting;

import de.exlll.configlib.YamlConfigurations;
import ink.magma.riaspigotbridge.platform.adopter.AdopterProvider;

import java.io.File;

public class SettingDeployer {
    private static Setting setting;

    public static Setting load() {
        File pluginFolder = AdopterProvider.get().getPluginFolder();
        if (!pluginFolder.exists()) pluginFolder.mkdirs();

        File file = new File(pluginFolder, "settings.yml");

        setting = YamlConfigurations.update(
                file.toPath(),
                Setting.class
        );

        return setting;
    }

    public static Setting get() {
        return setting == null ? load() : setting;
    }
}

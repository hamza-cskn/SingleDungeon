package mc.obliviate.singledungeon.config;

import mc.obliviate.singledungeon.SingleDungeon;
import mc.obliviate.singledungeon.arena.DungeonArena;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class ConfigurationHandler {

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String ARENAS_FILE_NAME = "arenas.yml";
    private static YamlConfiguration config;
    private static YamlConfiguration arenas;

    public void load() {
        loadConfigFile(new File(SingleDungeon.getInstance().getDataFolder() + File.separator + CONFIG_FILE_NAME));
        loadArenasFile(new File(SingleDungeon.getInstance().getDataFolder() + File.separator + ARENAS_FILE_NAME));
        loadArenas();
    }

    private void loadConfigFile(File configFile) {
        config = YamlConfiguration.loadConfiguration(configFile);
        if (config.getKeys(false).isEmpty()) {
            SingleDungeon.getInstance().saveResource(CONFIG_FILE_NAME, true);
            config = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    private void loadArenasFile(File configFile) {
        arenas = YamlConfiguration.loadConfiguration(configFile);
        if (arenas.getKeys(false).isEmpty()) {
            SingleDungeon.getInstance().saveResource(ARENAS_FILE_NAME, true);
            arenas = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    private void loadArenas() {
        for (String arenaName : arenas.getKeys(false)) {
            DungeonArena.deserialize(Objects.requireNonNull(arenas.getConfigurationSection(arenaName)));
        }
    }
}

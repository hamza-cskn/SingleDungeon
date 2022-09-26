package mc.obliviate.singledungeon;

import mc.obliviate.singledungeon.command.StartCMD;
import mc.obliviate.singledungeon.config.ConfigurationHandler;
import mc.obliviate.singledungeon.listener.ArenaListener;
import mc.obliviate.singledungeon.listener.ConnectionListener;
import mc.obliviate.singledungeon.listener.MobListener;
import mc.obliviate.singledungeon.data.repository.DungeonRepository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SingleDungeon extends JavaPlugin {

    private final ConfigurationHandler configurationHandler = new ConfigurationHandler();
    private DungeonRepository dungeonRepository;

    public static SingleDungeon getInstance() {
        return JavaPlugin.getPlugin(SingleDungeon.class);
    }

    @Override
    public void onEnable() {
        this.configurationHandler.load();
        this.registerListeners();
        this.registerCommands();
        this.dungeonRepository = new DungeonRepository();
    }

    private void registerCommands() {
        Objects.requireNonNull(Bukkit.getPluginCommand("start")).setExecutor(new StartCMD());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArenaListener(), this);
        Bukkit.getPluginManager().registerEvents(new MobListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    public DungeonRepository getDungeonRepository() {
        return dungeonRepository;
    }
}

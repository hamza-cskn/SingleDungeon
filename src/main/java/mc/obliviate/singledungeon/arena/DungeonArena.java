package mc.obliviate.singledungeon.arena;

import mc.obliviate.singledungeon.util.serializer.YamlSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonArena {

    private static final Map<String, DungeonArena> DUNGEON_ARENA_MAP = new HashMap<>();

    private final Location spawnLocation;
    private final List<Location> mobSpawnLocations;

    public static DungeonArena deserialize(ConfigurationSection section) {
        Location spawnLocation = YamlSerializer.deserializeLocationFromYaml(section.getConfigurationSection("spawn-location"));
        List<Location> mobSpawnLocationList = YamlSerializer.deserializeLocationListFromYaml(section.getConfigurationSection("mob-spawn-location"));
        return new DungeonArena(section.getName(), spawnLocation, mobSpawnLocationList);
    }

    public static void serialize(ConfigurationSection section, DungeonArena arena) {
        YamlSerializer.serializeLocationToYaml(section, arena.spawnLocation);
        YamlSerializer.serializeLocationListToYaml(section, arena.mobSpawnLocations);
    }

    public DungeonArena(String arenaName, Location spawnLocation, List<Location> mobSpawnLocations) {
        this.spawnLocation = spawnLocation;
        this.mobSpawnLocations = mobSpawnLocations;
        DungeonArena.DUNGEON_ARENA_MAP.put(arenaName, this);
    }

    public static DungeonArena getArenaByName(String arenaName) {
        return DUNGEON_ARENA_MAP.get(arenaName);
    }

    public static Map<String, DungeonArena> getDungeonArenaMap() {
        return Collections.unmodifiableMap(DUNGEON_ARENA_MAP);
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public List<Location> getMobSpawnLocations() {
        return mobSpawnLocations;
    }

}

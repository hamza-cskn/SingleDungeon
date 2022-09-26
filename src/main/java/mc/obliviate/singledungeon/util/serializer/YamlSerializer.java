package mc.obliviate.singledungeon.util.serializer;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class YamlSerializer {

    public static void serializeLocationToYaml(final ConfigurationSection section, final Location location) {
        if (location == null) return;
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", (double) location.getYaw());
        section.set("pitch", (double) location.getPitch());
    }

    @SuppressWarnings("ConstantConditions")
    public static Location deserializeLocationFromYaml(final ConfigurationSection section) {
        Preconditions.checkNotNull(section, "section cannot be null");
        if (!(section.isSet("world") && section.isSet("x") && section.isSet("y") && section.isSet("x"))) return null;
        final World world = Bukkit.getWorld(section.getString("world"));
        Preconditions.checkNotNull(world, "world could not found (YAML loc deserializing): " + section.getString("world"));

        final double x = section.getDouble("x");
        final double y = section.getDouble("y");
        final double z = section.getDouble("z");
        final double yaw = section.getDouble("yaw", 0);
        final double pitch = section.getDouble("pitch", 0);

        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static void serializeLocationListToYaml(final ConfigurationSection section, List<Location> locationList) {
        Preconditions.checkNotNull(section, "section cannot be null");
        int index = 0;
        for (Location loc : locationList) {
            serializeLocationToYaml(section.createSection("" + index++), loc);
        }
    }

    public static List<Location> deserializeLocationListFromYaml(final ConfigurationSection section) {
        Preconditions.checkNotNull(section, "section cannot be null");
        final List<Location> result = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            result.add(deserializeLocationFromYaml(section.getConfigurationSection(key)));
        }
        return result;
    }

}

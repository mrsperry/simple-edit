package io.github.mrsperry.simpleedit;

import org.bukkit.Location;
import org.bukkit.World;

public final class Utils {
    public static String coordinateString(final Location location) {
        return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static String locationString(final Location location) {
        final World world = location.getWorld();
        if (world == null) {
            return "";
        }

        return world.getName() + "," + Utils.coordinateString(location);
    }
}

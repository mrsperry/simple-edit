package io.github.mrsperry.simpleedit;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

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

    public static Pair<Material, Integer> parseMaterialChance(final String arg, final int argsLength) {
        final String[] split = arg.split("%");
        String materialString = arg;

        int chance = (int) Math.floor(100f / (argsLength - 1));
        if (split.length == 2) {
            try {
                chance = Integer.parseInt(split[0]);
            } catch (final Exception ex) {
                return null;
            }

            materialString = split[1];
        }

        final Material material;
        try {
            material = Material.valueOf(materialString.toUpperCase());
        } catch (final IllegalArgumentException ex) {
            return null;
        }

        return new Pair<>(material, chance);
    }

    public static List<String> getMaterialStrings() {
        final List<String> materials = new ArrayList<>();
        for (final Material material : Material.values()) {
            materials.add(material.toString().toLowerCase());
        }
        return materials;
    }

    public static List<String> getMaterialChanceTabComplete(String arg) {
        if (arg.contains("%") && arg.length() > arg.indexOf("%")) {
            arg = arg.substring(arg.indexOf("%") + 1);
        }

        return StringUtil.copyPartialMatches(arg, Utils.getMaterialStrings(), new ArrayList<>());
    }

    public static Location parseLocation(final String string) {
        final String[] args = string.split(",");

        try {
            return new Location(
                    Bukkit.getWorld(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]));
        } catch (final Exception ex) {
            return null;
        }
    }
}

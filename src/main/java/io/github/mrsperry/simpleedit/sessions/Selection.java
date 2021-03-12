package io.github.mrsperry.simpleedit.sessions;

import io.github.mrsperry.simpleedit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.function.Predicate;

public final class Selection {
    private Location pos1;
    private Location pos2;

    public final void setPosition(final boolean firstPosition, final Location location) {
        if (firstPosition) {
            this.pos1 = location;
        } else {
            this.pos2 = location;
        }
    }

    public final ArrayList<Block> getCubeBlocks() {
        return this.getCubeSelection();
    }

    public final ArrayList<Block> getBlocksByPredicate(Predicate<Block> predicate) {
        final ArrayList<Block> results = new ArrayList<>();
        for (final Block block : this.getCubeSelection()) {
            if (predicate.test(block)) {
                results.add(block);
            }
        }

        return results;
    }

    private boolean checkPositionWorlds() {
        return this.pos1.getWorld() != this.pos2.getWorld();
    }

    private ArrayList<Block> getCubeSelection() {
        final ArrayList<Block> blocks = new ArrayList<>();
        if (this.checkPositionWorlds()) {
            return blocks;
        }

        final int startX = Math.min(this.pos1.getBlockX(), this.pos2.getBlockX());
        final int startY = Math.min(this.pos1.getBlockY(), this.pos2.getBlockY());
        final int startZ = Math.min(this.pos1.getBlockZ(), this.pos2.getBlockZ());

        final int endX = Math.max(this.pos1.getBlockX(), this.pos2.getBlockX());
        final int endY = Math.max(this.pos1.getBlockY(), this.pos2.getBlockY());
        final int endZ = Math.max(this.pos1.getBlockZ(), this.pos2.getBlockZ());

        final World world = this.pos1.getWorld();
        if (world == null) {
            return blocks;
        }

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public final String serialize() {
        return Utils.locationString(this.pos1) + "|" + Utils.locationString(this.pos2);
    }

    public static Selection deserialize(final String data) {
        final Selection selection = new Selection();
        final String[] positions = data.split("\\|");

        selection.setPosition(true, Selection.parseLocation(positions[0]));
        selection.setPosition(false, Selection.parseLocation(positions[1]));

        return selection;
    }

    private static Location parseLocation(final String string) {
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

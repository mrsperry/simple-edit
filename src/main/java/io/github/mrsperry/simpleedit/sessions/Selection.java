package io.github.mrsperry.simpleedit.sessions;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Predicate;

public final class Selection {
    private Location pos1;
    private Location pos2;
    private Vector<Integer> start;
    private Vector<Integer> end;

    public final void setPosition(final boolean firstPosition, final Location location) {
        if (firstPosition) {
            this.pos1 = location;
        } else {
            this.pos2 = location;
        }

        final int x1 = this.pos1.getBlockX();
        final int x2 = this.pos2.getBlockX();
        final int y1 = this.pos1.getBlockY();
        final int y2 = this.pos2.getBlockY();
        final int z1 = this.pos1.getBlockZ();
        final int z2 = this.pos2.getBlockZ();

        this.start = new Vector<>(Lists.newArrayList(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2)));
        this.end = new Vector<>(Lists.newArrayList(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2)));
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

        final World world = this.pos1.getWorld();
        if (world == null) {
            return blocks;
        }

        for (int x = this.start.get(0); x <= this.end.get(0); x++) {
            for (int y = this.start.get(1); y <= this.end.get(1); y++) {
                for (int z = this.start.get(2); z <= this.end.get(2); z++) {
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

package io.github.mrsperry.simpleedit.sessions;

import io.github.mrsperry.simpleedit.SimpleEdit;
import io.github.mrsperry.simpleedit.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Selection {
    private final JavaPlugin plugin;
    private final long outlineUpdateRate;

    private World world;
    private Location pos1;
    private Location pos2;
    private int[] start;
    private int[] end;

    private boolean doDrawOutline;
    private int outlineTaskID;

    public Selection() {
        this.plugin = SimpleEdit.getInstance();
        this.outlineUpdateRate = this.plugin.getConfig().getLong("outline-update-rate", 10);

        this.doDrawOutline = false;
    }

    public final void setPosition(final boolean firstPosition, final Location location) {
        if (firstPosition) {
            this.pos1 = location;
        } else {
            this.pos2 = location;
        }
        this.world = location.getWorld();

        if (this.pos1 != null && this.pos2 != null) {
            final int x1 = this.pos1.getBlockX();
            final int x2 = this.pos2.getBlockX();
            final int y1 = this.pos1.getBlockY();
            final int y2 = this.pos2.getBlockY();
            final int z1 = this.pos1.getBlockZ();
            final int z2 = this.pos2.getBlockZ();

            this.start = new int[] { Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2) };
            this.end = new int[] { Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2) };
        }

        if (this.doDrawOutline) {
            this.resetOutline();
            this.drawOutline();
        }
    }

    public final boolean toggleDrawOutline() {
        this.doDrawOutline = !this.doDrawOutline;
        if (this.doDrawOutline) {
            this.drawOutline();
        } else {
            this.resetOutline();
        }
        return this.doDrawOutline;
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

    private boolean checkLocationPrerequisites() {
        return this.pos1 == null
                || this.pos2 == null
                || this.world == null
                || this.pos1.getWorld() != this.pos2.getWorld();
    }

    private void resetOutline() {
        Bukkit.getScheduler().cancelTask(this.outlineTaskID);
        this.outlineTaskID = -1;
    }

    private void drawOutline() {
        if (this.checkLocationPrerequisites()) {
            return;
        }

        final World world = this.world;
        final int[] start = this.start;
        final int[] end = this.end;

        this.outlineTaskID = new BukkitRunnable() {
            @Override
            public void run() {
                runSelectionConsumer((int[] coords) -> {
                    final boolean xEdge = (coords[0] == start[0] || coords[0] == end[0]);
                    final boolean yEdge = (coords[1] == start[1] || coords[1] == end[1]);
                    final boolean zEdge = (coords[2] == start[2] || coords[2] == end[2]);

                    // Only spawn particles along the edges of the selection cubes
                    if ((xEdge && yEdge) || (yEdge && zEdge) || (xEdge && zEdge)) {
                        final Location location = new Location(world, coords[0], coords[1], coords[2]).add(0.5, 0.5, 0.5);

                        if (!world.getBlockAt(location).getType().isSolid()) {
                            world.spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 1, new Particle.DustOptions(Color.RED, 1));
                        }
                    }
                });
            }
        }.runTaskTimer(this.plugin, 0, this.outlineUpdateRate).getTaskId();
    }

    private ArrayList<Block> getCubeSelection() {
        final ArrayList<Block> blocks = new ArrayList<>();
        if (this.checkLocationPrerequisites()) {
            return blocks;
        }

        this.runSelectionConsumer((int[] coords) -> blocks.add(this.world.getBlockAt(coords[0], coords[1], coords[2])));

        return blocks;
    }

    private void runSelectionConsumer(final Consumer<int[]> consumer) {
        for (int x = this.start[0]; x <= this.end[0]; x++) {
            for (int y = this.start[1]; y <= this.end[1]; y++) {
                for (int z = this.start[2]; z <= this.end[2]; z++) {
                    consumer.accept(new int[] { x, y, z });
                }
            }
        }
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

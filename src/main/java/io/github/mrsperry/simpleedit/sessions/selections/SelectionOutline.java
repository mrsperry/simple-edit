package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.mcutils.types.ColorTypes;
import io.github.mrsperry.simpleedit.SimpleEdit;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public final class SelectionOutline {
    private final Selection selection;
    private final long updateRate;

    private boolean isDrawing;
    private int drawTaskID;
    private Color color;

    public SelectionOutline(final Selection selection, final long updateRate) {
        this.selection = selection;
        this.updateRate = updateRate;

        this.isDrawing = false;
        this.drawTaskID = -1;
        this.color = Color.RED;
    }

    public final boolean toggle() {
        this.isDrawing = !this.isDrawing;
        if (this.isDrawing) {
            this.draw();
        } else {
            this.stop();
        }

        return this.isDrawing;
    }

    public final void refresh() {
        if (this.isDrawing) {
            this.stop();
            this.draw();
        }
    }

    private void stop() {
        Bukkit.getScheduler().cancelTask(this.drawTaskID);
        this.drawTaskID = -1;
    }

    private void draw() {
        final SelectionPosition position = this.selection.getPosition();
        if (position.checkLocationPrerequisites()) {
            return;
        }

        final World world = position.getWorld();
        final List<Block> blocks = this.selection.getEdgeSelection();

        this.drawTaskID = new BukkitRunnable() {
            @Override
            public void run() {
                for (final Block block : blocks) {
                    final Location location = block.getLocation().add(0.5, 0.5, 0.5);

                    if (!world.getBlockAt(location).getType().isSolid()) {
                        world.spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 1, new Particle.DustOptions(color, 1));
                    }
                }
            }
        }.runTaskTimer(SimpleEdit.getInstance(), 0, this.updateRate).getTaskId();
    }

    public final String serialize() {
        return "outline{"
                + (this.isDrawing ? "enabled" : "disabled") + ";"
                + ColorTypes.colorToString(this.color).toLowerCase() + "}";
    }

    public static void deserialize(final SelectionOutline outline, final String[] data) {
        if (data[0].equalsIgnoreCase("enabled")) {
            outline.toggle();
        }

        outline.setColor(ColorTypes.stringToColor(data[1]));
    }

    public final void setColor(final Color color) {
        this.color = color;
    }
}

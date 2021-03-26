package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class CylAction extends Action {
    private CylAction(final SelectionHistory history, final Location center, final int radius, final int height, final List<Pair<Material, Integer>> materials) {
        final Location blockCenter = center.getBlock().getLocation().add(0.5, 0.5, 0.5);
        final List<Block> blocks = this.getBlocks(radius, blockCenter);

        final List<Block> holder = new ArrayList<>();
        if (height >= 1) {
            for (final Block block : blocks) {
                for (int y = 1; y < height; y++) {
                    final Block current = block.getLocation().add(0, y, 0).getBlock();

                    if (!holder.contains(current)) {
                        holder.add(current);
                    }
                }
            }
        }
        blocks.addAll(holder);

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    private CylAction(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        final Location blockCenter = center.getBlock().getLocation().add(0.5, 0.5, 0.5);
        final Predicate<Block> predicate = (final Block block) -> block.getRelative(0, 1, 0).getType().isAir();
        final List<Block> blocks = this.getBlocks(radius, blockCenter, predicate);

        super.run(history, blocks, masks, super.getMaterialWeights(materials));
    }

    private List<Block> getBlocks(final int radius, final Location center) {
        return this.getBlocks(radius, center, (final Block block) -> true);
    }

    private List<Block> getBlocks(final int radius, final Location center, final Predicate<Block> predicate) {
        final Vector centerVector = center.toVector();
        final List<Block> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                final Location location = center.clone().add(x, 0, z);

                if (location.toVector().distance(centerVector) >= radius + 0.5) {
                    continue;
                }

                final Block block = location.getBlock();
                if (predicate.test(block)) {
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final int height, final List<Pair<Material, Integer>> materials) {
        new CylAction(history, center, radius, height, materials);
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new CylAction(history, center, radius, masks, materials);
    }
}

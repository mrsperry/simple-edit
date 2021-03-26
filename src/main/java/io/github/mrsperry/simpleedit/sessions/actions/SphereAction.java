package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public final class SphereAction extends Action {
    private SphereAction(final SelectionHistory history, final Location center, final boolean hollow, final int radius, final List<Pair<Material, Integer>> materials) {
        final Location blockCenter = center.getBlock().getLocation().add(0.5, 0.5, 0.5);

        final BiPredicate<Block, Double> predicate = (final Block block, final Double distance) -> !hollow || !(distance <= radius - 0.5);
        final List<Block> blocks = this.getBlocks(blockCenter, radius, predicate);

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    private SphereAction(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        final Location blockCenter = center.getBlock().getLocation().add(0.5, 0.5, 0.5);
        super.run(history, this.getBlocks(blockCenter, radius), masks, super.getMaterialWeights(materials));
    }

    private List<Block> getBlocks(final Location center, final int radius) {
        return this.getBlocks(center, radius, (final Block block, final Double distance) -> true);
    }

    private List<Block> getBlocks(final Location center, final int radius, final BiPredicate<Block, Double> predicate) {
        final Vector centerVector = center.toVector();
        final List<Block> blocks = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final Location location = center.clone().add(x, y, z);

                    final double distance = location.toVector().distance(centerVector);
                    if (distance >= radius + 0.5) {
                        continue;
                    }

                    final Block block = location.getBlock();
                    if (predicate.test(block, distance)) {
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public static void run(final SelectionHistory history, final Location center, final boolean hollow, final int radius, final List<Pair<Material, Integer>> materials) {
        new SphereAction(history, center, hollow, radius, materials);
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new SphereAction(history, center, radius, masks, materials);
    }
}
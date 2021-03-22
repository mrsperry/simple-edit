package io.github.mrsperry.simpleedit.sessions.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public final class FillAction extends Action {
    private FillAction(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        final World world = center.getWorld();
        if (world == null) {
            return;
        }

        final Vector centerVector = center.toVector();
        final List<Block> blocks = new ArrayList<>();
        final Queue<Block> frontier = new PriorityQueue<>();
        frontier.add(world.getBlockAt(center));

        // Breadth-first search
        while (!frontier.isEmpty()) {
            final Block current = frontier.poll();

            final List<Block> neighbors = Lists.newArrayList(
                    current.getRelative(BlockFace.NORTH),
                    current.getRelative(BlockFace.SOUTH),
                    current.getRelative(BlockFace.EAST),
                    current.getRelative(BlockFace.WEST)
            );

            for (final Block block : neighbors) {
                if (blocks.contains(block)) {
                    continue;
                }

                if (block.getType().isSolid()) {
                    continue;
                }

                if (block.getLocation().toVector().distance(centerVector) > radius) {
                    continue;
                }

                blocks.add(block);
                frontier.add(block);
            }
        }

        // Add all non-solid blocks below the current blocks
        for (final Block block : blocks) {
            final int yLevel = block.getY();

            for (int y = yLevel - 1; y >= 0; y--) {
                final Block current = block.getRelative(0, -(yLevel - y), 0);

                if (current.getType().isSolid()) {
                    break;
                }

                blocks.add(current);
            }
        }

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        new FillAction(history, center, radius, materials);
    }
}

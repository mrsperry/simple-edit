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

public final class FillAction extends Action {
    private FillAction(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        final World world = center.getWorld();
        if (world == null) {
            return;
        }

        final Vector centerVector = center.toVector();
        final List<Block> blocks = new ArrayList<>();
        final List<Block> frontier = new ArrayList<>();
        frontier.add(world.getBlockAt(center));

        // Breadth-first search
        while (!frontier.isEmpty()) {
            final Block current = frontier.remove(0);

            final List<Block> neighbors = Lists.newArrayList(
                    current.getRelative(BlockFace.NORTH),
                    current.getRelative(BlockFace.SOUTH),
                    current.getRelative(BlockFace.EAST),
                    current.getRelative(BlockFace.WEST)
            );

            for (final Block neighbor : neighbors) {
                if (blocks.contains(neighbor)) {
                    continue;
                }

                if (neighbor.getType().isSolid()) {
                    continue;
                }

                if (neighbor.getLocation().toVector().distance(centerVector) > radius) {
                    continue;
                }

                blocks.add(neighbor);
                frontier.add(neighbor);

                // Add all non-solid blocks below the current block
                final int yLevel = current.getY();
                for (int y = yLevel - 1; y >= 0; y--) {
                    final Block below = current.getRelative(0, -(yLevel - y), 0);

                    if (below.getType().isSolid()) {
                        break;
                    }

                    blocks.add(below);
                }
            }
        }

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        new FillAction(history, center, radius, materials);
    }
}

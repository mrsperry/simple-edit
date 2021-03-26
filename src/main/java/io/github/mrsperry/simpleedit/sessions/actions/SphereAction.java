package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class SphereAction extends Action {
    private SphereAction(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        final Location blockCenter = center.getBlock().getLocation().add(0.5, 0.5, 0.5);
        final Vector centerVector = blockCenter.clone().toVector();

        final List<Block> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final Location location = blockCenter.clone().add(x, y, z);

                    final double distance = location.toVector().distance(centerVector);
                    if (distance <= radius + 0.5) {
                        blocks.add(location.getBlock());
                    }
                }
            }
        }

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        new SphereAction(history, center, radius, materials);
    }
}
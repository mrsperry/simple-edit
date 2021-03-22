package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.List;

public final class ReplaceNearAction extends Action {
    private ReplaceNearAction(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        final List<Block> blocks = new ArrayList<>();
        final Block centerBlock = center.getBlock();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final Block current = centerBlock.getRelative(x, y, z);

                    if (masks.contains(current.getType())) {
                        blocks.add(current);
                    }
                }
            }
        }

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new ReplaceNearAction(history, center, radius, masks, materials);
    }
}

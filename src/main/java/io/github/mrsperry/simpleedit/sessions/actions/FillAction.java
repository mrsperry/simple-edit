package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.List;

public final class FillAction extends Action {
    private FillAction(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        final List<Block> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                final Location location = center.clone().add(x, 0, z);

                for (int y = location.getBlockY(); y >= 0; y--) {
                    final Block current = location.subtract(0, location.getBlockY() - y, 0).getBlock();
                    if (current.getType().isSolid()) {
                        break;
                    }

                    blocks.add(current);
                }
            }
        }

        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        new FillAction(history, center, radius, materials);
    }
}

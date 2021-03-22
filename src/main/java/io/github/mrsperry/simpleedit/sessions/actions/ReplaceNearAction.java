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
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(center.clone().add(x, y, z).getBlock());
                }
            }
        }

        super.run(history, blocks, masks, super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new ReplaceNearAction(history, center, radius, masks, materials);
    }
}

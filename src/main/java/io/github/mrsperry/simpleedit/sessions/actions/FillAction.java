package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class FillAction extends Action {
    private FillAction(final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        final Random random = new Random();
        final List<Material> weights = super.getMaterialWeights(materials);

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

        super.run(blocks, (final Block block) -> {
            final Material material = weights.get(random.nextInt(weights.size()));
            block.setType(material);
        });
    }

    public static void run(final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        new FillAction(center, radius, materials);
    }
}

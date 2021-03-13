package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.Selection;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.List;
import java.util.Random;

public final class ReplaceAction extends Action {
    private ReplaceAction(final List<Block> blocks, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        final Random random = new Random();
        final List<Material> weights = super.getMaterialWeights(materials);

        super.run(blocks, masks, (final Block block) -> {
            final Material material = weights.get(random.nextInt(weights.size()));
            block.setType(material);
        });
    }

    public static void run(final Selection selection, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new ReplaceAction(selection.getCubeBlocks(), masks, materials);
    }
}

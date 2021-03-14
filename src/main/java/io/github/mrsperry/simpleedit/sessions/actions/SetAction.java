package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.List;
import java.util.Random;

public final class SetAction extends Action {
    private SetAction(final List<Block> blocks, final List<Pair<Material, Integer>> materials) {
        final Random random = new Random();
        final List<Material> weights = super.getMaterialWeights(materials);

        super.run(blocks, (final Block block) -> {
            final Material material = weights.get(random.nextInt(weights.size()));
            block.setType(material);
        });
    }

    public static void run(final Selection selection, final List<Pair<Material, Integer>> materials) {
        new SetAction(selection.getCubeSelection(), materials);
    }
}

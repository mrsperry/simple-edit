package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.Selection;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class SetAction extends Action {
    private SetAction(final Selection selection, List<Material> weights) {
        final Random random = new Random();

        super.run(selection.getCubeBlocks(), (Block block) -> {
            final Material material = weights.get(random.nextInt(weights.size()));
            block.setType(material);
        });
    }

    public static void run(final Selection selection, final ArrayList<Pair<Material, Integer>> materials) {
        final List<Material> weights = new ArrayList<>();
        for (final Pair<Material, Integer> weight : materials) {
            for (int index = 0; index < weight.getValue(); index++) {
                weights.add(weight.getKey());
            }
        }

        new SetAction(selection, weights);
    }
}

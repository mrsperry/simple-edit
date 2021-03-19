package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.List;
import java.util.Random;

public final class BoxAction extends Action {
    private BoxAction(final SelectionHistory history, final List<Block> blocks, final List<Pair<Material, Integer>> materials) {
        final Random random = new Random();
        final List<Material> weights = super.getMaterialWeights(materials);

        super.run(history, blocks, (final Block block) -> {
            final Material material = weights.get(random.nextInt(weights.size()));
            block.setType(material);
        });
    }

    public static void run(final Selection selection, final boolean hollow, final List<Pair<Material, Integer>> materials) {
        new BoxAction(selection.getHistory(), hollow ? selection.getEdgeSelection() : selection.getFaceSelection(), materials);
    }
}

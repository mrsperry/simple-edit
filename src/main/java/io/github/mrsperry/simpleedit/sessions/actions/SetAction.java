package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.List;

public final class SetAction extends Action {
    private SetAction(final SelectionHistory history, final List<Block> blocks, final List<Pair<Material, Integer>> materials) {
        super.run(history, blocks, super.getMaterialWeights(materials));
    }

    public static void run(final Selection selection, final List<Pair<Material, Integer>> materials) {
        new SetAction(selection.getHistory(), selection.getCubeSelection(), materials);
    }
}

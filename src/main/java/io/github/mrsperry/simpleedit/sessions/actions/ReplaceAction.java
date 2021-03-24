package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public final class ReplaceAction extends Action {
    private ReplaceAction(final SelectionHistory history, final List<Block> blocks, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        super.run(history, blocks, masks, super.getMaterialWeights(materials));
    }

    public static void run(final Selection selection, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new ReplaceAction(selection.getHistory(), selection.getCubeSelection(), masks, materials);
    }
}

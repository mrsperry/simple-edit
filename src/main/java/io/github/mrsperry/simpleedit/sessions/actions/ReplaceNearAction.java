package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.List;

public final class ReplaceNearAction extends Action {
    private ReplaceNearAction(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        super.run(history, super.searchForBlocks(center, radius, masks), super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        new ReplaceNearAction(history, center, radius, masks, materials);
    }
}

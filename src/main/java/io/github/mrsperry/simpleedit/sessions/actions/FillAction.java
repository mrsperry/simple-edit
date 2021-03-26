package io.github.mrsperry.simpleedit.sessions.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.function.Predicate;

public final class FillAction extends Action {
    private FillAction(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        final int centerY = center.getBlock().getY();

        final List<Predicate<Block>> predicates = Lists.newArrayList(
                (final Block block) -> block.getY() > centerY,
                (final Block block) -> !block.getType().isAir()
        );

        super.run(history, super.searchFloodFill(center, radius, predicates), super.getMaterialWeights(materials));
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final List<Pair<Material, Integer>> materials) {
        new FillAction(history, center, radius, materials);
    }
}

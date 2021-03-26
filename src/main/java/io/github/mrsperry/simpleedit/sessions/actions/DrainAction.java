package io.github.mrsperry.simpleedit.sessions.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

import java.util.List;
import java.util.function.Predicate;

public final class DrainAction extends Action {
    private DrainAction(final SelectionHistory history, final Location center, final int radius, final boolean waterlogged) {
        final List<Predicate<Block>> predicates = Lists.newArrayList(
                (final Block block) -> {
                    final BlockData data = block.getBlockData();
                    if (waterlogged && data instanceof Waterlogged) {
                        return !((Waterlogged) data).isWaterlogged();
                    }

                    return !block.isLiquid();
                }
        );

        final List<Block> blocks = super.searchFloodFill(center, radius, predicates);

        super.run(history, blocks, (final Block block) -> {
            if (block.isLiquid()) {
                block.setType(Material.AIR);
            } else {
                final Waterlogged data = (Waterlogged) block.getBlockData();
                data.setWaterlogged(false);
                block.setBlockData(data);
            }
        });
    }

    public static void run(final SelectionHistory history, final Location center, final int radius, final boolean waterlogged) {
        new DrainAction(history, center, radius, waterlogged);
    }
}

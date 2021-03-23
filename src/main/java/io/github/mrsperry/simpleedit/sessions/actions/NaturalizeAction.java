package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class NaturalizeAction extends Action {
    private NaturalizeAction(final Selection selection, final List<Block> blocks, final boolean onlySkyBlocks) {
        final World world = selection.getPosition().getWorld();

        final List<Block> blockList = new ArrayList<>();
        for (final Block block : blocks) {
            if (block.getType() == Material.STONE) {
                blockList.add(block);
            }
        }

        super.run(selection.getHistory(), blockList, (final Block block) -> {
            if (!block.getRelative(0, 1, 0).getType().isAir()) {
                return;
            }

            if (onlySkyBlocks && world.getHighestBlockYAt(block.getX(), block.getZ()) != block.getY()) {
                return;
            }

            block.setType(Material.GRASS_BLOCK);

            for (int index = 1; index < 3; index++) {
                final Block below = block.getRelative(0, -index, 0);
                if (below.getType() == Material.STONE) {
                    below.setType(Material.DIRT);
                }
            }
        });
    }

    public static void run(final Selection selection, final boolean onlySkyBlocks) {
        new NaturalizeAction(selection, selection.getCubeSelection(), onlySkyBlocks);
    }
}

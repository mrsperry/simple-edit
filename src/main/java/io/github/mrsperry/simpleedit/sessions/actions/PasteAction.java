package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PasteAction extends Action {
    private PasteAction(final SelectionHistory history, final Location origin, final boolean ignoreAir, final Block[][][] blocks) {
        final List<Block> blockList = new ArrayList<>();
        final Map<Location, Pair<Material, BlockData>> blockData = new HashMap<>();

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = 0; z < blocks[0][0].length; z++) {
                    final Block block = blocks[x][y][z];
                    final Material type = block.getType();
                    if (ignoreAir && type.isAir()) {
                        continue;
                    }

                    final Block replace = block.getWorld().getBlockAt(origin.clone().add(x, y, z));
                    blockList.add(replace);
                    blockData.put(replace.getLocation(), new Pair<>(type, block.getBlockData()));
                }
            }
        }

        super.run(history, blockList, (final Block block) -> {
            final Pair<Material, BlockData> data = blockData.getOrDefault(block.getLocation(), null);
            if (data != null) {
                block.setType(data.getKey());
                block.setBlockData(data.getValue());
            }
        });
    }

    public static void run(final SelectionHistory history, final Location origin, final boolean ignoreAir, final Block[][][] blocks) {
        new PasteAction(history, origin, ignoreAir, blocks);
    }
}
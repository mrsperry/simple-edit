package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.sessions.selections.ClipboardDirection;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StackAction extends Action {
    private StackAction(final Selection selection, final boolean ignoreAir, final int amount, final ClipboardDirection.Cardinal direction, final Block[][][] blocks) {
        final List<Block> blockList = new ArrayList<>();
        final Map<Location, Pair<Material, BlockData>> blockData = new HashMap<>();

        final int[] start = selection.getPosition().getStart();
        final int[] end = selection.getPosition().getEnd();
        final Location startLocation = new Location(selection.getPosition().getWorld(), start[0], start[1], start[2]);

        int coord = 0;
        boolean negative = false;
        switch (direction) {
            case Up:
                coord = 1;
                break;
            case Down:
                coord = 1;
                negative = true;
                break;
            // -Z
            case North:
                coord = 2;
                negative = true;
                break;
            // +Z
            case South:
                coord = 2;
                break;
            // +X
            case East:
                coord = 0;
                break;
            // -X
            case West:
                coord = 0;
                negative = true;
                break;
        }

        int distance = 1 + (end[coord] - start[coord]);
        if (negative) {
            distance *= -1;
        }
        final Vector offset = new Vector(coord == 0 ? distance : 0, coord == 1 ? distance : 0, coord == 2 ? distance : 0);

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = 0; z < blocks[0][0].length; z++) {
                    final Block block = blocks[x][y][z];
                    final Material type = block.getType();
                    if (ignoreAir && type.isAir()) {
                        continue;
                    }

                    for (int index = 1; index < amount + 1; index++) {
                        final Location current = startLocation.clone().add(
                                x + (offset.getX() * index),
                                y + (offset.getY() * index),
                                z + (offset.getZ() * index));

                        final Block replace = current.getBlock();
                        blockList.add(replace);
                        blockData.put(replace.getLocation(), new Pair<>(type, block.getBlockData()));
                    }
                }
            }
        }

        super.run(selection.getHistory(), blockList, (final Block block) -> {
            final Pair<Material, BlockData> data = blockData.getOrDefault(block.getLocation(), null);
            if (data != null) {
                block.setType(data.getKey());
                block.setBlockData(data.getValue());
            }
        });
    }

    public static void run(final Selection selection, final boolean ignoreAir, final int amount, final ClipboardDirection.Cardinal direction) {
        new StackAction(selection, ignoreAir, amount, direction, selection.getCubeSelectionArray());
    }
}
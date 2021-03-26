package io.github.mrsperry.simpleedit.sessions.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.SimpleEdit;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Action {
    private static final JavaPlugin plugin = SimpleEdit.getInstance();
    private static final int blockLimit = Action.plugin.getConfig().getInt("affected-blocks-per-tick", 3000);

    private List<Block> blocks;
    private List<Material> masks;
    private Consumer<Block> action;
    private Map<Location, Pair<Material, BlockData>> affected;

    protected final void run(final SelectionHistory history, final List<Block> blocks, final List<Material> weights) {
        this.run(history, blocks, new ArrayList<>(), weights);
    }

    protected final void run(final SelectionHistory history, final List<Block> blocks, final Consumer<Block> action) {
        this.run(history, blocks, new ArrayList<>(), action);
    }

    protected final void run(final SelectionHistory history, final List<Block> blocks, final List<Material> masks, final List<Material> weights) {
        final Random random = new Random();
        final int size = weights.size();

        this.run(history, blocks, masks, (final Block block) -> block.setType(weights.get(random.nextInt(size))));
    }

    protected final void run(final SelectionHistory history, final List<Block> blocks, final List<Material> masks, final Consumer<Block> action) {
        this.blocks = blocks;
        this.masks = masks;
        this.action = action;
        this.affected = new HashMap<>();

        this.runNewTask(0);
        history.record(this);
    }

    protected final List<Material> getMaterialWeights(final List<Pair<Material, Integer>> materials) {
        final List<Material> weights = new ArrayList<>();
        for (final Pair<Material, Integer> weight : materials) {
            for (int index = 0; index < weight.getValue(); index++) {
                weights.add(weight.getKey());
            }
        }

        return weights;
    }

    protected final List<Block> searchForBlocks(final Location center, final int radius, final List<Material> masks) {
        final List<Block> blocks = new ArrayList<>();
        final Block centerBlock = center.getBlock();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final Block current = centerBlock.getRelative(x, y, z);

                    if (masks.contains(current.getType())) {
                        blocks.add(current);
                    }
                }
            }
        }

        return blocks;
    }

    protected final List<Block> searchFloodFill(final Location center, final int radius, List<Predicate<Block>> predicates) {
        final Vector centerVector = center.toVector();

        final List<Block> blocks = new ArrayList<>();
        final List<Block> frontier = new ArrayList<>();
        frontier.add(center.getBlock());

        // Breadth-first search
        while (!frontier.isEmpty()) {
            final Block current = frontier.remove(0);
            final List<Block> neighbors = Lists.newArrayList(
                    current.getRelative(BlockFace.UP),
                    current.getRelative(BlockFace.DOWN),
                    current.getRelative(BlockFace.NORTH),
                    current.getRelative(BlockFace.SOUTH),
                    current.getRelative(BlockFace.EAST),
                    current.getRelative(BlockFace.WEST)
            );

            for (final Block neighbor : neighbors) {
                if (blocks.contains(neighbor)) {
                    continue;
                }

                if (neighbor.getLocation().toVector().distance(centerVector) > radius) {
                    continue;
                }

                boolean failedPredicate = false;
                for (final Predicate<Block> predicate : predicates) {
                    if (predicate.test(neighbor)) {
                        failedPredicate = true;
                        break;
                    }
                }

                if (failedPredicate) {
                    continue;
                }

                blocks.add(neighbor);
                frontier.add(neighbor);
            }
        }

        return blocks;
    }

    private void runNewTask(final int counter) {
        final List<Block> blocks = this.blocks;
        final List<Material> masks = this.masks;
        final Consumer<Block> action = this.action;

        new BukkitRunnable() {
            @Override
            public final void run() {
                int blocksAffected = 0;
                final boolean noMask = masks.size() == 0;

                for (int index = counter; index < blocks.size(); index++) {
                    final Block block = blocks.get(index);
                    if (noMask || masks.contains(block.getType())) {
                        affected.put(block.getLocation(), new Pair<>(block.getType(), block.getBlockData()));

                        action.accept(block);
                    }

                    if (++blocksAffected == Action.blockLimit) {
                        runNewTask(counter + blocksAffected);
                        break;
                    }
                }
            }
        }.runTaskLater(Action.plugin, 1);
    }

    public final void undo() {
        for (final Location location : this.affected.keySet()) {
            final World world = location.getWorld();
            if (world == null) {
                continue;
            }

            final Pair<Material, BlockData> data = this.affected.get(location);
            final Block selected = world.getBlockAt(location);
            selected.setType(data.getKey());
            selected.setBlockData(data.getValue());
        }
    }

    public final void redo() {
        this.runNewTask(0);
    }
}

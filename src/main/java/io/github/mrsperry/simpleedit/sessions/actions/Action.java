package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.SimpleEdit;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Action {
    private static final JavaPlugin plugin = SimpleEdit.getInstance();
    private static final int blockLimit = Action.plugin.getConfig().getInt("affected-blocks-per-tick", 3000);

    private List<Block> blocks;
    private List<Material> masks;
    private Consumer<Block> action;
    private Map<Location, Pair<Material, BlockData>> affected;

    protected void run(final SelectionHistory history, final List<Block> blocks, final Consumer<Block> action) {
        this.run(history, blocks, new ArrayList<>(), action);
    }

    protected void run(final SelectionHistory history, final List<Block> blocks, final List<Material> masks, final Consumer<Block> action) {
        this.blocks = blocks;
        this.masks = masks;
        this.action = action;
        this.affected = new HashMap<>();

        this.runNewTask(0);
        history.record(this);
    }

    protected List<Material> getMaterialWeights(final List<Pair<Material, Integer>> materials) {
        final List<Material> weights = new ArrayList<>();
        for (final Pair<Material, Integer> weight : materials) {
            for (int index = 0; index < weight.getValue(); index++) {
                weights.add(weight.getKey());
            }
        }

        return weights;
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

    public void undo() {
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

    public void redo() {
        this.runNewTask(0);
    }
}

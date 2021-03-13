package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.simpleedit.SimpleEdit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Action {
    private static final JavaPlugin plugin = SimpleEdit.getInstance();
    private static final int blockLimit = Action.plugin.getConfig().getInt("affected-blocks-per-tick", 3000);

    private List<Block> blocks;
    private List<Material> masks;
    private Consumer<Block> action;

    protected void run(final ArrayList<Block> blocks, final Consumer<Block> action) {
        this.run(blocks, null, action);
    }

    protected void run(final List<Block> blocks, final List<Material> masks, final Consumer<Block> action) {
        this.blocks = blocks;
        this.masks = masks;
        this.action = action;

        this.runNewTask(0);
    }

    private void runNewTask(final int counter) {
        final List<Block> blocks = this.blocks;
        final List<Material> masks = this.masks;
        final Consumer<Block> action = this.action;

        new BukkitRunnable() {
            @Override
            public final void run() {
                int blocksAffected = 0;
                final boolean checkMask = masks != null;

                for (int index = counter; index < blocks.size(); index++) {
                    final Block block = blocks.get(index);
                    if (checkMask && masks.contains(block.getType())) {
                        action.accept(blocks.get(index));
                    }

                    if (++blocksAffected == Action.blockLimit) {
                        runNewTask(counter + blocksAffected);
                        break;
                    }
                }
            }
        }.runTaskLater(Action.plugin, 1);
    }
}

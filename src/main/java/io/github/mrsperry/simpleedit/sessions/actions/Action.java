package io.github.mrsperry.simpleedit.sessions.actions;

import io.github.mrsperry.simpleedit.SimpleEdit;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Action {
    private static final JavaPlugin plugin = SimpleEdit.getInstance();
    private static final int blockLimit = Action.plugin.getConfig().getInt("affected-blocks-per-tick", 3000);

    protected void run(final ArrayList<Block> blocks, final Consumer<Block> consumer) {
        this.runNewTask(blocks, consumer, 0, 0);
    }

    private void runNewTask(final ArrayList<Block> blocks, final Consumer<Block> consumer, final int counter, final int delay) {
        new BukkitRunnable() {
            @Override
            public final void run() {
                int blocksAffected = 0;
                for (int index = counter; index < blocks.size(); index++) {
                    consumer.accept(blocks.get(index));

                    if (++blocksAffected == Action.blockLimit) {
                        runNewTask(blocks, consumer, counter + blocksAffected, 1);
                        break;
                    }
                }
            }
        }.runTaskLater(Action.plugin, delay);
    }
}

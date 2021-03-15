package io.github.mrsperry.simpleedit.sessions.selections;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SelectionClipboard {
    private final Selection selection;

    private Block[][][] blocks;
    private Location copyOffset;

    private int rotation;
    private ClipboardDirection flip;

    public SelectionClipboard(final Selection selection) {
        this.selection = selection;

        this.rotation = 0;
        this.flip = ClipboardDirection.None;
    }

    public final void copy(final Location location) {
        this.blocks = this.selection.getCubeSelectionArray();

        final int[] start = this.selection.getPosition().getStart();
        final int x = (int) Math.floor(location.getX()) - start[0];
        final int y = (int) Math.floor(location.getY()) - start[1];
        final int z = (int) Math.floor(location.getZ()) - start[2];

        this.copyOffset = new Location(location.getWorld(), x, y, z);
    }

    public final void paste(final Location location, final boolean ignoreAir) {
        final World world = this.selection.getPosition().getWorld();

        location.subtract(this.copyOffset);

        for (int x = 0; x < this.blocks.length; x++) {
            for (int y = 0;  y < this.blocks[0].length; y++) {
                for (int z = 0; z < this.blocks[0][0].length; z++) {
                    final Block block = this.blocks[x][y][z];
                    if (ignoreAir && block.getType().isAir()) {
                        continue;
                    }

                    final Block replace = world.getBlockAt(location.clone().add(x, y, z));
                    replace.setType(block.getType());
                    replace.setBlockData(block.getBlockData(), true);
                }
            }
        }
    }

    public final void rotate(final int amount) {
        this.rotation = amount;
    }

    public final void flip(final ClipboardDirection direction) {
        this.flip = direction;
    }
}

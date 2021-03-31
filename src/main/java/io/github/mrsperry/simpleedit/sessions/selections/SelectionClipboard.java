package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.sessions.actions.PasteAction;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class SelectionClipboard {
    private final Selection selection;

    private Block[][][] blocks;
    private Location initialOffset;
    private Location copyOffset;

    private boolean initialNorthSouth;
    private boolean initialEastWest;
    private int rotation;
    private ClipboardDirection.Cardinal flip;

    public SelectionClipboard(final Selection selection) {
        this.selection = selection;
    }

    public final void copy(final Location location) {
        this.blocks = this.selection.getCubeSelectionArray();

        final int[] start = this.selection.getPosition().getStart();
        final int x = (int) Math.floor(location.getX()) - start[0];
        final int y = (int) Math.floor(location.getY()) - start[1];
        final int z = (int) Math.floor(location.getZ()) - start[2];

        this.initialOffset = new Location(location.getWorld(), x, y, z);
        this.copyOffset = this.initialOffset.clone();

        this.initialNorthSouth = z >= 0;
        this.initialEastWest = x < 0;
        this.rotation = 0;
        this.flip = ClipboardDirection.Cardinal.None;
    }

    public final void paste(final Location location, final boolean ignoreAir) {
        PasteAction.run(this.selection.getHistory(), location.subtract(this.copyOffset), ignoreAir, this.blocks);
    }

    public final void rotate(final int amount) {
        final int length = this.blocks.length;
        final int height = this.blocks[0].length;
        final int width = this.blocks[0][0].length;

        final Block[][][] copy = new Block[width][height][length];
        for (int y = 0; y < this.blocks[0].length; y++) {
            for (int x = 0; x < length; x++) {
                for (int z = width - 1; z >= 0; z--) {
                    copy[width - z - 1][y][x] = this.blocks[x][y][z];
                }
            }
        }
        this.blocks = copy;

        this.rotation += 90;
        if (this.rotation >= 360) {
            this.rotation = 0;
        }
        this.copyOffset = this.initialOffset.clone();

        final boolean northEastOrSouthWest = (this.initialNorthSouth && this.initialEastWest) || (!this.initialNorthSouth && !this.initialEastWest);
        boolean modifyX = false, modifyZ = false;
        if (this.rotation == 90) {
            modifyX = !northEastOrSouthWest;
            modifyZ = northEastOrSouthWest;
        } else if (this.rotation == 180) {
            modifyX = true;
            modifyZ = true;
        } else if (this.rotation == 270) {
            modifyX = northEastOrSouthWest;
            modifyZ = !northEastOrSouthWest;
        }

        final boolean invert = length != width && this.rotation != 180;
        final double x = this.initialOffset.getX();
        final double z = this.initialOffset.getZ();
        if (modifyX) {
            this.copyOffset.setX(-(invert ? z : x) + width - 1);

            if (invert) {
                this.copyOffset.setZ(x);
            }
        }
        if (modifyZ) {
            this.copyOffset.setZ(-(invert ? x : z) + length - 1);

            if (invert) {
                this.copyOffset.setX(z);
            }
        }

        if (amount > 90) {
            this.rotate(amount - 90);
        }
    }

    public final void flip(final ClipboardDirection.Cardinal direction) {
        this.flip = direction;
    }
}

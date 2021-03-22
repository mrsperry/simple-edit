package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.sessions.actions.PasteAction;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class SelectionClipboard {
    private final Selection selection;

    private Block[][][] blocks;
    private Location copyOffset;

    private int rotation;
    private ClipboardDirection.Cardinal flip;

    public SelectionClipboard(final Selection selection) {
        this.selection = selection;

        this.rotation = 0;
        this.flip = ClipboardDirection.Cardinal.None;
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
        PasteAction.run(this.selection.getHistory(), location.subtract(this.copyOffset), ignoreAir, this.blocks);
    }

    public final void rotate(final int amount) {
        this.rotation = amount;
    }

    public final void flip(final ClipboardDirection.Cardinal direction) {
        this.flip = direction;
    }
}

package io.github.mrsperry.simpleedit.sessions.selections;

import org.bukkit.Location;
import org.bukkit.World;

public final class SelectionPosition {
    private final Selection selection;

    private World world;
    private Location pos1;
    private Location pos2;
    private int[] start;
    private int[] end;

    public SelectionPosition(final Selection selection) {
        this.selection = selection;
    }

    public final void setPosition(final boolean firstPosition, final Location location) {
        if (firstPosition) {
            this.pos1 = location;
        } else {
            this.pos2 = location;
        }
        this.world = location.getWorld();

        if (this.pos1 != null && this.pos2 != null) {
            final int x1 = this.pos1.getBlockX();
            final int x2 = this.pos2.getBlockX();
            final int y1 = this.pos1.getBlockY();
            final int y2 = this.pos2.getBlockY();
            final int z1 = this.pos1.getBlockZ();
            final int z2 = this.pos2.getBlockZ();

            this.start = new int[]{Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2)};
            this.end = new int[]{Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2)};
        }

        this.selection.getOutline().refresh();
    }

    public boolean checkLocationPrerequisites() {
        return this.pos1 == null
                || this.pos2 == null
                || this.world == null
                || this.pos1.getWorld() != this.pos2.getWorld();
    }

    public final World getWorld() {
        return this.world;
    }

    public final Location getPos1() {
        return this.pos1;
    }

    public final Location getPos2() {
        return this.pos2;
    }

    public final int[] getStart() {
        return this.start;
    }

    public final int[] getEnd() {
        return this.end;
    }
}

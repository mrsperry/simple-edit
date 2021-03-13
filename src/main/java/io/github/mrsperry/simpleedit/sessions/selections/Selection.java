package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.SimpleEdit;
import io.github.mrsperry.simpleedit.Utils;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Selection {
    private final SelectionPosition position;
    private final SelectionOutline outline;

    public Selection() {
        this.position = new SelectionPosition(this);
        this.outline = new SelectionOutline(this, SimpleEdit.getInstance().getConfig().getLong("outline-update-rate", 10));
    }

    public final List<Block> getCubeSelection() {
        final List<Block> blocks = new ArrayList<>();
        if (this.position.checkLocationPrerequisites()) {
            return blocks;
        }

        this.runSelectionConsumer((int[] coords) -> blocks.add(this.position.getWorld().getBlockAt(coords[0], coords[1], coords[2])));

        return blocks;
    }

    public final List<Block> getEdgeSelection() {
        final List<Block> blocks = new ArrayList<>();
        if (this.position.checkLocationPrerequisites()) {
            return blocks;
        }

        final int[] start = this.position.getStart();
        final int[] end = this.position.getEnd();

        this.runSelectionConsumer((final int[] coords) -> {
            final boolean xEdge = (coords[0] == start[0] || coords[0] == end[0]);
            final boolean yEdge = (coords[1] == start[1] || coords[1] == end[1]);
            final boolean zEdge = (coords[2] == start[2] || coords[2] == end[2]);

            // Only add blocks along the edges of the cube
            if ((xEdge && yEdge) || (yEdge && zEdge) || (xEdge && zEdge)) {
                blocks.add(this.position.getWorld().getBlockAt(coords[0], coords[1], coords[2]));
            }
        });

        return blocks;
    }

    private void runSelectionConsumer(final Consumer<int[]> consumer) {
        final int[] start = this.position.getStart();
        final int[] end = this.position.getEnd();

        for (int x = start[0]; x <= end[0]; x++) {
            for (int y = start[1]; y <= end[1]; y++) {
                for (int z = start[2]; z <= end[2]; z++) {
                    consumer.accept(new int[] { x, y, z });
                }
            }
        }
    }

    public final String serialize() {
        return Utils.locationString(this.position.getPos1()) + "|" + Utils.locationString(this.position.getPos2());
    }

    public static Selection deserialize(final String data) {
        final Selection selection = new Selection();
        final String[] positions = data.split("\\|");

        selection.position.setPosition(true, Utils.parseLocation(positions[0]));
        selection.position.setPosition(false, Utils.parseLocation(positions[1]));

        return selection;
    }

    public final SelectionPosition getPosition() {
        return this.position;
    }

    public final SelectionOutline getOutline() {
        return this.outline;
    }
}

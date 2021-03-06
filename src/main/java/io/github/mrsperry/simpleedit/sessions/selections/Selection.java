package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.SimpleEdit;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Selection {
    private final SelectionClipboard clipboard;
    private final SelectionHistory history;
    private final SelectionPosition position;
    private final SelectionOutline outline;

    public Selection() {
        this.clipboard = new SelectionClipboard(this);
        this.history = new SelectionHistory();
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

    public final Block[][][] getCubeSelectionArray() {
        final SelectionPosition position = this.getPosition();
        final int[] start = position.getStart();
        final int[] end = position.getEnd();

        final int xLength = end[0] - start[0] + 1;
        final int yLength = end[1] - start[1] + 1;
        final int zLength = end[2] - start[2] + 1;
        final Block[][][] blocks = new Block[xLength][yLength][zLength];
        if (position.checkLocationPrerequisites()) {
            return blocks;
        }

        this.runSelectionConsumer((int[] coords) -> {
            final Block block = position.getWorld().getBlockAt(coords[0], coords[1], coords[2]);
            blocks[coords[0] - start[0]][coords[1] - start[1]][coords[2] - start[2]] = block;
        });

        return blocks;
    }

    public final List<Block> getFaceSelection() {
        return this.getFaceSelection(null);
    }

    public final List<Block> getFaceSelection(final Predicate<boolean[]> predicate) {
        final List<Block> blocks = new ArrayList<>();
        if (this.position.checkLocationPrerequisites()) {
            return blocks;
        }

        final int[] start = this.position.getStart();
        final int[] end = this.position.getEnd();

        this.runSelectionConsumer((final int[] coords) -> {
            final boolean xFace = (coords[0] == start[0] || coords[0] == end[0]);
            final boolean yFace = (coords[1] == start[1] || coords[1] == end[1]);
            final boolean zFace = (coords[2] == start[2] || coords[2] == end[2]);

            if (xFace || yFace || zFace) {
                final boolean[] faces = new boolean[]{xFace, yFace, zFace};

                if (predicate == null || predicate.test(faces)) {
                    blocks.add(this.position.getWorld().getBlockAt(coords[0], coords[1], coords[2]));
                }
            }
        });

        return blocks;
    }

    public final List<Block> getWallSelection() {
        return this.getFaceSelection((final boolean[] faces) -> !faces[1] || (faces[0] || faces[2]));
    }

    public final List<Block> getEdgeSelection() {
        return this.getFaceSelection((final boolean[] faces) -> {
            final boolean xFace = faces[0];
            final boolean yFace = faces[1];
            final boolean zFace = faces[2];

            return (xFace && yFace) || (yFace && zFace) || (xFace && zFace);
        });
    }

    private void runSelectionConsumer(final Consumer<int[]> consumer) {
        final int[] start = this.position.getStart();
        final int[] end = this.position.getEnd();

        for (int x = start[0]; x <= end[0]; x++) {
            for (int y = start[1]; y <= end[1]; y++) {
                for (int z = start[2]; z <= end[2]; z++) {
                    consumer.accept(new int[]{x, y, z});
                }
            }
        }
    }

    public final String serialize() {
        return this.position.serialize() + "," + this.outline.serialize();
    }

    public static Selection deserialize(final String data) {
        final Selection selection = new Selection();

        for (String part : data.split("},")) {
            try {
                final String type = part.split("\\{")[0];
                final String[] contents = part.substring(type.length() + 1).replace("}", "").split(";");

                switch (type) {
                    case "positions":
                        SelectionPosition.deserialize(selection.getPosition(), contents);
                        break;
                    case "outline":
                        SelectionOutline.deserialize(selection.getOutline(), contents);
                        break;
                }
            } catch (final Exception ex) {
                return null;
            }
        }

        return selection;
    }

    public final SelectionClipboard getClipboard() {
        return this.clipboard;
    }

    public final SelectionHistory getHistory() {
        return this.history;
    }

    public final SelectionPosition getPosition() {
        return this.position;
    }

    public final SelectionOutline getOutline() {
        return this.outline;
    }
}

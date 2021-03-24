package io.github.mrsperry.simpleedit.sessions.selections;

import java.util.ArrayList;
import java.util.List;

public final class ClipboardDirection {
    public static Cardinal getDirection(final double pitch, final double yaw) {
        if (pitch < -45) {
            return Cardinal.Up;
        } else if (pitch > 45) {
            return Cardinal.Down;
        }

        // Facing negative X
        if (yaw > 45 && yaw <= 135) {
            return Cardinal.West;
        // Facing negative Z
        } else if (yaw > 135 && yaw <= 225) {
            return Cardinal.North;
        // Facing positive X
        } else if (yaw > 225 && yaw <= 315) {
            return Cardinal.East;
        // Facing positive Z
        } else {
            return Cardinal.South;
        }
    }

    public static List<String> getCardinalNames() {
        final List<String> cardinals = new ArrayList<>();
        for (final Cardinal cardinal : Cardinal.values()) {
            cardinals.add(cardinal.toString().toLowerCase());
        }

        return cardinals;
    }

    public enum Cardinal {
        North,
        South,
        East,
        West,
        Up,
        Down,
        None
    }
}

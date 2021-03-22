package io.github.mrsperry.simpleedit.sessions.selections;

public final class ClipboardDirection {
    public enum Cardinal {
        North,
        South,
        East,
        West,
        None
    }

    public static Cardinal getDirection(final double yaw) {
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
}

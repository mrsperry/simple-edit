package io.github.mrsperry.simpleedit.sessions;

import io.github.mrsperry.simpleedit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class Selection {
    private Location pos1;
    private Location pos2;

    public final void setPosition(final boolean firstPosition, final Location location) {
        if (firstPosition) {
            this.pos1 = location;
        } else {
            this.pos2 = location;
        }
    }

    public final String serialize() {
        return Utils.locationString(this.pos1) + "|" + Utils.locationString(this.pos2);
    }

    public static Selection deserialize(final String data) {
        final Selection selection = new Selection();
        final String[] positions = data.split("\\|");

        selection.setPosition(true, Selection.parseLocation(positions[0]));
        selection.setPosition(false, Selection.parseLocation(positions[1]));

        return selection;
    }

    private static Location parseLocation(final String string) {
        final String[] args = string.split(",");

        try {
            return new Location(
                    Bukkit.getWorld(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]));
        } catch (final Exception ex) {
            return null;
        }
    }
}

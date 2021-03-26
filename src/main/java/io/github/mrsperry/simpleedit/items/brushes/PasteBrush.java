package io.github.mrsperry.simpleedit.items.brushes;

import io.github.mrsperry.simpleedit.items.Brushes;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.PasteAction;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PasteBrush extends Brush {
    private final Selection selection;
    private final boolean ignoreAir;

    private PasteBrush(final Player player, final boolean ignoreAir) {
        super(player);

        this.selection = SessionManager.getSession(player.getUniqueId()).getSelection();
        this.ignoreAir = ignoreAir;
    }

    @Override
    public void run(final Location location) {
        PasteAction.run(this.selection.getHistory(), location.clone().add(0, 1, 0), this.ignoreAir, this.selection.getCubeSelectionArray());
    }

    public static void create(final Player player, final boolean ignoreAir) {
        Brushes.getInstance().addBrush(new PasteBrush(player, ignoreAir));
    }
}

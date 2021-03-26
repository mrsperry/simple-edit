package io.github.mrsperry.simpleedit.items.brushes;

import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Brush {
    private final Player owner;
    private final Selection selection;
    private final ItemStack item;

    public Brush(final Player owner) {
        this.owner = owner;
        this.selection = SessionManager.getSession(owner.getUniqueId()).getSelection();
        this.item = owner.getInventory().getItemInMainHand();
    }

    public abstract void run(final Location location);

    public final Player getOwner() {
        return this.owner;
    }

    public final Selection getSelection() {
        return this.selection;
    }

    public final ItemStack getItem() {
        return this.item;
    }
}

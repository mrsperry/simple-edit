package io.github.mrsperry.simpleedit.items.brushes;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.items.Brushes;
import io.github.mrsperry.simpleedit.sessions.actions.CylAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public final class CylBrush extends Brush {
    private final int radius;
    private final List<Material> masks;
    private final List<Pair<Material, Integer>> materials;

    private CylBrush(final Player player, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        super(player);

        this.radius = radius;
        this.masks = masks;
        this.materials = materials;
    }

    @Override
    public void run(final Location location) {
        CylAction.run(this.getSelection().getHistory(), location.add(0, 1, 0), this.radius, this.masks, this.materials);
    }

    public static void create(final Player player, final int radius, final List<Material> masks, final List<Pair<Material, Integer>> materials) {
        Brushes.getInstance().addBrush(new CylBrush(player, radius, masks, materials));
    }
}

package io.github.mrsperry.simpleedit.items;

import io.github.mrsperry.simpleedit.items.brushes.Brush;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Brushes implements Listener {
    private static Brushes instance;

    private final Map<Player, Set<Brush>> brushes;

    public Brushes() {
        Brushes.instance = this;

        this.brushes = new HashMap<>();
    }

    public static Brushes getInstance() {
        return Brushes.instance;
    }

    public void addBrush(final Brush brush) {
        final Player player = brush.getOwner();

        final Set<Brush> brushes = this.brushes.getOrDefault(player, new HashSet<>());
        brushes.removeIf(current -> current.getItem().equals(brush.getItem()));
        brushes.add(brush);

        this.brushes.put(player, brushes);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Player player = event.getPlayer();
        for (final Player owner : this.brushes.keySet()) {
            if (player != owner) {
                continue;
            }
            final ItemStack stack = owner.getInventory().getItemInMainHand();
            final Set<Brush> brushes = this.brushes.get(owner);
            Brush brush = null;

            for (final Brush current : brushes) {
                if (!current.getItem().equals(stack)) {
                    continue;
                }

                brush = current;
                break;
            }

            if (brush == null) {
                return;
            }

            Block target = null;
            final Action action = event.getAction();

            if (action == Action.RIGHT_CLICK_AIR) {
                target = player.getTargetBlockExact(150, FluidCollisionMode.NEVER);
            } else if (action == Action.RIGHT_CLICK_BLOCK) {
                target = event.getClickedBlock();
            }

            if (target == null) {
                return;
            }

            brush.run(target.getLocation());
            event.setCancelled(true);
        }
    }
}

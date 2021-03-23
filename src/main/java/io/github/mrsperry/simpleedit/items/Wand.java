package io.github.mrsperry.simpleedit.items;

import io.github.mrsperry.simpleedit.SimpleEdit;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import java.util.logging.Logger;

public final class Wand implements Listener {
    private static Material wandMaterial;
    private final boolean creativeOnly;

    public Wand(final SimpleEdit plugin) {
        final FileConfiguration config = plugin.getConfig();
        final Logger logger = plugin.getLogger();

        Wand.wandMaterial = Material.WOODEN_AXE;
        final String material = config.getString("wand-material", "wooden axe");
        try {
            if (material != null) {
                Wand.wandMaterial = Material.valueOf(material.toUpperCase().replaceAll(" ", "_"));
            }
        } catch (final IllegalArgumentException ex) {
            logger.severe("Could not parse wand material: " + material);
            logger.warning("The default material (wooden axe) will be used instead");
        }

        this.creativeOnly = config.getBoolean("creative-only-wand", true);
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        final Action action = event.getAction();
        if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final boolean isFirstPosition = action == Action.LEFT_CLICK_BLOCK;

        final Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Wand.wandMaterial) {
            return;
        }

        if (this.creativeOnly && player.getGameMode() != GameMode.CREATIVE) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final Location location = block.getLocation();

        final Selection selection = SessionManager.getSession(player.getUniqueId()).getSelection();
        selection.getPosition().setPosition(isFirstPosition, location);

        player.sendMessage(Wand.getMessage(isFirstPosition, location, selection.getCubeSelection().size()));

        event.setCancelled(true);
    }

    public static String getMessage(final boolean isFirstPosition, final Location location, final int affected) {
        final String position = isFirstPosition ? "First" : "Second";
        String message = position + " position set to (" + Utils.coordinateString(location) + ")";

        if (affected != 0) {
            message += " (" + Utils.formatNumber(affected) + ")";
        }

        return ChatColor.LIGHT_PURPLE + message;
    }

    public static Material getWandMaterial() {
        return Wand.wandMaterial;
    }
}

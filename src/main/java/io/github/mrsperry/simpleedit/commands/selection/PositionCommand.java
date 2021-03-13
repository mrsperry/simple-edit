package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public final class PositionCommand {
    public static void onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        final boolean isFirstPosition = args[0].equalsIgnoreCase("pos1");

        if (args.length != 1) {
            SimpleEditCommands.tooManyArguments(sender, isFirstPosition ? "pos1" : "pos2");
            return;
        }

        final Player player = (Player) sender;
        final UUID id = player.getUniqueId();
        final Location location = player.getLocation();

        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getPosition().setPosition(isFirstPosition, location);

        final String position = isFirstPosition ? "First" : "Second";
        player.sendMessage(ChatColor.LIGHT_PURPLE + position + " position set to (" + Utils.coordinateString(location) + ")");
    }
}

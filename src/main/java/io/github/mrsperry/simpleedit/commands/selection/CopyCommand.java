package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CopyCommand {
    private static final String usage = "copy";

    public static void onCommand(final CommandSender sender, final int argsLength) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        if (argsLength > 1) {
            SimpleEditCommands.tooManyArguments(sender, CopyCommand.usage);
            return;
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getClipboard().copy(player.getLocation());

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection copied");
    }
}

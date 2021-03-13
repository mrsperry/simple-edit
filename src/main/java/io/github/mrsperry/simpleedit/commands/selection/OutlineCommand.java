package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class OutlineCommand {
    private static final String usage = "outline";

    public static void onCommand(final CommandSender sender, final int argsLength) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        if (argsLength != 1) {
            SimpleEditCommands.tooManyArguments(sender, OutlineCommand.usage);
            return;
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        final boolean enabled = session.getSelection().toggleDrawOutline();

        player.sendMessage(ChatColor.GRAY + "Selection outline " + (enabled ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
    }
}

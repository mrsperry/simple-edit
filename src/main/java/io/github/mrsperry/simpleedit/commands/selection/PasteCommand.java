package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PasteCommand {
    private static final String usage = "paste [-a]";

    public static void onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        boolean ignoreAir = false;
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("-a")) {
                ignoreAir = true;
            } else {
                SimpleEditCommands.invalidArgument(sender, PasteCommand.usage, args[1]);
            }
        } else if (args.length > 2) {
            SimpleEditCommands.tooManyArguments(sender, PasteCommand.usage);
            return;
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getClipboard().paste(player.getLocation(), ignoreAir);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection pasted");
    }
}

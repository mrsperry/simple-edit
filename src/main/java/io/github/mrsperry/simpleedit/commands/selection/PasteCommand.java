package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.ICommandHandler;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PasteCommand extends ICommandHandler {
    public PasteCommand() {
        super("paste [-a]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        boolean ignoreAir = false;
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("-a")) {
                ignoreAir = true;
            } else {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getClipboard().paste(player.getLocation(), ignoreAir);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection pasted");
    }
}

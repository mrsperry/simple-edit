package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class RotateCommand extends BaseCommand {
    public RotateCommand() {
        super("rotate <degrees>");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 1, 1)) {
            return;
        }

        int degrees = 0;
        try {
            degrees = Integer.parseInt(args[1]);
        } catch (final NumberFormatException ex) {
            SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[1]);
            return;
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getClipboard().rotate(degrees);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection rotated by " + degrees + " degrees");
    }
}

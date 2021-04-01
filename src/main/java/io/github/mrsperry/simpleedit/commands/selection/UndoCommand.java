package io.github.mrsperry.simpleedit.commands.selection;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class UndoCommand extends BaseCommand {
    public UndoCommand() {
        super("undo");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (this.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (final Exception ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }
        }

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Amount must be greater than zero");
            SimpleEditCommands.usage(sender, this.getUsage());
            return;
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        if (session.getSelection().getHistory().undo(amount)) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Undid " + amount + " action" + (amount == 1 ? "" : "s"));
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Nothing left to undo");
        }
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("amount #");
        }

        return super.onTabComplete(args);
    }
}

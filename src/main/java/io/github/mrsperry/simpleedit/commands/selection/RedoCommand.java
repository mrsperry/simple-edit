package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class RedoCommand extends BaseCommand {
    public RedoCommand() {
        super("undo");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (this.commandPrerequisites(sender, args)) {
            return;
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        if (session.getSelection().getHistory().redo()) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Action redone");
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Nothing left to redo");
        }
    }
}
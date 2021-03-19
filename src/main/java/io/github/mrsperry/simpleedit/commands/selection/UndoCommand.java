package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.ICommandHandler;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class UndoCommand extends ICommandHandler {
    public UndoCommand() {
        super("undo");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (this.commandPrerequisites(sender, args)) {
            return;
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        if (session.getSelection().getHistory().undo()) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Action undone");
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Nothing left to undo");
        }
    }
}

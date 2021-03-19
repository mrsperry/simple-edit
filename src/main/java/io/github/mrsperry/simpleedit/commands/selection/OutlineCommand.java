package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class OutlineCommand extends BaseCommand {
    public OutlineCommand() {
        super("outline");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args)) {
            return;
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        final boolean enabled = session.getSelection().getOutline().toggle();

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection outline " + (enabled ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
    }
}

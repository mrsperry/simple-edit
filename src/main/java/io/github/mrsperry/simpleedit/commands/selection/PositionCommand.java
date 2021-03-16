package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.ICommandHandler;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PositionCommand extends ICommandHandler {
    public PositionCommand() {
        super("pos<1 | 2>");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args)) {
            return;
        }

        final boolean isFirstPosition = args[0].equalsIgnoreCase("pos1");
        final Player player = (Player) sender;
        final Location location = player.getLocation();

        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getPosition().setPosition(isFirstPosition, location);

        final String position = isFirstPosition ? "First" : "Second";
        player.sendMessage(ChatColor.LIGHT_PURPLE + position + " position set to (" + Utils.coordinateString(location) + ")");
    }
}

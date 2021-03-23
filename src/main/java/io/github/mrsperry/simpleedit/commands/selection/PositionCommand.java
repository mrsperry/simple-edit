package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.items.Wand;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PositionCommand extends BaseCommand {
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

        final Selection selection = SessionManager.getSession(player.getUniqueId()).getSelection();
        selection.getPosition().setPosition(isFirstPosition, location);

        player.sendMessage(Wand.getMessage(isFirstPosition, location, selection.getCubeSelection().size()));
    }
}

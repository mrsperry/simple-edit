package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.DrainAction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class DrainCommand extends BaseCommand {
    public DrainCommand() {
        super("drain <radius> [-l]");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 1, 2)) {
            return;
        }

        final int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (final IllegalArgumentException ex) {
            SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
            return;
        }

        boolean waterlogged = false;
        if (args.length == 3) {
            if (args[2].equalsIgnoreCase("-l")) {
                waterlogged = true;
            } else {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[2]);
            }
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        DrainAction.run(session.getSelection().getHistory(), player.getLocation(), radius, waterlogged);
    }

    @Override
    protected List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("radius #");
        } else if (args.length == 3) {
            return Lists.newArrayList("-l");
        }

        return super.onTabComplete(args);
    }
}

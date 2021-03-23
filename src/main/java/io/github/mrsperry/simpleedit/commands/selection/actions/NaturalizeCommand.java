package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.NaturalizeAction;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public final class NaturalizeCommand extends BaseCommand {
    public NaturalizeCommand() {
        super("naturalize [-s]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        boolean onlySkyBlocks = true;
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("-s")) {
                onlySkyBlocks = false;
            } else {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        NaturalizeAction.run(session.getSelection(), onlySkyBlocks);
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("-s");
        }

        return super.onTabComplete(args);
    }
}
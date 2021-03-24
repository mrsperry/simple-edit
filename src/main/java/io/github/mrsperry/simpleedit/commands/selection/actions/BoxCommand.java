package io.github.mrsperry.simpleedit.commands.selection.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.BoxAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class BoxCommand extends BaseCommand {
    public BoxCommand() {
        super("box [-h] [chance%]<material> [materials...]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 1, -1)) {
            return;
        }

        int startIndex = 1;
        boolean hollow = false;
        if (args[1].equalsIgnoreCase("-h")) {
            hollow = true;
            startIndex = 2;

            if (args.length == 2) {
                SimpleEditCommands.tooFewArguments(sender, this.getUsage());
                return;
            }
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = startIndex; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[index]);
                return;
            }

            materials.add(material);
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        BoxAction.run(session.getSelection(), hollow, materials);
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length > 1) {
            final List<String> complete = Utils.getMaterialChanceTabComplete(args[args.length - 1]);
            if (args.length == 2) {
                complete.add("-h");
            }

            return complete;
        }

        return super.onTabComplete(args);
    }
}

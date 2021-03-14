package io.github.mrsperry.simpleedit.commands.selection.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.BoxAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class BoxCommand {
    private static final String usage = "box [-h] [chance%]<material> [materials...]";

    public static void onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        if (args.length == 1) {
            SimpleEditCommands.tooFewArguments(sender, BoxCommand.usage);
            return;
        }

        int startIndex = 1;
        boolean hollow = false;
        if (args[1].equalsIgnoreCase("-h")) {
            if (args.length == 2) {
                SimpleEditCommands.tooFewArguments(sender, BoxCommand.usage);
                return;
            }

            hollow = true;
            startIndex = 2;
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = startIndex; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, BoxCommand.usage, args[index]);
                return;
            }

            materials.add(material);
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        BoxAction.run(session.getSelection(), hollow, materials);
    }

    public static List<String> onTabComplete(final String[] args) {
        if (args.length > 1) {
            return Utils.getMaterialChanceTabComplete(args[args.length - 1]);
        }

        return null;
    }
}

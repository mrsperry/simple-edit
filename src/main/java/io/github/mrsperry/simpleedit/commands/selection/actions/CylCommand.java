package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.ICommandHandler;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.CylAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class CylCommand extends ICommandHandler {
    public CylCommand() {
        super("cyl <radius> [-h height] [chance%]<material> [materials...]");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 2, -1)) {
            return;
        }

        final int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (final NumberFormatException ex) {
            SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
            return;
        }

        int height = 1;
        final boolean checkHeight = args[2].equalsIgnoreCase("-h");
        if (checkHeight) {
            if (args.length >= 4) {
                try {
                    height = Integer.parseInt(args[3]);
                } catch (final NumberFormatException ex) {
                    SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[3]);
                    return;
                }
            } else {
                SimpleEditCommands.tooFewArguments(sender, this.getUsage());
                return;
            }
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = checkHeight ? 4 : 2; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[index]);
                return;
            }

            materials.add(material);
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        CylAction.run(session.getSelection().getHistory(), player.getLocation(), radius, height, materials);
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("radius #");
        } else if (args.length > 2) {
            final List<String> materials = Utils.getMaterialChanceTabComplete(args[args.length - 1]);

            if (args.length == 3) {
                final List<String> copy = Lists.newArrayList(materials);
                copy.add("-h");

                return copy;
            } else if (args.length == 4 && args[2].equalsIgnoreCase("-h")) {
                return Lists.newArrayList("height #");
            } else {
                return materials;
            }
        }

        return super.onTabComplete(args);
    }
}

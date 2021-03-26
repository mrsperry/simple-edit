package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.SphereAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class SphereCommand extends BaseCommand {
    public SphereCommand() {
        super("sphere [-h] <radius> [chance%]<material> [materials...]");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 2, -1)) {
            return;
        }

        boolean hollow = false;
        if (args[1].equalsIgnoreCase("-h")) {
            hollow = true;
        }

        final int radius;
        final String radiusString = hollow ? args[2] : args[1];
        try {
            radius = Integer.parseInt(radiusString);
        } catch (final NumberFormatException ex) {
            SimpleEditCommands.invalidArgument(sender, this.getUsage(), radiusString);
            return;
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = hollow ? 3 : 2; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[index]);
                return;
            }

            materials.add(material);
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        SphereAction.run(session.getSelection().getHistory(), player.getLocation(), hollow, radius, materials);
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        final List<String> materials = Utils.getMaterialChanceTabComplete(args[args.length - 1]);

        if (args.length == 2) {
            return Lists.newArrayList("radius #", "-h");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("-h")) {
                return Lists.newArrayList("radius #");
            } else {
                return materials;
            }
        }

        return materials;
    }
}

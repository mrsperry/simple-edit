package io.github.mrsperry.simpleedit.commands.selection;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.actions.ReplaceNearAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

public final class ReplaceNearCommand {
    private static final String usage = "replacenear <radius> <material[,material...]> [chance%]<material> [materials...]";

    public static void onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        if (args.length < 4) {
            SimpleEditCommands.tooFewArguments(sender, ReplaceNearCommand.usage);
            return;
        }

        final int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (final NumberFormatException ex) {
            SimpleEditCommands.invalidArgument(sender, ReplaceNearCommand.usage, args[1]);
            return;
        }

        final List<Material> masks = new ArrayList<>();
        for (final String mask : args[2].split(",")) {
            try {
                masks.add(Material.valueOf(mask.toUpperCase()));
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, ReplaceNearCommand.usage, args[2]);
                return;
            }
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = 3; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, ReplaceNearCommand.usage, args[index]);
                return;
            }

            materials.add(material);
        }

        ReplaceNearAction.run(((Player) sender).getLocation(), radius, masks, materials);
    }

    public static List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("radius");
        } else if (args.length == 3) {
            final String[] split = args[1].split(",");
            return StringUtil.copyPartialMatches(split[split.length - 1], Utils.getMaterialStrings(), new ArrayList<>());
        } else {
            return Utils.getMaterialChanceTabComplete(args[args.length - 1]);
        }
    }
}

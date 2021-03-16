package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.ICommandHandler;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.actions.ReplaceNearAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

public final class ReplaceNearCommand extends ICommandHandler {
    public ReplaceNearCommand() {
        super("replacenear <radius> <material[,material...]> [chance%]<material> [materials...]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 3, -1)) {
            return;
        }

        final int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (final NumberFormatException ex) {
            SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[1]);
            return;
        }

        final List<Material> masks = new ArrayList<>();
        for (final String mask : args[2].split(",")) {
            try {
                masks.add(Material.valueOf(mask.toUpperCase()));
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[2]);
                return;
            }
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = 3; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[index]);
                return;
            }

            materials.add(material);
        }

        ReplaceNearAction.run(((Player) sender).getLocation(), radius, masks, materials);
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("radius #");
        } else if (args.length > 2) {
            return Utils.getMaterialChanceTabComplete(args[args.length - 1]);
        }

        return super.onTabComplete(args);
    }
}

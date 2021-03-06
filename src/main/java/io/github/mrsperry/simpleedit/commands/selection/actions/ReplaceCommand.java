package io.github.mrsperry.simpleedit.commands.selection.actions;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.ReplaceAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class ReplaceCommand extends BaseCommand {
    public ReplaceCommand() {
        super("replace <material[,material...]> [chance%]<material> [materials...]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 2, -1)) {
            return;
        }

        final List<Material> masks = new ArrayList<>();
        for (final String mask : args[1].split(",")) {
            try {
                masks.add(Material.valueOf(mask.toUpperCase()));
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[1]);
                return;
            }
        }

        final List<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = 2; index < args.length; index++) {
            final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
            if (material == null) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[index]);
                return;
            }

            materials.add(material);
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        ReplaceAction.run(session.getSelection(), masks, materials);
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            final String[] split = args[1].split(",");
            return StringUtil.copyPartialMatches(split[split.length - 1], Utils.getMaterialStrings(), new ArrayList<>());
        } else if (args.length > 2) {
            return Utils.getMaterialChanceTabComplete(args[args.length - 1]);
        }

        return super.onTabComplete(args);
    }
}

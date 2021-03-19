package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.ICommandHandler;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.FillAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public final class FillCommand extends ICommandHandler {
    public FillCommand() {
        super("fill <radius> [chance%]<material> [materials...]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 2, -1)) {
            return;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (final IllegalArgumentException ex) {
            SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
            return;
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

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        FillAction.run(session.getSelection().getHistory(), player.getLocation(), radius, materials);
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

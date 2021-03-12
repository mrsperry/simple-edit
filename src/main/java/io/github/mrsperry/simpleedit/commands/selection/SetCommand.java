package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.SetAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import java.util.ArrayList;

public final class SetCommand {
    public static void onCommand(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        final ArrayList<Pair<Material, Integer>> materials = new ArrayList<>();
        for (int index = 1; index < args.length; index++) {
            final String[] split = args[index].split("%");
            String materialString = args[index];

            int chance = (int) Math.floor(100f / (args.length - 1));
            if (split.length == 2) {
                try {
                    chance = Integer.parseInt(split[0]);
                } catch (final Exception ex) {
                    SimpleEditCommands.invalidArgument(sender, "set [chance%]<material> [materials...]", args[index]);
                }

                materialString = split[1];
            }

            final Material material;
            try {
                material = Material.valueOf(materialString.toUpperCase());
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, "set [chance%]<material> [materials...]", args[1]);
                return;
            }

            materials.add(new Pair<>(material, chance));
        }

        final Session session = SessionManager.getSession(((Player) sender).getUniqueId());
        SetAction.run(session.getSelection(), materials);
    }

    public static ArrayList<String> onTabComplete(final String[] args) {
        if (args.length >= 2) {
            final ArrayList<String> materials = new ArrayList<>();
            for (final Material material : Material.values()) {
                materials.add(material.toString().toLowerCase());
            }

            String current = args[args.length - 1];
            if (current.contains("%") && current.length() > current.indexOf("%")) {
                current = current.substring(current.indexOf("%") + 1);
            }

            return StringUtil.copyPartialMatches(current, materials, new ArrayList<>());
        }

        return null;
    }
}

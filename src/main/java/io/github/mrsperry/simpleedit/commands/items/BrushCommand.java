package io.github.mrsperry.simpleedit.commands.items;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.classes.Pair;
import io.github.mrsperry.mcutils.types.ToolTypes;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.items.Wand;
import io.github.mrsperry.simpleedit.items.brushes.PaintBrush;
import io.github.mrsperry.simpleedit.items.brushes.PasteBrush;
import io.github.mrsperry.simpleedit.items.brushes.SphereBrush;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class BrushCommand extends BaseCommand {
    public BrushCommand() {
        super("brush <action> [arguments...]");
    }

    @Override
    protected void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 1, -1)) {
            return;
        }

        final String command = args[1].toLowerCase();

        final Player player = (Player) sender;
        final ItemStack item = player.getInventory().getItemInMainHand();
        final Material type = item.getType();

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + Utils.capitalize(command) + " brush");
        final List<String> lore = new ArrayList<>();

        if (type == Wand.getWandMaterial()) {
            player.sendMessage(ChatColor.RED + "You may not create a brush with the same material as the selection wand!");
            return;
        } else if (!ToolTypes.getAllToolTypes().contains(type)) {
            player.sendMessage(ChatColor.RED + "A brush must be a tool (sword, pickaxe, axe, shovel, or hoe)");
            return;
        }

        if (command.equals("paste")) {
            boolean ignoreAir = false;
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase("-a")) {
                    ignoreAir = true;

                    lore.add(ChatColor.GOLD + "Ignores air blocks");
                } else {
                    SimpleEditCommands.invalidArgument(sender, "brush paste [-a]", args[2]);
                    return;
                }
            }

            PasteBrush.create(player, ignoreAir);
        } else if (command.equals("paint") || command.equals("sphere")) {
            final String usage = "brush " + command + " <radius> <mask>[,masks...] [chance%]<material> [materials...]";

            if (args.length < 5) {
                SimpleEditCommands.tooFewArguments(sender, usage);
                return;
            }

            final int radius;
            try {
                radius = Integer.parseInt(args[2]);
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, usage, args[2]);
                return;
            }
            lore.add(ChatColor.GOLD + "Radius: " + radius);

            final List<Material> masks = new ArrayList<>();
            StringBuilder masksString = new StringBuilder().append(ChatColor.YELLOW).append("Masks: ");
            for (final String mask : args[3].split(",")) {
                try {
                    masks.add(Material.valueOf(mask.toUpperCase()));
                    masksString.append(mask.replace("_", " ")).append(", ");
                } catch (final IllegalArgumentException ex) {
                    SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[1]);
                    return;
                }
            }
            lore.add(masksString.substring(0, masksString.length() - 2));

            final List<Pair<Material, Integer>> materials = new ArrayList<>();
            StringBuilder materialsString = new StringBuilder().append(ChatColor.GOLD).append("Materials: ");
            for (int index = 4; index < args.length; index++) {
                final Pair<Material, Integer> material = Utils.parseMaterialChance(args[index], args.length);
                if (material == null) {
                    SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[index]);
                    return;
                }

                materials.add(material);
                materialsString.append(material.getKey().toString().toLowerCase().replace("_", " ")).append(", ");
            }
            lore.add(materialsString.substring(0, materialsString.length() - 2));

            if (command.equals("paint")) {
                PaintBrush.create(player, radius, masks, materials);
            } else {
                SphereBrush.create(player, radius, masks, materials);
            }
        } else {
            SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
            return;
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Brush set to " + command);
    }

    @Override
    protected List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], Lists.newArrayList("paste", "paint", "sphere"), new ArrayList<>());
        }

        if (args[1].equalsIgnoreCase("paste")) {
            if (args.length == 3) {
                return StringUtil.copyPartialMatches(args[2], Lists.newArrayList("-a"), new ArrayList<>());
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("paste")) {
                return StringUtil.copyPartialMatches(args[2], Lists.newArrayList("-a"), new ArrayList<>());
            } else {
                return Lists.newArrayList("radius #");
            }
        } else if (args.length == 4) {
            if (!args[1].equalsIgnoreCase("paste")) {
                return StringUtil.copyPartialMatches(args[3], Utils.getMaterialStrings(), new ArrayList<>());
            }
        }

        return Utils.getMaterialChanceTabComplete(args[args.length - 1]);
    }
}

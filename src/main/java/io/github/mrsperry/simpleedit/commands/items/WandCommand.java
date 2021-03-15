package io.github.mrsperry.simpleedit.commands.items;

import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.items.Wand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class WandCommand {
    private static final String usage = "wand";

    public static void onCommand(final CommandSender sender, final int argsLength) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return;
        }

        if (argsLength != 1) {
            SimpleEditCommands.tooManyArguments(sender, WandCommand.usage);
            return;
        }

        ((Player) sender).getInventory().addItem(new ItemStack(Wand.getWandMaterial()));
    }
}

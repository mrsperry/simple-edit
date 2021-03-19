package io.github.mrsperry.simpleedit.commands.items;

import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.items.Wand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class WandCommand extends BaseCommand {
    public WandCommand() {
        super("wand");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 0)) {
            return;
        }

        ((Player) sender).getInventory().addItem(new ItemStack(Wand.getWandMaterial()));
    }
}

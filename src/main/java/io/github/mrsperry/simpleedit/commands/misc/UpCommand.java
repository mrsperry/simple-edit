package io.github.mrsperry.simpleedit.commands.misc;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class UpCommand extends BaseCommand {
    public UpCommand() {
        super("up [amount]");
    }

    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (final Exception ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }
        }

        final Player player = (Player) sender;
        if (amount < 0) {
            player.sendMessage(ChatColor.RED + "Amount cannot be negative");
            SimpleEditCommands.usage(sender, this.getUsage());
            return;
        }

        final Block current = player.getLocation().getBlock();

        int offset = 0;
        Block selected = current.getRelative(0, amount, 0);
        Block above = selected.getRelative(0, 1, 0);
        Block below = selected.getRelative(0, -1, 0);

        while (selected.getType().isSolid() || above.getType().isSolid()) {
            offset++;

            selected = selected.getRelative(0, 1, 0);
            above = selected.getRelative(0, 1, 0);
            below = selected.getRelative(0, -1, 0);
        }

        if (below.getType().isAir()) {
            below.setType(Material.GLASS);
        }

        amount += offset;
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Moved up " + amount + " block" + (amount != 1 ? "s" : ""));
        player.teleport(player.getLocation().clone().add(0, amount, 0));
    }

    @Override
    public List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("amount");
        }

        return super.onTabComplete(args);
    }
}

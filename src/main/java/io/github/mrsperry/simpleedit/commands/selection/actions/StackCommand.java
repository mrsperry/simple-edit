package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.PasteAction;
import io.github.mrsperry.simpleedit.sessions.selections.ClipboardDirection;
import io.github.mrsperry.simpleedit.sessions.selections.Selection;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionHistory;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionPosition;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class StackCommand extends BaseCommand {
    public StackCommand() {
        super("stack [-a] [amount] [direction]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 3)) {
            return;
        }

        final Player player = (Player) sender;
        final Location location = player.getLocation();

        int amount = 1;
        boolean ignoreAir = false;
        ClipboardDirection.Cardinal direction = ClipboardDirection.getDirection(location.getPitch(), location.getYaw());

        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("-a")) {
                ignoreAir = true;
            }

            final String amountString = ignoreAir ? args[2] : args[1];
            try {
                amount = Integer.parseInt(amountString);
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), amountString);
                return;
            }

            if (args.length >= 3) {
                final String directionString = ignoreAir ? args[3] : args[2];
                try {
                    direction = ClipboardDirection.Cardinal.valueOf(Utils.capitalize(directionString));
                } catch (final IllegalArgumentException ex) {
                    SimpleEditCommands.invalidArgument(sender, this.getUsage(), directionString);
                    return;
                }
            }
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Amount cannot be less than 1");
            SimpleEditCommands.usage(sender, this.getUsage());
            return;
        }

        final Session session = SessionManager.getSession(player.getUniqueId());
        final Selection selection = session.getSelection();
        final SelectionHistory history = selection.getHistory();
        final SelectionPosition position = selection.getPosition();

        final World world = position.getWorld();
        final int[] startCoords = position.getStart();
        final int[] endCoords = position.getEnd();
        final Location start = new Location(world, startCoords[0], startCoords[1], startCoords[2]);

        final Block[][][] blocks = selection.getCubeSelectionArray();

        int coord = 0;
        boolean negative = false;

        switch (direction) {
            case Up:
                coord = 1;
                break;
            case Down:
                coord = 1;
                negative = true;
                break;
            // -Z
            case North:
                coord = 2;
                negative = true;
                break;
            // +Z
            case South:
                coord = 2;
                break;
            // +X
            case East:
                coord = 0;
                break;
            // -X
            case West:
                coord = 0;
                negative = true;
                break;
        }

        int offset = 1 + (endCoords[coord] - startCoords[coord]);
        if (negative) {
            offset *= -1;
        }

        for (int index = 1; index < amount + 1; index++) {
            PasteAction.run(history, start.add(coord == 0 ? offset : 0, coord == 1 ? offset : 0, coord == 2 ? offset : 0), ignoreAir, blocks);
        }

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection stacked " + direction.toString().toLowerCase() + " " + amount + " time" + (amount != 1 ? "s" : ""));
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        final List<String> cardinals = ClipboardDirection.getCardinalNames();

        if (args.length == 2) {
            return Lists.newArrayList("amount", "-a");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("-a")) {
                return Lists.newArrayList("amount");
            } else {
                return StringUtil.copyPartialMatches(args[2], cardinals, new ArrayList<>());
            }
        } else if (args.length == 4) {
            return StringUtil.copyPartialMatches(args[3], cardinals, new ArrayList<>());
        }

        return super.onTabComplete(args);
    }
}
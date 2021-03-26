package io.github.mrsperry.simpleedit.commands.selection.actions;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.actions.StackAction;
import io.github.mrsperry.simpleedit.sessions.selections.ClipboardDirection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

        boolean ignoreAir = false;
        int amount = 1;
        ClipboardDirection.Cardinal direction = ClipboardDirection.getDirection(location.getPitch(), location.getYaw());

        String amountString = null;
        String directionString = null;

        if (args.length != 1) {
            if (args[1].equalsIgnoreCase("-a")) {
                ignoreAir = true;
            }
        }

        if (args.length == 2) {
            if (!ignoreAir) {
                amountString = args[1];
            }
        } else if (args.length == 3) {
            amountString = args[ignoreAir ? 2 : 1];

            if (!ignoreAir) {
                directionString = args[2];
            }
        } else if (args.length == 4) {
            if (!ignoreAir) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }

            amountString = args[2];
            directionString = args[3];
        }

        if (amountString != null) {
            try {
                amount = Integer.parseInt(amountString);
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), amountString);
                return;
            }
        }

        if (directionString != null) {
            try {
                direction = ClipboardDirection.Cardinal.valueOf(Utils.capitalize(directionString));
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), directionString);
                return;
            }
        }

        final Session session = SessionManager.getSession(player.getUniqueId());
        StackAction.run(session.getSelection(), ignoreAir, amount, direction);

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
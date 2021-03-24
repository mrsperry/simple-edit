package io.github.mrsperry.simpleedit.commands.selection;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.Utils;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.selections.ClipboardDirection;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionPosition;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class ScaleCommand extends BaseCommand {
    public ScaleCommand() {
        super("scale [amount] [direction]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 2)) {
            return;
        }

        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (final Exception ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }
        }

        final Player player = (Player) sender;
        final Location location = player.getLocation();

        ClipboardDirection.Cardinal direction = ClipboardDirection.getDirection(location.getPitch(), location.getYaw());
        if (args.length == 3) {
            try {
                direction = ClipboardDirection.Cardinal.valueOf(Utils.capitalize(args[2]));
            } catch (final IllegalArgumentException ex) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[2]);
                return;
            }
        }

        final SelectionPosition position = SessionManager.getSession(player.getUniqueId()).getSelection().getPosition();

        final Location pos1 = position.getPos1();
        final Location pos2 = position.getPos2();

        boolean isFirstFurthest;
        switch (direction) {
            case Up:
                isFirstFurthest = pos1.getY() > pos2.getY();
                position.setPosition(isFirstFurthest, (isFirstFurthest ? pos1 : pos2).clone().add(0, amount, 0));
                break;
            case Down:
                isFirstFurthest = pos1.getY() < pos2.getY();
                position.setPosition(isFirstFurthest, (isFirstFurthest ? pos1 : pos2).clone().subtract(0, amount, 0));
                break;
            // -Z
            case North:
                isFirstFurthest = pos1.getZ() < pos2.getZ();
                position.setPosition(isFirstFurthest, (isFirstFurthest ? pos1 : pos2).clone().subtract(0, 0, amount));
                break;
            // +Z
            case South:
                isFirstFurthest = pos1.getZ() > pos2.getZ();
                position.setPosition(isFirstFurthest, (isFirstFurthest ? pos1 : pos2).clone().add(0, 0, amount));
                break;
            // +X
            case East:
                isFirstFurthest = pos1.getX() > pos2.getX();
                position.setPosition(isFirstFurthest, (isFirstFurthest ? pos1 : pos2).clone().add(amount, 0, 0));
                break;
            // -X
            case West:
                isFirstFurthest = pos1.getX() < pos2.getX();
                position.setPosition(isFirstFurthest, (isFirstFurthest ? pos1 : pos2).clone().subtract(amount, 0, 0));
                break;
        }

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection scaled by " + amount + " block" + (amount != 1 ? "s" : "") + " " + direction.toString().toLowerCase());
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            return Lists.newArrayList("amount");
        } else if (args.length == 3) {
            return StringUtil.copyPartialMatches(args[2], ClipboardDirection.getCardinalNames(), new ArrayList<>());
        }

        return super.onTabComplete(args);
    }
}

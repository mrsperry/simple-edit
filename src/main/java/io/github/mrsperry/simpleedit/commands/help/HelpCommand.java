package io.github.mrsperry.simpleedit.commands.help;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;

public final class HelpCommand {
    private static final int linesToDisplay = 5;
    private static final ArrayList<String> pages = new ArrayList<>(Arrays.asList(
            "pos1 - sets the first clipboard position",
            "pos2 - sets the second clipboard position",
            "outline - shows the outline of your selection",
            "set - replaces all materials with any number of materials",
            "replace - replaces specific materials with other materials",
            "replacenear - replaces specific materials around you (not your selection) with other materials",
            "box - creates an enclosed box (add '-h' for hollow faces) out of the specified materials"
    ));

    public static void onCommand(final CommandSender sender, final String[] args) {
        int page = 0;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]) - 1;
            } catch (final NumberFormatException ex) {
                SimpleEditCommands.invalidArgument(sender, HelpCommand.getErrorMessage(), args[1]);
                return;
            }
        } else if (args.length != 1) {
            SimpleEditCommands.tooManyArguments(sender, HelpCommand.getErrorMessage());
            return;
        }

        if (page < 0 || page > HelpCommand.getNumberOfPages() - 1) {
            SimpleEditCommands.invalidArgument(sender, HelpCommand.getErrorMessage(), args[1]);
            return;
        }

        final StringBuilder message = new StringBuilder()
                .append(ChatColor.YELLOW)
                .append("===== SimpleEdit Help (")
                .append(page + 1)
                .append("/")
                .append(HelpCommand.getNumberOfPages())
                .append(") =====\n");
        for (int index = 0; index < HelpCommand.linesToDisplay; index++) {
            try {
                message.append(HelpCommand.pages.get((page * HelpCommand.linesToDisplay) + index))
                        .append("\n");
            } catch (final IndexOutOfBoundsException ex) {
                break;
            }
        }

        sender.sendMessage(message.toString());
    }

    public static ArrayList<String> onTabComplete(final int length) {
        if (length == 2) {
            return Lists.newArrayList("page #");
        } else {
            return null;
        }
    }

    private static String getErrorMessage() {
        return "help [page (1-" + HelpCommand.getNumberOfPages() + ")]";
    }

    private static int getNumberOfPages() {
        return (int) Math.ceil(HelpCommand.pages.size() / (float) HelpCommand.linesToDisplay);
    }
}

package io.github.mrsperry.simpleedit.commands;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.help.HelpCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import java.util.List;

public final class SimpleEditCommands implements TabExecutor {
    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String line, final String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "help":
                    HelpCommand.onCommand(sender, args);
                    break;
                default:
                    SimpleEditCommands.invalidArgument(sender, "<action | help>", args[0]);
                    break;
            }
        } else {
            SimpleEditCommands.tooFewArguments(sender, "<action | help>");
        }

        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender sender, final Command command, final String line, final String[] args) {
        switch (args[0]) {
            case "help":
                return HelpCommand.onTabComplete(args.length);
            default:
                return Lists.newArrayList("help");
        }
    }

    /**
     * Sent when there are too few arguments for a command
     * @param sender The command sender
     * @param usage The proper command usage
     */
    public static void tooFewArguments(final CommandSender sender, final String usage) {
        sender.sendMessage(ChatColor.RED + "Too few arguments!");
        SimpleEditCommands.usage(sender, usage);
    }

    /**
     * Sent when there are too many arguments for a command
     * @param sender The command sender
     * @param usage The proper command usage
     */
    public static void tooManyArguments(final CommandSender sender, final String usage) {
        sender.sendMessage(ChatColor.RED + "Too many arguments!");
        SimpleEditCommands.usage(sender, usage);
    }

    /**
     * Sent when there is an invalid argument for a command
     * @param sender The command sender
     * @param usage The proper command usage
     * @param arg The invalid argument
     */
    public static void invalidArgument(final CommandSender sender, final String usage, final String arg) {
        sender.sendMessage(ChatColor.RED + "Invalid argument: " + arg);
        SimpleEditCommands.usage(sender, usage);
    }

    /**
     * Sent when the correct usage of a command needs to be displayed
     * @param sender The command sender
     * @param usage The proper command usage
     */
    public static void usage(final CommandSender sender, final String usage) {
        sender.sendMessage(ChatColor.RED + "Usage: /simpleedit " + usage);
    }

    /**
     * Sent when a console sender tries to use a player only command
     * @param sender The command sender
     */
    public static void mustBePlayer(final CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
    }
}

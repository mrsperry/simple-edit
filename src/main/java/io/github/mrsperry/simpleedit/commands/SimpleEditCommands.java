package io.github.mrsperry.simpleedit.commands;

import io.github.mrsperry.simpleedit.commands.help.HelpCommand;
import io.github.mrsperry.simpleedit.commands.items.WandCommand;
import io.github.mrsperry.simpleedit.commands.selection.*;
import io.github.mrsperry.simpleedit.commands.selection.actions.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import java.util.*;

public final class SimpleEditCommands implements TabExecutor {
    private final Map<String, ICommandHandler> commands;

    public SimpleEditCommands() {
        this.commands = new HashMap<>();
        this.commands.put("help", new HelpCommand());
        this.commands.put("pos1", new PositionCommand());
        this.commands.put("pos2", new PositionCommand());
        this.commands.put("outline", new OutlineCommand());
        this.commands.put("set", new SetCommand());
        this.commands.put("replace", new ReplaceCommand());
        this.commands.put("replacenear", new ReplaceNearCommand());
        this.commands.put("box", new BoxCommand());
        this.commands.put("walls", new WallsCommand());
        this.commands.put("copy", new CopyCommand());
        this.commands.put("paste", new PasteCommand());
        this.commands.put("wand", new WandCommand());
        this.commands.put("fill", new FillCommand());
        this.commands.put("cyl", new CylCommand());
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String line, final String[] args) {
        if (args.length >= 1) {
            final String cmd = args[0].toLowerCase();
            if (this.commands.containsKey(cmd)) {
                this.commands.get(cmd).onCommand(sender, args);
                return true;
            }

            SimpleEditCommands.invalidArgument(sender, "<action | help>", args[0]);
            return true;
        }

        SimpleEditCommands.tooFewArguments(sender, "<action | help>");
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender sender, final Command command, final String line, final String[] args) {
        final String cmd = args[0].toLowerCase();
        if (this.commands.containsKey(cmd)) {
            return this.commands.get(cmd).onTabComplete(args);
        }

        return StringUtil.copyPartialMatches(args[0], this.commands.keySet(), new ArrayList<>());
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

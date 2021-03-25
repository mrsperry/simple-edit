package io.github.mrsperry.simpleedit.commands;

import io.github.mrsperry.simpleedit.commands.items.WandCommand;
import io.github.mrsperry.simpleedit.commands.misc.HelpCommand;
import io.github.mrsperry.simpleedit.commands.misc.UpCommand;
import io.github.mrsperry.simpleedit.commands.selection.*;
import io.github.mrsperry.simpleedit.commands.selection.actions.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SimpleEditCommands implements TabExecutor {
    private static boolean worldEditCommands;
    private final Map<String, BaseCommand> commands;

    public SimpleEditCommands(final boolean worldEditCommands) {
        SimpleEditCommands.worldEditCommands = worldEditCommands;
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
        this.commands.put("undo", new UndoCommand());
        this.commands.put("redo", new RedoCommand());
        this.commands.put("rotate", new RotateCommand());
        this.commands.put("naturalize", new NaturalizeCommand());
        this.commands.put("up", new UpCommand());
        this.commands.put("scale", new ScaleCommand());
        this.commands.put("stack", new StackCommand());
    }

    /**
     * Sent when there are too few arguments for a command
     *
     * @param sender The command sender
     * @param usage  The proper command usage
     */
    public static void tooFewArguments(final CommandSender sender, final String usage) {
        sender.sendMessage(ChatColor.RED + "Too few arguments!");
        SimpleEditCommands.usage(sender, usage);
    }

    /**
     * Sent when there are too many arguments for a command
     *
     * @param sender The command sender
     * @param usage  The proper command usage
     */
    public static void tooManyArguments(final CommandSender sender, final String usage) {
        sender.sendMessage(ChatColor.RED + "Too many arguments!");
        SimpleEditCommands.usage(sender, usage);
    }

    /**
     * Sent when there is an invalid argument for a command
     *
     * @param sender The command sender
     * @param usage  The proper command usage
     * @param arg    The invalid argument
     */
    public static void invalidArgument(final CommandSender sender, final String usage, final String arg) {
        sender.sendMessage(ChatColor.RED + "Invalid argument: " + arg);
        SimpleEditCommands.usage(sender, usage);
    }

    /**
     * Sent when the correct usage of a command needs to be displayed
     *
     * @param sender The command sender
     * @param usage  The proper command usage
     */
    public static void usage(final CommandSender sender, final String usage) {
        sender.sendMessage(ChatColor.RED + "Usage: /" + (SimpleEditCommands.worldEditCommands ? "/" : "simpleedit ") + usage);
    }

    /**
     * Sent when a console sender tries to use a player only command
     *
     * @param sender The command sender
     */
    public static void mustBePlayer(final CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
    }

    private String[] convertArgs(final Command command, String[] args) {
        final String[] copy = new String[args.length + 1];
        copy[0] = command.getName().toLowerCase().substring(1);
        System.arraycopy(args, 0, copy, 1, args.length);

        return copy;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String line, String[] args) {
        if (SimpleEditCommands.worldEditCommands && command.getName().startsWith("/")) {
            args = this.convertArgs(command, args);
        }

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
    public final List<String> onTabComplete(final CommandSender sender, final Command command, final String line, String[] args) {
        if (SimpleEditCommands.worldEditCommands && command.getName().startsWith("/")) {
            args = this.convertArgs(command, args);
        }

        final String cmd = args[0].toLowerCase();
        if (this.commands.containsKey(cmd)) {
            return this.commands.get(cmd).onTabComplete(args);
        }

        return StringUtil.copyPartialMatches(args[0], this.commands.keySet(), new ArrayList<>());
    }

    public final Map<String, BaseCommand> getCommandMap() {
        return this.commands;
    }
}

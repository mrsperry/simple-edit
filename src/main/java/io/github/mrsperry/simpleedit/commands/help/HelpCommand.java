package io.github.mrsperry.simpleedit.commands.help;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HelpCommand extends BaseCommand {
    private final int linesToDisplay;
    private final ArrayList<String> pages;

    public HelpCommand() {
        super("help [page]");

        this.linesToDisplay = 5;
        this.pages = new ArrayList<>(Arrays.asList(
                "pos1 - sets the first clipboard position",
                "pos2 - sets the second clipboard position",
                "outline - shows the outline of your selection",
                "set - replaces all materials with any number of materials",
                "replace - replaces specific materials with other materials",
                "replacenear - replaces specific materials around you (not your selection) with other materials",
                "box - creates an enclosed box (add '-h' for hollow faces) out of the specified materials",
                "walls - creates solid walls out of the specified materials",
                "copy - copies the current selection to be pasted elsewhere",
                "paste - pastes the copied selection relative to your position",
                "wand - gives you a wand item that can be used to set positions",
                "fill - flood fills an area based on the specified radius",
                "cyl - creates a cylinder (add '-h' for additional height) out of the specified materials",
                "undo - undoes your previous actions",
                "redo - redoes your previous actions"
        ));
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        int page = 0;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]) - 1;
            } catch (final NumberFormatException ex) {
                SimpleEditCommands.invalidArgument(sender, this.getErrorMessage(), args[1]);
                return;
            }
        }

        if (page < 0 || page > this.getNumberOfPages() - 1) {
            SimpleEditCommands.invalidArgument(sender, this.getErrorMessage(), args[1]);
            return;
        }

        final StringBuilder message = new StringBuilder()
                .append(ChatColor.YELLOW)
                .append("===== SimpleEdit Help (")
                .append(page + 1)
                .append("/")
                .append(this.getNumberOfPages())
                .append(") =====\n");
        for (int index = 0; index < this.linesToDisplay; index++) {
            try {
                message.append(this.pages.get((page * this.linesToDisplay) + index))
                        .append("\n");
            } catch (final IndexOutOfBoundsException ex) {
                break;
            }
        }

        sender.sendMessage(message.toString());
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length > 1) {
            return Lists.newArrayList("page #");
        }

        return super.onTabComplete(args);
    }

    private String getErrorMessage() {
        return "help [page (1-" + this.getNumberOfPages() + ")]";
    }

    private int getNumberOfPages() {
        return (int) Math.ceil(this.pages.size() / (float) this.linesToDisplay);
    }
}

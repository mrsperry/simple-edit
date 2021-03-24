package io.github.mrsperry.simpleedit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand {
    private final String usage;

    protected BaseCommand(final String usage) {
        this.usage = usage;
    }

    protected final boolean commandPrerequisites(final CommandSender sender, final String[] args) {
        return this.commandPrerequisites(sender, args, 0, 0);
    }

    protected final boolean commandPrerequisites(final CommandSender sender, final String[] args, final int minArgs, final int maxArgs) {
        if (!(sender instanceof Player)) {
            SimpleEditCommands.mustBePlayer(sender);
            return true;
        }

        if (minArgs > args.length - 1) {
            SimpleEditCommands.tooFewArguments(sender, this.usage);
            return true;
        } else if (maxArgs != -1 && maxArgs < args.length - 1) {
            SimpleEditCommands.tooManyArguments(sender, this.usage);
            return true;
        }

        return false;
    }

    protected abstract void onCommand(final CommandSender sender, final String[] args);

    protected List<String> onTabComplete(final String[] args) {
        return new ArrayList<>();
    }

    protected final String getUsage() {
        return this.usage;
    }
}

package io.github.mrsperry.simpleedit.commands.selection;

import com.google.common.collect.Lists;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.List;

public final class RotateCommand extends BaseCommand {
    public RotateCommand() {
        super("rotate <degrees>");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        int degrees = 90;
        if (args.length == 2) {
            try {
                degrees = Integer.parseInt(args[1]);
            } catch (final NumberFormatException ex) {
                SimpleEditCommands.invalidArgument(sender, super.getUsage(), args[1]);
                return;
            }

            if (Math.abs(degrees) % 90 != 0 || degrees == 0 || degrees < -270 || degrees > 270) {
                sender.sendMessage(ChatColor.RED + "Degrees must be a factor of 90 and no greater than 270.");
                return;
            }
        }

        int absoluteDegrees = degrees;
        if (degrees < 0) {
            absoluteDegrees = 360 + degrees;
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        session.getSelection().getClipboard().rotate(absoluteDegrees);

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection rotated by " + degrees + " degrees");
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            if (args[1].startsWith("-")) {
                return StringUtil.copyPartialMatches(args[1], Lists.newArrayList("-90", "-180", "-270"), Lists.newArrayList());
            } else {
                return StringUtil.copyPartialMatches(args[1], Lists.newArrayList("90", "180", "270"), Lists.newArrayList());
            }
        }

        return super.onTabComplete(args);
    }
}

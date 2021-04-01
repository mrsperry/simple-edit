package io.github.mrsperry.simpleedit.commands.selection;

import io.github.mrsperry.mcutils.types.ColorTypes;
import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.Session;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import io.github.mrsperry.simpleedit.sessions.selections.SelectionOutline;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public final class OutlineCommand extends BaseCommand {
    public OutlineCommand() {
        super("outline [color]");
    }

    @Override
    public final void onCommand(final CommandSender sender, final String[] args) {
        if (super.commandPrerequisites(sender, args, 0, 1)) {
            return;
        }

        boolean toggle = true;
        Color color = Color.RED;
        if (args.length == 2) {
            toggle = false;

            color = ColorTypes.stringToColor(args[1]);
            if (color == null) {
                SimpleEditCommands.invalidArgument(sender, this.getUsage(), args[1]);
                return;
            }
        }

        final Player player = (Player) sender;
        final Session session = SessionManager.getSession(player.getUniqueId());
        final SelectionOutline outline = session.getSelection().getOutline();

        if (toggle) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection outline " + (outline.toggle() ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        } else {
            outline.setColor(color);
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Selection outline color set to " + ColorTypes.colorToChatColor(color) + args[1]);
        }
    }

    @Override
    public final List<String> onTabComplete(final String[] args) {
        if (args.length == 2) {
            final List<String> colors = new ArrayList<>();
            for (final String color : ColorTypes.getColors().keySet()) {
                colors.add(color.toLowerCase());
            }

            return StringUtil.copyPartialMatches(args[1], colors, new ArrayList<>());
        }

        return super.onTabComplete(args);
    }
}

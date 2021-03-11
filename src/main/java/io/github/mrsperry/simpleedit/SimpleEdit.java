package io.github.mrsperry.simpleedit;

import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleEdit extends JavaPlugin {
    private static SimpleEdit instance;

    @Override
    public final void onEnable() {
        SimpleEdit.instance = this;

        this.saveDefaultConfig();

        final PluginCommand command = this.getCommand("simpleedit");
        if (command != null) {
            command.setExecutor(new SimpleEditCommands());
        } else {
            this.getLogger().severe("Could not bind main command!");
        }

        SessionManager.initialize(this.getDataFolder());
    }

    @Override
    public final void onDisable() {
        SessionManager.save();
    }

    public static SimpleEdit getInstance() {
        return SimpleEdit.instance;
    }
}

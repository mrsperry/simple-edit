package io.github.mrsperry.simpleedit;

import io.github.mrsperry.simpleedit.commands.BaseCommand;
import io.github.mrsperry.simpleedit.commands.SimpleEditCommands;
import io.github.mrsperry.simpleedit.items.Brushes;
import io.github.mrsperry.simpleedit.items.Wand;
import io.github.mrsperry.simpleedit.sessions.SessionManager;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

public final class SimpleEdit extends JavaPlugin implements Listener {
    private static SimpleEdit instance;

    public static SimpleEdit getInstance() {
        return SimpleEdit.instance;
    }

    @Override
    public final void onEnable() {
        SimpleEdit.instance = this;

        this.saveDefaultConfig();

        final PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new Wand(this), this);
        manager.registerEvents(new Brushes(), this);

        final boolean worldEditCommands = this.getConfig().getBoolean("world-edit-commands", true);
        final SimpleEditCommands executor = new SimpleEditCommands(worldEditCommands);

        if (worldEditCommands) {
            this.registerWorldEditCommands(executor);
        }

        final PluginCommand command = this.getCommand("simpleedit");
        if (command != null) {
            command.setExecutor(executor);
        } else {
            this.getLogger().severe("Could not bind main command!");
        }


        SessionManager.initialize(this.getDataFolder());
    }

    private void registerWorldEditCommands(final SimpleEditCommands executor) {
        try {
            // Allow access to the global command map
            final Server server = this.getServer();
            final Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            // Allow access to plugin commands
            final Constructor<PluginCommand> pluginCommand = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            pluginCommand.setAccessible(true);

            // Get the command executor along with a list of commands to register
            final Map<String, BaseCommand> commandMap = executor.getCommandMap();

            final CommandMap registrar = (CommandMap) field.get(server);
            final String pluginName = this.getName();

            // Register commands and se their executor
            for (final String commandName : commandMap.keySet()) {
                final PluginCommand command = pluginCommand.newInstance("/" + commandName, this);
                registrar.register(pluginName, command);
                command.setExecutor(executor);
            }
        } catch (final Exception ex) {
            this.getLogger().severe("Could not register world edit style commands!");
        }
    }

    @Override
    public final void onDisable() {
        SessionManager.save();
    }
}

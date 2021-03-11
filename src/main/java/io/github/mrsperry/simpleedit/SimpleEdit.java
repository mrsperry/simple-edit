package io.github.mrsperry.simpleedit;

import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleEdit extends JavaPlugin {
    private static SimpleEdit instance;

    @Override
    public final void onEnable() {
        SimpleEdit.instance = this;
    }

    public static SimpleEdit getInstance() {
        return SimpleEdit.instance;
    }
}

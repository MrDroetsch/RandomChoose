package me.bunnykick.randomchoose;

import me.bunnykick.randomchoose.commands.Commands;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class RandomChoose extends JavaPlugin {

    private ConfigManager configManager;
    private Commands commands;

    public static boolean USE_DEFAULT;

    @Override
    public void onEnable() {
        loadConfig();
        loadCommands();
    }

    @Override
    public void onDisable() {}

    public void loadCommands() {
        commands = new Commands(this);
        commands.load();
    }

    public void loadConfig() {
        try {
            configManager = new ConfigManager(this);
            USE_DEFAULT = false;
        } catch(IOException | InvalidConfigurationException e) {
            System.err.println("ERROR beim laden der Config. Default werte werden genutzt!");
            USE_DEFAULT = true;
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Commands getCommands() {
        return commands;
    }

}

package me.bunnykick.randomchoose;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.bunnykick.randomchoose.utils.ConfigPaths.Permission.*;
import static me.bunnykick.randomchoose.utils.ConfigPaths.Message.*;

public class ConfigManager {

    private RandomChoose plugin;
    private File file;
    private FileConfiguration config;

    public ConfigManager(RandomChoose plugin) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    private void load() throws IOException, InvalidConfigurationException {
        if(!file.exists()) {
            config.options().copyDefaults(true);
            config.addDefault(CHOOSE_RANDOM, CHOOSE_RANDOM_DEFAULT);
            config.addDefault(RANDOM_BYPASS, RANDOM_BYPASS_DEFAULT);
            config.addDefault(RANDOM_REWARD, RANDOM_REWARD_DEFAULT);
            config.addDefault(NOPERM, "&4Keine Berechtigung");
            save();
        } else {
            config.load(file);
        }
    }

    private void save() throws IOException {
        config.save(file);
    }

    public String getString(String path) {
        if(!config.contains(path)) {
            throw new NullPointerException("Angegebener Config-Pfad existiert nicht: (\"" + path + "\")");
        }
        return config.getString(path);
    }

}

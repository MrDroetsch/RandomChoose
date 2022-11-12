package me.bunnykick.randomchoose.commands;

import me.bunnykick.randomchoose.RandomChoose;

public class Commands {

    private RandomChoose plugin;

    private CRandomChoose cRandomChoose;

    public Commands(RandomChoose plugin) {
        this.plugin = plugin;
    }

    public void load() {
        cRandomChoose = new CRandomChoose(this);
        plugin.getCommand("chooserandom").setExecutor(cRandomChoose);
    }

    public RandomChoose getPlugin() {
        return plugin;
    }
}

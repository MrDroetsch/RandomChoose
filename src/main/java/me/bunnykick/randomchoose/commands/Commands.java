package me.bunnykick.randomchoose.commands;

import me.bunnykick.randomchoose.RandomChoose;

public class Commands {

    private RandomChoose plugin;

    private CRandomChoose cRandomChoose;
    private RandomReward randomReward;

    public Commands(RandomChoose plugin) {
        this.plugin = plugin;
    }

    public void load() {
        cRandomChoose = new CRandomChoose(this);
        plugin.getCommand("chooserandom").setExecutor(cRandomChoose);

        randomReward = new RandomReward(this);
        plugin.getCommand("randomreward").setExecutor(randomReward);
    }

    public CRandomChoose getRandomChooseCommand() {
        return cRandomChoose;
    }

    public RandomChoose getPlugin() {
        return plugin;
    }
}

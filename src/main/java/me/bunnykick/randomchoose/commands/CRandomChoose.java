package me.bunnykick.randomchoose.commands;

import me.bunnykick.randomchoose.ConfigManager;
import me.bunnykick.randomchoose.RandomChoose;
import me.bunnykick.randomchoose.utils.ConfigPaths;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CRandomChoose implements CommandExecutor {

    private Commands commands;
    private ConfigManager configManager;
    private Random random;

    public CRandomChoose(Commands commands) {
        this.commands = commands;
        configManager = commands.getPlugin().getConfigManager();
        random = new Random();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            switch(args.length) {
                case 0 -> playerCommand(player, RandomChoose.USE_DEFAULT, false, null);
                case 1 -> {
                    if(args[0].equalsIgnoreCase("all")) {
                        playerCommand(player, RandomChoose.USE_DEFAULT, true, null);
                    } else {
                        player.sendMessage("§2Ergebnis: §c" + args[0]);
                    }
                }
                default -> playerCommand(player, RandomChoose.USE_DEFAULT, false, List.of(args));
            }
        } else {
            // TODO: Console??
        }
        return true;
    }

    private boolean checkPermission(Player player, boolean def, String configPath) {
        String permission = ConfigPaths.Permission.getDefault(configPath);

        if(!def) permission = configManager.getString(configPath);

        return player.hasPermission(permission);
    }

    private void playerCommand(Player player, boolean def, boolean all, List<String> auswahl) {

        if(!checkPermission(player, def, ConfigPaths.Permission.CHOOSE_RANDOM)) {
            player.sendMessage(configManager.getString(ConfigPaths.Message.NOPERM).replace("&", "§"));
            return;
        }

        if(auswahl != null && auswahl.size() > 0) {
            player.sendMessage("§2Random-Ergebnis: §c" + auswahl.get(random.nextInt(auswahl.size())));
            return;
        }

        String bypass = def ? ConfigPaths.Permission.CHOOSE_RANDOM_BYPASS_DEFAULT : configManager.getString(ConfigPaths.Permission.CHOOSE_RANDOM_BYPASS);
        List<Player> playerList = all ?
                new ArrayList<>(commands.getPlugin().getServer().getOnlinePlayers()) :
                commands.getPlugin().getServer().getOnlinePlayers().stream().filter(p -> !p.hasPermission(bypass)).collect(Collectors.toList());

        if(playerList.size() < 1) {
            player.sendMessage("§cError! Keine Spieler zur auswahl");
            return;
        }
        Player target = playerList.get(random.nextInt(playerList.size()));
        if(target != null) {
            player.sendMessage("§2Random-Spieler: §c" + target.getName());
        }
    }

}

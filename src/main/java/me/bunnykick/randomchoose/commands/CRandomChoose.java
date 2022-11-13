package me.bunnykick.randomchoose.commands;

import me.bunnykick.randomchoose.ConfigManager;
import me.bunnykick.randomchoose.RandomChoose;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static me.bunnykick.randomchoose.utils.ConfigPaths.*;

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
                case 0 -> playerCommand(player, false, null);
                case 1 -> {
                    if(args[0].equalsIgnoreCase("all")) {
                        playerCommand(player, true, null);
                    } else if(args[0].equalsIgnoreCase("help")) {
                        if(checkPermission(player, Permission.CHOOSE_RANDOM)) {
                            player.sendMessage("§2Einen Random-Spieler auswählen:");
                            player.sendMessage("§c/chooserandom §7- Wählt einen zufälligen Spieler aus");
                            player.sendMessage("§c/chooserandom all §7- Wählt einen Spieler aus, bypass wird ignoriert");
                            player.sendMessage("§c/chooserandom <Liste> §7- Wählt eins der angegebenen Wörter aus");
                            player.sendMessage("§c/chooserandom help §7- zeigt diese Hilfe an");
                        } else {
                            player.sendMessage(configManager.getString(Message.NOPERM).replace("&", "§"));
                        }
                    } else {
                        player.sendMessage("§2Ergebnis: §c" + args[0]);
                    }
                }
                default -> playerCommand(player, false, List.of(args));
            }
        } else {
            // TODO: Console??
        }
        return true;
    }

    private boolean checkPermission(Player player, String configPath) {
        String permission = Permission.getDefault(configPath);

        if(!RandomChoose.USE_DEFAULT) permission = configManager.getString(configPath);

        return player.hasPermission(permission);
    }

    private void playerCommand(Player player, boolean all, List<String> auswahl) {

        if(!checkPermission(player, Permission.CHOOSE_RANDOM)) {
            player.sendMessage(configManager.getString(Message.NOPERM).replace("&", "§"));
            return;
        }

        if(auswahl != null && auswahl.size() > 0) {
            player.sendMessage("§2Random-Ergebnis: §c" + auswahl.get(random.nextInt(auswahl.size())));
            return;
        }

        String bypass = RandomChoose.USE_DEFAULT ? Permission.RANDOM_BYPASS_DEFAULT : configManager.getString(Permission.RANDOM_BYPASS);
        List<Player> playerList = all ?
                new ArrayList<>(commands.getPlugin().getServer().getOnlinePlayers()) :
                commands.getPlugin().getServer().getOnlinePlayers().stream().filter(p -> !p.hasPermission(bypass)).collect(Collectors.toList());

        Player target = choose(playerList);
        if(target == null) {
            player.sendMessage("§cError! Keine Spieler zur auswahl");
            return;
        }

        player.sendMessage("§2Random-Spieler: §c" + target.getName());
    }

    public <E> E choose(List<E> list) {
        if(list != null && list.size() > 0) {
            return list.get(random.nextInt(list.size()));
        }
        return null;
    }

}

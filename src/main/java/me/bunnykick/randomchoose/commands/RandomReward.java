package me.bunnykick.randomchoose.commands;

import me.bunnykick.randomchoose.ConfigManager;
import me.bunnykick.randomchoose.RandomChoose;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.bunnykick.randomchoose.utils.ConfigPaths.Permission.*;
import static me.bunnykick.randomchoose.utils.ConfigPaths.Message.*;

public class RandomReward implements CommandExecutor, Listener {

    private Commands commands;
    private ConfigManager configManager;

    private Player winner;
    private Inventory inventory;

    public RandomReward(Commands commands) {
        this.commands = commands;
        configManager = commands.getPlugin().getConfigManager();
        winner = null;
        inventory = commands.getPlugin().getServer().createInventory(null, InventoryType.CHEST, "§2Verlosungsitems");

        commands.getPlugin().getServer().getPluginManager().registerEvents(this, commands.getPlugin());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            switch(args.length) {
                case 0 -> {
                    if(isWinner(player)) {
                        openInventory(player);
                    } else {
                        openCheckedInventory(player);
                    }
                }
                case 1 ->  {
                    if(args[0].equalsIgnoreCase("choose")) {
                        choosePlayer(player, false);
                    } else if(args[0].equalsIgnoreCase("chooseall")) {
                        choosePlayer(player, true);
                    } else if(args[0].equalsIgnoreCase("reset")) {
                        reset(player);
                    } else {
                        checkedHelp(player);
                    }
                }
                default -> checkedHelp(player);
            }
        } else {
            // TODO: Console??
        }
        return true;
    }

    private boolean isWinner(Player player) {
        return winner != null && player.getUniqueId().equals(winner.getUniqueId());
    }

    private void openInventory(Player player) {
        player.openInventory(inventory);
    }

    private boolean check(Player player, String path, boolean message) {
        String permission = RandomChoose.USE_DEFAULT ? getDefault(path) : configManager.getString(path);
        if(!player.hasPermission(permission)) {
            if(message) player.sendMessage(configManager.getString(NOPERM).replace("&", "§"));
            return false;
        }
        return true;
    }

    private void reset(Player player) {
        if(!check(player, RANDOM_REWARD, true)) return;
        winner = null;
        inventory.clear();
        player.sendMessage("§2Verlosung zurückgesetzt.");
    }

    private void openCheckedInventory(Player player) {
        if(!check(player, RANDOM_REWARD, true)) return;
        player.openInventory(inventory);
        if(winner != null) {
            player.sendMessage("§4ACHTUNG! \n§2Die Items im Inventar wurden noch nicht abgeholt. \nLetzter Gewinner der Verlosung: §c" + winner.getName());
            player.sendMessage("§2Nutze §c/randomreward reset §2um das Inventar und den Gewinner zu löschen (Items gehen verloren!)");
        }
    }

    private void checkedHelp(Player player) {
        if(isWinner(player)) {
            player.sendMessage("§2Nutze §c/randomreward §2um an deinen Gewinn zu kommen!");
            return;
        }
        if(!check(player, RANDOM_REWARD, true)) return;
        player.sendMessage("§2Verlosung von Items:");
        player.sendMessage("§c/randomreward §7- um die zu verlosenden Items zu setzen");
        player.sendMessage("§c/randomreward choose §7- um die Items unter den Spielern zu verlosen (Ein Gewinner kriegt alles)");
        player.sendMessage("§c/randomreward chooseall §7- ignoriert die bypass-Permission und lost aus alles Spielern einen aus");
        player.sendMessage("§c/randomreward help §7- zeigt diese Hilfe an");
    }

    private boolean hasSlot(Inventory inventory) {
        return inventory.firstEmpty() >= 0;
    }

    private void choosePlayer(Player player, boolean all) {
        if(!check(player, RANDOM_REWARD, true)) return;

        if(inventory.isEmpty()) {
            player.sendMessage("§cEs befinden sihc keine Items im Inventar. Nutze /randomreward um Items hinzuzufügen.");
            return;
        }

        List<Player> playerList = all ? new ArrayList<>(commands.getPlugin().getServer().getOnlinePlayers()) :
                commands.getPlugin().getServer().getOnlinePlayers().stream().filter(p -> !check(p, RANDOM_BYPASS, false)).collect(Collectors.toList());
        Player target = commands.getRandomChooseCommand().choose(playerList);
        if(target == null) {
            player.sendMessage("§cERROR! Keine Spieler zur Auswahl!");
            return;
        }

        handleWinner(target);
        player.sendMessage("§c" + target.getName() + "§2 hat die Verlosung gewonnen!");
    }

    private ItemStack getFirst(Inventory inv) {
        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if(item != null) {
                inventory.setItem(i, null);
                return item;
            }
        }
        return null;
    }

    private void handleWinner(Player player) {
        Inventory playerInventory = player.getInventory();
        while(hasSlot(playerInventory)) {
            ItemStack item = getFirst(inventory);
            if(item == null) {
                break;
            }
            playerInventory.addItem(item);
        }
        if(inventory.isEmpty()) {
            player.sendMessage("§2Glückwunsch! Du hast bei der Verlosung gewonnen! Die Items sind in deinem Inventar.");
            return;
        }
        winner = player;
        player.sendMessage("§2Glückwunsch! Du hast bei der Verlosung gewonnen!");
        player.sendMessage("§2Dein Inventar ist zu voll! Leere es aus und nutze §c/randomreward §2um deine Items zu erhalten!");
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(winner != null && inventory.isEmpty()) {
            winner = null;
        }
    }

}

package org.cptgummiball.bonk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private final BONK plugin;
    private final ItemManager itemManager;
    private final ConfigManager configManager;

    public CommandHandler(BONK plugin, ItemManager itemManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String notPlayer = plugin.getConfig().getString("messages.notplayer");
        String noPermission = plugin.getConfig().getString("messages.nopermission");
        String bonkReceive = plugin.getConfig().getString("messages.bonkreceive");
        String reloadMessage = plugin.getConfig().getString("messages.reload");

        if (command.getName().equalsIgnoreCase("bonk")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("bonk.reload")) {
                    sender.sendMessage(ChatColor.RED + noPermission);
                    return true;
                }

                plugin.reloadConfig();
                configManager.loadConfigValues();
                sender.sendMessage(ChatColor.GREEN + reloadMessage);
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(notPlayer);
                return true;
            }

            Player player = (Player) sender;
            if (!player.hasPermission("bonk.get")) {
                player.sendMessage(ChatColor.RED + noPermission);
                return true;
            }

            player.getInventory().addItem(itemManager.getBonkItem());
            player.sendMessage(ChatColor.GREEN + bonkReceive);

            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
        }
        return completions;
    }
}


package org.cptgummiball.bonk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

public class BONK extends JavaPlugin implements Listener, TabExecutor {

    private ItemStack bonkItem;
    private double knockbackStrength;
    private double damage;

    @Override
    public void onEnable() {
        // Load Config
        saveDefaultConfig();
        loadConfigValues();

        // Register event listeners
        getServer().getPluginManager().registerEvents(this, this);

        // Register Command
        this.getCommand("bonk").setExecutor(this);
        this.getCommand("bonk").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
    }

    // Method for loading configuration values
    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        String materialName = config.getString("general.bonk-item", "STICK");
        Material material = Material.matchMaterial(materialName);

        if (material != null) {
            bonkItem = createBonkItem(material);
        } else {
            bonkItem = createBonkItem(Material.STICK);  // Fallback
        }

        knockbackStrength = config.getDouble("general.knockback-strength", 1.0);
        damage = config.getDouble("general.damage", 0.0);
    }

    // Method to create the special Bonk item with unique properties
    private ItemStack createBonkItem(Material material) {
        String bonkname = getConfig().getString("general.Name");
        String bonklore = getConfig().getString("general.Lore");
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        // Set a unique name and lore
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + bonkname);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + bonklore);
            meta.setLore(lore);

            // Add invisible marker
            meta.setCustomModelData(49721);
            item.setItemMeta(meta);
        }

        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String notplayer = getConfig().getString("messages.notplayer");
        String nopermission = getConfig().getString("messages.nopermission");
        String bonkreceive = getConfig().getString("messages.bonkreceive");
        String reloadMessage = getConfig().getString("messages.reload");

        // Check whether the command is "bonk".
        if (command.getName().equalsIgnoreCase("bonk")) {

            // Check whether the command is "/bonk reload".
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                // Check if sender has permission
                if (!sender.hasPermission("bonk.reload")) {
                    assert nopermission != null;
                    sender.sendMessage(ChatColor.RED + nopermission);
                    return true;
                }

                // Reload the configuration
                reloadConfig();
                loadConfigValues();  // Reload the values from config.yml
                assert reloadMessage != null;
                sender.sendMessage(ChatColor.GREEN + reloadMessage);
                return true;
            }

            // Command for receiving the Bonk item
            if (!(sender instanceof Player)) {
                assert notplayer != null;
                sender.sendMessage(notplayer);
                return true;
            }

            Player player = (Player) sender;

            // Check if player has permission
            if (!player.hasPermission("bonk.get")) {
                assert nopermission != null;
                player.sendMessage(ChatColor.RED + nopermission);
                return true;
            }

            // Give Bonk-Item
            player.getInventory().addItem(bonkItem);
            assert bonkreceive != null;
            player.sendMessage(ChatColor.GREEN + bonkreceive);

            return true;
        }
        return false;
    }

    // Event when attacking Entity
    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            // Check if player uses BONK
            if (isBonkItem(itemInHand)) {
                Entity target = event.getEntity();

                // Set knockback
                target.setVelocity(target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(knockbackStrength));

                // Set damage
                event.setDamage(damage);
            }
        }
    }

    // Method to check whether the item is the unique Bonk item
    private boolean isBonkItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        // Check whether the CustomModelData is set (serves as a clear marker)
        return meta.hasCustomModelData() && meta.getCustomModelData() == 49721;
    }


    // Tab completion for the command
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //Check if the command is "bonk"
        if (command.getName().equalsIgnoreCase("bonk")) {
            List<String> completions = new ArrayList<>();

            // If no arguments were entered, we add the reload argument
            if (args.length == 1) {
                completions.add("reload");
            }

            return completions;
        }
        return null;
    }
}

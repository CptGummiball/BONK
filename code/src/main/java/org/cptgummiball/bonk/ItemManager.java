package org.cptgummiball.bonk;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final BONK plugin;
    private ItemStack bonkItem;

    public ItemManager(BONK plugin) {
        this.plugin = plugin;
    }

    public void createBonkItem(Material material) {
        int modelData = plugin.getConfig().getInt("CustomModelData");
        String bonkName = plugin.getConfig().getString("general.Name");
        String bonkLore = plugin.getConfig().getString("general.Lore");
        ChatColor nameColor = ChatColor.valueOf(plugin.getConfig().getString("general.NameColor", "GOLD").toUpperCase());
        ChatColor loreColor = ChatColor.valueOf(plugin.getConfig().getString("general.LoreColor", "GRAY").toUpperCase());

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(nameColor + bonkName);
            List<String> lore = new ArrayList<>();
            lore.add(loreColor + bonkLore);
            meta.setLore(lore);
            meta.setCustomModelData(modelData);
            item.setItemMeta(meta);
        }

        bonkItem = item;
    }

    public boolean isBonkItem(ItemStack item) {
        int modelData = plugin.getConfig().getInt("CustomModelData");
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == modelData;
    }

    public ItemStack getBonkItem() {
        return bonkItem;
    }
}


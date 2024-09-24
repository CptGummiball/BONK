package org.cptgummiball.bonk;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BONK extends JavaPlugin {

    private ItemManager itemManager;
    private ConfigManager configManager;
    private CustomTextureManager customtexturemanager;

    @Override
    public void onEnable() {
        // Initialize Config Manager and Item Manager
        configManager = new ConfigManager(this);
        itemManager = new ItemManager(this);

        // Load Config
        configManager.updateConfig();
        configManager.loadConfigValues();

        // Initialize CustomTextureManager and create TextureFolder if not exist
        createTextureFolder();
        customtexturemanager = new CustomTextureManager(this);

        // Register Event Listeners
        getServer().getPluginManager().registerEvents(new EventListeners(this, itemManager, configManager), this);

        // Register Command
        CommandHandler commandHandler = new CommandHandler(this, itemManager, configManager);
        this.getCommand("bonk").setExecutor(commandHandler);
        this.getCommand("bonk").setTabCompleter(commandHandler);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    private void createTextureFolder() {
        // Define the folder for the textures
        File textureFolder = new File(getDataFolder(), "custom-texture");

        // Check if the folder exists and create it if not
        if (getConfig().getBoolean("use-custom-texture", false)) {
            if (!textureFolder.exists()) {
                boolean created = textureFolder.mkdirs(); // Erstelle den Ordner

                if (created) {
                    getLogger().info("Texture folder created: " + textureFolder.getAbsolutePath());
                } else {
                    getLogger().warning("Failed to create texture folder: " + textureFolder.getAbsolutePath());
                }
            } else {
                getLogger().info("Texture folder already exists: " + textureFolder.getAbsolutePath());
            }
        }
    }
}

package org.cptgummiball.bonk;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CustomTextureManager implements Listener {

    private final JavaPlugin plugin;
    private final String customTextureFolder = "custom-texture"; // Ordner mit den Texturen

    public CustomTextureManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // Register the listener for PlayerJoinEvent
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        // Check whether the use of custom textures is enabled
        if (config.getBoolean("use-custom-texture", false)) {
            // Send resource pack
            sendResourcePack(player);
        }
    }

    private void sendResourcePack(Player player) {
        // The path to the texture file
        Path texturePath = Path.of(plugin.getDataFolder().getPath(), customTextureFolder, "texture.png");

        if (Files.exists(texturePath)) {
            // Create the resource pack and send it to the player
            try {
                // Create a ZIP archive of the resource pack
                File resourcePack = createResourcePack(texturePath);
                player.setResourcePack(resourcePack.toURI().toString());
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().warning("Error sending resource pack to player: " + player.getName());
            }
        } else {
            plugin.getLogger().warning("Texture file not found: " + texturePath);
        }
    }

    private File createResourcePack(Path texturePath) throws IOException {
        File resourcePack = new File(plugin.getDataFolder(), "bonk-resource-pack.zip");

        // Create the ZIP archive
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(resourcePack))) {
            String namespace = "bonk";
            String itemName = plugin.getConfig().getString("general.Name", "Bonk Item"); // Setze den Namen aus der Config
            int customModelData = plugin.getConfig().getInt("general.CustomModelData", 49721); // Setze den CustomModelData-Wert

            // Add the texture file
            ZipEntry textureEntry = new ZipEntry("assets/" + namespace + "/textures/item/texture.png"); // Pfad in der ZIP-Datei
            zos.putNextEntry(textureEntry);
            Files.copy(texturePath, zos);
            zos.closeEntry();

            // Add the pack.mcmeta file
            ZipEntry mcmetaEntry = new ZipEntry("pack.mcmeta");
            zos.putNextEntry(mcmetaEntry);
            String mcmetaContent = "{\n" +
                    "  \"pack\": {\n" +
                    "    \"pack_format\": 6,\n" +
                    "    \"description\": \"Custom Resource Pack for BONK\"\n" +
                    "  }\n" +
                    "}";
            zos.write(mcmetaContent.getBytes());
            zos.closeEntry();

            // Add the models/item JSON file
            ZipEntry modelEntry = new ZipEntry("assets/" + namespace + "/models/item/texture.json");
            zos.putNextEntry(modelEntry);
            String modelContent = "{\n" +
                    "  \"parent\": \"item/generated\",\n" +
                    "  \"textures\": {\n" +
                    "    \"layer0\": \"" + namespace + ":textures/item/texture\"\n" +
                    "  },\n" +
                    "  \"custom_model_data\": " + customModelData + "\n" +
                    "}";
            zos.write(modelContent.getBytes());
            zos.closeEntry();

            //Add the lang JSON file
            ZipEntry langEntry = new ZipEntry("assets/" + namespace + "/lang/en_us.json");
            zos.putNextEntry(langEntry);
            String langContent = "{\n" +
                    "  \"item." + namespace + ".texture\": \"" + itemName + "\"\n" +
                    "}";
            zos.write(langContent.getBytes());
            zos.closeEntry();
        }

        return resourcePack;
    }
}


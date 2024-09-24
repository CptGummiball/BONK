package org.cptgummiball.bonk;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConfigManager {

    private final BONK plugin;

    public ConfigManager(BONK plugin) {
        this.plugin = plugin;
    }

    public void loadConfigValues() {
        FileConfiguration config = plugin.getConfig();
        String materialName = config.getString("general.bonk-item", "STICK");
        Material material = Material.matchMaterial(materialName);
        if (material != null) {
            plugin.getItemManager().createBonkItem(material);
        } else {
            plugin.getItemManager().createBonkItem(Material.STICK);
        }
    }

    public void updateConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String currentVersion = "1.1";
        String configVersion = config.getString("version", "0");

        if (configVersion.equals("0") || configVersion.compareTo(currentVersion) < 0) {
            YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml")));
            for (String key : config.getKeys(true)) {
                if (newConfig.contains(key)) {
                    newConfig.set(key, config.get(key));
                }
            }
            newConfig.set("version", currentVersion);
            try {
                newConfig.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleBonkEffects(Player player, Entity target) {
        FileConfiguration config = plugin.getConfig();
        String noparticle = config.getString("messages.invalidparticle");
        String nosound = config.getString("messages.invalidsound");

        // Set knockback
        target.setVelocity(target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(config.getDouble("general.knockback-strength", 1.0)));

        // Set damage
        if (target instanceof Player) {
            ((Player) target).damage(config.getDouble("general.damage", 0.0));
        }

        // Set on fire
        if (config.getBoolean("general.setonfire", false)) {
            int fireDurationSeconds = config.getInt("general.fire-duration", 5);
            target.setFireTicks(fireDurationSeconds * 20);  // Convert seconds to ticks
        }

        // Handle particle effect
        if (config.getBoolean("general.effect-enabled", false)) {
            String effectName = config.getString("general.knockback-effect", "SMOKE");
            try {
                Particle effect = Particle.valueOf(effectName.toUpperCase());
                target.getWorld().spawnParticle(effect, target.getLocation(), 20);
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + noparticle + effectName);
            }
        }

        // Handle sounds
        if (config.getBoolean("general.sound-enabled", false)) {
            String playerSoundName = config.getString("general.bonk-sound-player", "ENTITY_PLAYER_LEVELUP");
            String targetSoundName = config.getString("general.bonk-sound-target", "ENTITY_GENERIC_HURT");

            try {
                Sound playerSound = Sound.valueOf(playerSoundName.toUpperCase());
                Sound targetSound = Sound.valueOf(targetSoundName.toUpperCase());

                player.playSound(player.getLocation(), playerSound, 1.0f, 1.0f);
                target.getWorld().playSound(target.getLocation(), targetSound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + nosound);
            }
        }

        // Send global message
        if (config.getBoolean("general.global-chat-message", false) && target instanceof Player) {
            Player targetPlayer = (Player) target;
            String message = config.getString("messages.bonked", "{target} got bonked by {player}!");
            message = message.replace("{target}", targetPlayer.getName()).replace("{player}", player.getName());
            String taggedMessage = ChatColor.GOLD + "[BONK] " + message;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(taggedMessage);
            }
        }

        // Handle firework effect
        if (config.getBoolean("general.firework-on-hit", false)) {
            Location targetLocation = target.getLocation();
            Firework firework = (Firework) targetLocation.getWorld().spawnEntity(targetLocation, EntityType.FIREWORK);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            String mainColorStr = config.getString("general.main-color", "FF0000");  // Default to red
            String fadeColorStr = config.getString("general.fade-color", "FFFF00");  // Default to yellow
            String effectTypeStr = config.getString("general.effect-type", "BALL");
            int power = config.getInt("general.power", 1);

            // Convert color strings to actual colors
            Color mainColor = Color.fromRGB(255, 0, 0);
            Color fadeColor = Color.fromRGB(255, 255, 0);

            try {
                mainColor = Color.fromRGB(Integer.parseInt(mainColorStr, 16));
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid main color in config: " + mainColorStr);
            }

            try {
                fadeColor = Color.fromRGB(Integer.parseInt(fadeColorStr, 16));
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid fade color in config: " + fadeColorStr);
            }

            // Convert effect type string to FireworkEffect.Type
            FireworkEffect.Type effectType = FireworkEffect.Type.BALL;
            try {
                effectType = FireworkEffect.Type.valueOf(effectTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid effect type in config: " + effectTypeStr);
            }

            FireworkEffect effect = FireworkEffect.builder()
                    .withColor(mainColor)
                    .withFade(fadeColor)
                    .with(effectType)
                    .build();

            fireworkMeta.addEffect(effect);
            fireworkMeta.setPower(power);
            firework.setFireworkMeta(fireworkMeta);

            // Trigger explosion immediately
            firework.detonate();
        }
    }
}


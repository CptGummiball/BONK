# BONK
![Spigot](https://img.shields.io/badge/Spigot-1.20--1.21.1-yellow.svg)
![Paper](https://img.shields.io/badge/PaperMC-1.20--1.21.1-blue.svg)
![Version](https://img.shields.io/badge/Version-1.1-lightgray.svg)
![MIT License](https://img.shields.io/badge/License-MIT-green.svg)

A fun and versatile Spigot **1.20+** plugin, BONK introduces a unique item that not only knocks back entities but also offers a range of exciting features. This plugin allows you to ignite targets, create stunning visual effects as they fly away, and play customizable sound effects for both the attacker and the target. Additionally, it can broadcast a message to all players in the chat whenever someone gets bonked. Perfectly designed for server administrators and players with special permissions, **BONK** empowers you to manage mobs, players, and animals in a playful and efficient way.

## Features
- **Unique Bonk Item:** Only the specially created Bonk Item can knock back entities.
- **Custom Knockback Strength:** Configurable in config.yml.
- **Custom Damage:** Set damage amount (including zero damage) in config.yml.
- **Optional Fire Damage:** Fire Damage can be activated.
- **Item Type Customization:** Change the default Bonk item to any item type you prefer.
- **Optional Effects:** Optional Effects like Sound, Firework, Particles
- **Permission Protected:** Only players with the correct permission can obtain the Bonk Item.

## Why use BONK?

**The BONK plugin is perfect for situations where:**

- You need to discipline or manage players.
- Mobs are becoming a nuisance.
- You want to herd animals back into pens.
- You want to have fun and lightly push things around without dealing excessive damage.

## Installation

1. Download the .jar file from the releases section or build it from the source code.
2. Place the .jar file in your server's plugins folder.
3. Start or reload your server.
4. A ``config.yml`` file will be automatically generated. Adjust the settings as needed.
5. Reload the plugin ``/bonk reload``

## Configuration
The ``config.yml`` file allows you to modify the Bonk Stick's behavior and change the ingame messages:

```yaml
version: 1.1
general:
  # Texts
  Name: "BONK"
  Lore: "Not a normal stick"
  NameColor: GOLD # Valid values: Any color from the ChatColor enum in Spigot
  LoreColor: GRAY # Valid values: Any color from the ChatColor enum in Spigot
  bonk-item: "STICK" # Valid values: Any material name from the Material enum in Spigot

  global-chat-message: true # If true, a global chat message will be displayed if a player got hit by BONK

  # Properties
  knockback-strength: 1.0 # Strength of knockback (default: 1.0)
  damage: 0.0 # Amount of damage (default: 0.0)
  setonfire: false # If set to true, the target will be set on fire
  fire-duration: 5 # Duration in seconds

  # Effects
  effect-enabled: true
  knockback-effect: "PARTICLE_EFFECT" # Valid values: Any Particle from the Particle enum in Spigot

  sound-enabled: true
  bonk-sound-player: "ENTITY_PLAYER_LEVELUP" # Valid values: Any sound from the Sound enum in Spigot
  bonk-sound-target: "ENTITY_GENERIC_HURT" # Valid values: Any sound from the Sound enum in Spigot

  firework-on-hit: false
  main-color: "FF0000"   # Main Color in HEX
  fade-color: "FFFF00"    # ColorChange in (Hex)
  effect-type: "BALL"     # Type of Effect (BALL, STAR, CREEPER, BURST)
  power: 1                # Explosion radius (1-3)

  # Custom Texture
  use-custom-texture: false # If you want to use custom texture for BONK make sure you placed a file named "texture.png" in the customtexture folder.
  CustomModelData: 49721 # Change this only if there are problems with other Resourcepacks

# lang
messages:
  bonked: "{target} got bonked by {player}!"
  notplayer: "This command can only be used by players."
  nopermission: "You do not have permission to use this command."
  bonkreceive: "You have received the Bonk Stick!"
  reload: "BONK plugin configuration reloaded successfully!"
  invalidparticle: "Invalid particle effect in config: "
  invalidsound: "Invalid sound name in config."


  # Useful Links:
  # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
  # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html
  # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html
  # https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html
````

## Commands
``/bonk``: Gives the player the unique Bonk Stick. Requires the bonk.get permission.  
``bonk reload``: Reloads the plugins config  

## Permissions
``bonk.get``: Allows a player to use the /bonk command to receive the Bonk Stick. By default, only operators (op) have this permission.  
``bonk.reload``: Allows reloading of the BONK plugin configuration.  

## Usage
1. Use the ``/bonk`` command to receive the Bonk Stick.
2. Right-click or left-click an entity (e.g., mob, player, or animal) to knock it back with the configured strength.
3. The configured damage will also be applied (if set in the config).

## Building from Source
If you'd like to build the plugin yourself:

1. Clone this repository.
2. Ensure you have Java 17 and Spigot API (1.20+) setup in your development environment.
3. Compile the project and export the .jar file.

## Contributing
Feel free to open issues, fork the repository, and submit pull requests for any improvements or bug fixes. Contributions are always welcome!

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


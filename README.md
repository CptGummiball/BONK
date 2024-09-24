# BONK
![Spigot](https://img.shields.io/badge/Spigot-1.20--1.21.1-yellow.svg)
![MIT License](https://img.shields.io/badge/PaperMC-1.20--1.21.1-blue.svg)
![Version](https://img.shields.io/badge/Version-1.0-lightgray.svg)
![MIT License](https://img.shields.io/badge/License-MIT-green.svg)

A simple Spigot **1.20+** plugin that provides a unique item, the **BONK**, which knocks back entities and optionally deals configurable damage. The **BONK** is designed for server administrators or players with special permissions to push mobs, players, or animals around in a fun and efficient way.

## Features
- **Unique Bonk Item:** Only the specially created Bonk Item can knock back entities.
- **Custom Knockback Strength:** Configurable in config.yml.
- **Custom Damage:** Set damage amount (including zero damage) in config.yml.
- **Item Type Customization:** Change the default Bonk item to any item type you prefer.
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
general:
  Name: "BONK"
  Lore: "Not a normal stick"
  bonk-item: "STICK" # Valid values: Any material name from the Material enum in Spigot
  knockback-strength: 1.0 # Strength of knockback (default: 1.0)
  damage: 0.0 # Amount of damage (default: 0.0)

messages:
  notplayer: "This command can only be used by players."
  nopermission: "You do not have permission to use this command."
  bonkreceive: "You have received the Bonk Stick!"
  reload: "BONK plugin configuration reloaded successfully!"
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


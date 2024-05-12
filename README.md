 # TorchLight - Offhand Torch Light Plugin

**Minecraft API:** 1.19  
**Tested on Spigot MC Server Versions:** 1.19, 1.20  
**Releases can be found on the plugin's Spigot page:** [TorchLight](https://www.spigotmc.org/resources/torch-light.116707/)

## Overview

TorchLight is a Minecraft plugin designed to illuminate your offhand torch when you
are exploring the dark expanses of the world! This plugin provides dynamic lighting for players wielding a
torch in their offhand, adding an immersive experience to your journeys.

### Features

- **Dynamic Offhand Lighting**: Automatically illuminates the area around the player when holding a torch in their
  offhand.
- **Simple Commands**: Enable or disable the plugin using intuitive commands.
- **Efficient Cleanup**: Cleans up light sources swiftly when disabled, preventing stray lighting.

## Installation

**Tested on Spigot server 1.19, 1.20**

1. Grab the latest version of the plugin from the Spigot page: https://www.spigotmc.org/resources/torch-light.116707/
2. Place the extracted jar into your plugins folder

## Usage

- **Automatic Lighting**: Once enabled, the plugin will detect torches in players' offhand slots and automatically
  illuminate their surroundings.

- **Commands**:
    - **Enable/Disable**:
        - `/torchlight on`: (default) Enables the offhand lighting feature.
        - `/torchlight off`: Disables the offhand lighting feature.

- **Permissions**:
    - `torchlight.toggle`: Grants permission to toggle the lighting feature.

## Configuration

The config.yml will be created in the plugins folder, inside the Torch plugin folder, and it will hold any configs that
need persistence

## Contribution

Contributions are welcome! Please follow these steps:

1. Fork the repository and clone it locally.
2. Create a new feature branch: `git checkout -b feature/my-new-feature`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to your fork: `git push origin feature/my-new-feature`.
5. Submit a Pull Request.

Or you can just open a feature request
here: [GitHub Issues page](https://github.com/stdNullPtr/spigot_offhandTorch/issues) and I will implement it.

## Support

If you encounter issues or have questions about using TorchLight, please submit an issue on
our [GitHub Issues page](https://github.com/stdNullPtr/spigot_offhandTorch/issues).

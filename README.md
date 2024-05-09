# Offhand Torch Light Plugin

**Minecraft API:** 1.20+

## Overview

A Minecraft plugin designed to illuminate your offhand torch when you
are exploring the dark expanses of the world! This plugin provides seamless dynamic lighting for players wielding a
torch in their offhand, adding an immersive experience to your journeys.

### Features

- **Dynamic Offhand Lighting**: Automatically illuminates the area around the player when holding a torch in their
  offhand.
- **Simple Commands**: Enable or disable the feature using intuitive commands.
- **Efficient Cleanup**: Cleans up light sources swiftly when disabled, preventing stray lighting.

## Installation

1. **Requirements**:
    - **Java**: Make sure your server is running Java 8 or newer.
    - **Minecraft Server**: Bukkit-compatible server software (Spigot, Paper, etc.) with version 1.16 or higher.

2. **Installation Steps**:
    1. Download the latest release from the [Releases](https://github.com/stdNullPtr/spigot_offhandTorch/releases) page.
    2. Place the `OffhandTorchLight.jar` file into the `plugins` folder of your server.
    3. Start (or restart) your server.

## Usage

- **Automatic Lighting**: Once enabled, the plugin will detect torches in players' offhand slots and automatically
  illuminate their surroundings.

- **Commands**:
    - **Enable/Disable**:
        - `/torchlight on`: Enables the offhand lighting feature.
        - `/torchlight off`: Disables the offhand lighting feature.

- **Permissions**:
    - `torchlight.toggle`: Grants permission to toggle the lighting feature.

## Configuration

Currently, the plugin does not have a dedicated configuration file. The feature is controlled entirely by commands.

## Contribution

Contributions are welcome! Please follow these steps:

1. Fork the repository and clone it locally.
2. Create a new feature branch: `git checkout -b feature/my-new-feature`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to your fork: `git push origin feature/my-new-feature`.
5. Submit a Pull Request.

## Support

If you encounter issues or have questions about using Offhand Torch Light, please submit an issue on
our [GitHub Issues page](https://github.com/stdNullPtr/spigot_offhandTorch/issues).

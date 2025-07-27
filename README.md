#(All credits to https://github.com/stdNullPtr/TorchLight-SpigotMC)

# TorchLight - Offhand Torch Light Plugin

**Minecraft API:** 1.19  
**Tested on Spigot MC Server Versions:** 1.19, 1.20, 1.21  
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
- **Performance Optimized**: Throttles light updates to reduce server load

## Installation

**Tested on Spigot server 1.19, 1.20, 1.21**

1. Grab the latest version of the plugin from the Spigot page: https://www.spigotmc.org/resources/torch-light.116707/
2. Place the extracted jar into your plugins folder

## Usage

- **Automatic Lighting**: Once enabled, the plugin will detect torches in players' offhand slots and automatically
  illuminate their surroundings.

- **Commands**:
    - **Enable/Disable**:
        - `/torchlight on`: (default) Enables the offhand lighting feature.
        - `/torchlight off`: Disables the offhand lighting feature.
        - `/torchlight time <seconds[1;30]>`: Set the light fade-out timer.
        - `/torchlight status`: Shows current plugin settings.
        - `/torchlight light-level <level[1;15]>`: Controls the light level of the torch.

- **Permissions**:
    - `torchlight.toggle`: Grants permission to toggle the lighting feature.

## Configuration

The config.yml will be created in the plugins folder, inside the Torch plugin folder, and it will hold any configs that
need persistence

## Troubleshooting

### Common Issues

1. **Light blocks not appearing**:
    - Ensure the plugin is enabled (`/torchlight status`)
    - Check that players have the correct permissions
    - Some other plugins or game mechanics might interfere with block placement

2. **Performance issues**:
    - Try reducing the light timer duration (`/torchlight time 1`)
    - Set a lower light level in the configuration
    - The plugin automatically throttles light updates to minimize impact

3. **Conflicts with other plugins**:
    - TorchLight modifies AIR blocks in the world, which might conflict with area protection plugins
    - Check console for errors related to block placement permissions

### Server Performance Tips

- For large servers, consider setting a lower default light timer (1-2 seconds)
- Players in creative or spectator mode are ignored by the lighting system
- Only air blocks will be replaced with light blocks,
  until [this is implemented](https://github.com/stdNullPtr/TorchLight-SpigotMC/issues/8)

## Contributing

Contributions are welcome! Please read
our [Contributing Guidelines](https://github.com/stdNullPtr/TorchLight-SpigotMC/blob/master/CONTRIBUTING.md) before
submitting a pull request. We follow the Conventional Commits specification to maintain a clean commit history and
automate our release process.

You can contribute in several ways:

1. **Code contributions**: Fork the repository and submit a pull request
2. **Bug reports**: Open an issue on our [GitHub Issues page](https://github.com/stdNullPtr/TorchLight-SpigotMC/issues)
3. **Feature requests**: Suggest new features through our issues page
4. **Documentation**: Help improve or translate documentation

## Support

If you encounter issues or have questions about using TorchLight, please submit an issue on
our [GitHub Issues page](https://github.com/stdNullPtr/TorchLight-SpigotMC/issues).

## License

TorchLight is released under the [AGPL License](LICENSE).

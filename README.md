# CreativeEditorWands StationAPI Edition for Minecraft Beta 1.7.3

A StationAPI mod for Minecraft Beta 1.7.3 that adds world edit type properties to wooden tools when enabled.
* Mod (mostly) works on Multiplayer with GlassConfigAPI version 2.0+ used to sync configs!

## List of changes

All changes only apply when player is OP or in single-player toggle wands on/off via bedrock.

* Selection Tool (wooden axe)
  * Mode 1: Select two points (`right-click`)
  * Mode 2: Copy selection (area between the two points) over to the new area `right-click` (starting at point 1)
  * Mode 3: Fill selection (area between the two points) with whatever block is `right-clicked`
* Paint Brush (wooden sword)
  * Draw with `right-click`
    * Block ID zero means copy `right-clicked` block and use it to draw
  * Use `scroll-wheel` in inventory to select block type
  * Hold `left-shift` and use `scroll-wheel` to select block metadata
* Erase Brush (wooden shovel)
  * Erase with `right-click`
    * Block ID zero means erase any block
  * Use `scroll-wheel` in inventory to select block type
  * Hold `left-shift` and use `scroll-wheel` to select block metadata
* Block Picker (wooden pickaxe)
  * Select block ID and metadata with `right-click`
    * In single-player in inventory pick up the pickaxe and click a brush to apply the color to it
    * In multiplayer the selected block will be used for all brushes
  * Use `scroll-wheel` in inventory to select block type
  * Hold `left-shift` and use `scroll-wheel` to select block metadata
* Brush Mode (wooden hoe)
  * Use `scroll-wheel` in inventory to change brush size
    * Hit living entities to change brush size in multiplayer (also works in single-player)
  * Use `right-click` on a block to change brush type and apply brush mode
  * Use `right-click` in the air to apply brush mode
* Enable/disable creative editor wands tools with bedrock block
  * Use `right-click` to enable/disable creative editor wands
    * This is a single-player only feature

## Installation using Prism Launcher

1. Download an instance of Babric for Prism Launcher: https://github.com/babric/prism-instance
2. Install Java 17, set the instance to use it, and disable compatibility checks on the instance: https://adoptium.net/temurin/releases/
3. Add StationAPI to the mod folder for the instance: https://jenkins.glass-launcher.net/job/StationAPI/lastSuccessfulBuild/
4. (Optional) Add Mod Menu to the mod folder for the instance: https://github.com/calmilamsy/ModMenu/releases
5. (Optional) Add GlassConfigAPI 2.0+ to the mod folder for the instance: https://maven.glass-launcher.net/#/releases/net/glasslauncher/mods/GlassConfigAPI
6. Add this mod to the mod folder for the instance: https://github.com/telvarost/CreativeEditorWands-StationAPI/releases
7. Run and enjoy! 👍

## Feedback

Got any suggestions on what should be added next? Feel free to share it by [creating an issue](https://github.com/telvarost/CreativeEditorWands-StationAPI/issues/new). Know how to code and want to do it yourself? Then look below on how to get started.

## Contributing

Thanks for considering contributing! To get started fork this repository, make your changes, and create a PR.

If you are new to StationAPI consider watching the following videos on Babric/StationAPI Minecraft modding: https://www.youtube.com/watch?v=9-sVGjnGJ5s&list=PLa2JWzyvH63wGcj5-i0P12VkJG7PDyo9T

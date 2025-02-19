package com.github.telvarost.creativeeditorwands.util.command;

import com.github.telvarost.creativeeditorwands.util.StructureStorage;
import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class StructureCommand implements Command {
    HashMap<String, StructureStorage> playersStructureStorage = new HashMap<>();

    @Override
    public void command(SharedCommandSource commandSource, String[] strings) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            commandSource.sendFeedback("You must be a player to use this command");
            return;
        }

        StructureStorage structureStorage = playersStructureStorage.get(player.name);
        if (structureStorage == null) {
            playersStructureStorage.put(player.name, new StructureStorage());
            structureStorage = playersStructureStorage.get(player.name);
        }

        if (strings.length > 1) {
            switch(strings[1].toLowerCase()) {
                case "1":
                    structureStorage.setPos1(new BlockPos((int) Math.floor(player.x), (int) Math.floor(player.y), (int) Math.floor(player.z)));
                    commandSource.sendFeedback("First Position Set");
                    break;
                case "2":
                    structureStorage.setPos2(new BlockPos((int) Math.floor(player.x), (int) Math.floor(player.y), (int) Math.floor(player.z)));
                    commandSource.sendFeedback("Second Position Set");
                    break;
                case "copy":
                    if (strings.length > 2) {
                        commandSource.sendFeedback(structureStorage.copy(player.world, strings[2]));
                    } else {
                        commandSource.sendFeedback("For copying you need a name");
                    }
                    break;
                case "paste":
                    if (strings.length > 2) {
                        commandSource.sendFeedback(structureStorage.paste(player.world, (int) Math.floor(player.x), (int) Math.floor(player.y), (int) Math.floor(player.z), strings[2]));
                    } else {
                        commandSource.sendFeedback("For pasting you need a name");
                    }
                    break;
                case "fill":
                    if (strings.length > 3) {
                        try {
                            int blockId;
                            int blockMeta;
                            blockId = Integer.parseInt(strings[2]);
                            blockMeta = Integer.parseInt(strings[3]);
                            commandSource.sendFeedback(structureStorage.fill(player.world, blockId, blockMeta));
                        } catch (NumberFormatException e) {
                            commandSource.sendFeedback("The block ID or metadata value provided is not a number");
                        }
                    } else if (strings.length > 2) {
                        try {
                            int blockId;
                            blockId = Integer.parseInt(strings[2]);
                            commandSource.sendFeedback(structureStorage.fill(player.world, blockId, -1));
                        } catch (NumberFormatException e) {
                            commandSource.sendFeedback("The block ID provided is not a number");
                        }
                    } else {
                        commandSource.sendFeedback("For filling you need to provide a block ID");
                    }
                    break;
            }
        } else {
            manual(commandSource);
        }
    }

    @Override
    public String name() {
        return "structure";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /structure {1/2/copy/paste} {(copy/paste) name}");
        commandSource.sendFeedback("Info: 1 / 2 are positions you can use");
        commandSource.sendFeedback("Info: copy, copies between 1 and 2 pos");
        commandSource.sendFeedback("Info: paste, pastes structure from min corner on player pos");
        commandSource.sendFeedback("Info: name is required for copy/paste as an identifier");
        commandSource.sendFeedback("Info: fill, fills between 1 and 2 pos");
        commandSource.sendFeedback("Info: block ID (and optionally block meta) is required for fill");
    }
}

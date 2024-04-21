package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.Config;
import com.github.telvarost.creativeeditorwands.achievement.CreativeEditorWandsAchievements;
import com.github.telvarost.creativeeditorwands.items.Fish;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.FishHook;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(FishHook.class)
public abstract class FishHookMixin extends EntityBase {

    @Unique
    private int _amountOfWaterBlocks = 0;
    @Unique
    private int _calcX = 0;
    @Unique
    private int _calcY = 0;
    @Unique
    private int _calcZ = 0;
    @Unique
    private int _blockY = 0;
    @Unique
    private boolean _specialFish = false;

    public FishHookMixin(Level arg) {
        super(arg);
    }

    @Redirect(
            method = "method_956",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;spawnEntity(Lnet/minecraft/entity/EntityBase;)Z"
            )
    )
    public boolean creativeEditorWands_method_956(Level instance, EntityBase entityBase) {
        if (Config.config.enableRandomFishSizes) {
            PlayerBase player = PlayerHelper.getPlayerFromGame();
            creativeEditorWands_calculateWaterSurfaceSize(instance);

            /** - Set up for fish size calculations */
            double surfaceWaterAdjustment = (_amountOfWaterBlocks / 10000.0);
            int fishSize = 20;
            _specialFish = false;
            Random rand = new Random();
            GammaDistribution gammaDistribution = new GammaDistribution(2, 0.125 + surfaceWaterAdjustment);

            /** - Calculate size of fish */
            if (Config.config.enableBiggerFish && 250 <= _amountOfWaterBlocks) {
                fishSize = (int)Math.round(1000.0 * gammaDistribution.sample());

                if (990 <= fishSize) {
                    _specialFish = true;
                }

                if (1000 <= fishSize) {
                    int ones = rand.nextInt(10) * 10;
                    int tenths = rand.nextInt(10);

                    fishSize = 1000 + ones + tenths;

                    if (fishSize == 1099) {
                        fishSize = fishSize + rand.nextInt(2);
                    }
                } else if (100 > fishSize) {
                    fishSize = creativeEditorWands_computeNormalFishSize(gammaDistribution);
                }
            } else {
                fishSize = creativeEditorWands_computeNormalFishSize(gammaDistribution);
            }

            if (Config.config.enableNonVanillaFish) {
                if (false == _specialFish) {
                    int fishType;

                    if (Config.config.enableBiggerFish && 250 <= _amountOfWaterBlocks) {
                        fishType = rand.nextInt(4);
                        if (Config.config.calculateWaterSurfaceSize && 1 == fishType) {
                            fishType = rand.nextInt(4);
                        }
                    } else if (150 <= _amountOfWaterBlocks) {
                        fishType = rand.nextInt(3);
                    } else if (50 <= _amountOfWaterBlocks) {
                        fishType = rand.nextInt(2);
                    } else {
                        fishType = 0;
                    }

                    switch (fishType)
                    {
                        default:
                        case 0:
                            ((Item)entityBase).item = new ItemInstance(Fish.raw_common_fish, 1);

                            if (400 <= fishSize) {
                                fishSize = (int)(fishSize * 0.50);
                            } else if (300 <= fishSize) {
                                fishSize = (int)(fishSize * 0.75);
                            }
                            break;

                        case 1:
                            ((Item)entityBase).item = new ItemInstance(Fish.raw_river_fish, 1);

                            if (350 <= fishSize) {
                                fishSize = (int)(fishSize * 0.8);
                            } else {
                                fishSize = fishSize + 10;
                                fishSize = (int)(fishSize * 1.1);
                            }
                            break;

                        case 2:
                            ((Item)entityBase).item = new ItemInstance(Fish.raw_sea_fish, 1);

                            if (450 <= fishSize) {
                                fishSize = (int)(fishSize * 0.9);
                            } else {
                                fishSize = fishSize + 50;
                                fishSize = (int)(fishSize * 1.1);
                            }
                            break;

                        case 3:
                            ((Item)entityBase).item = new ItemInstance(Fish.raw_ocean_fish, 1);

                            if (null != player) {
                                player.incrementStat(CreativeEditorWandsAchievements.OCEAN_FISH);
                            }

                            if (500 <= fishSize) {
                                if (600 <= fishSize) {
                                    /** - Do nothing */
                                } else {
                                    fishSize = (int)(fishSize * 1.1);
                                }
                            } else {
                                fishSize = fishSize + 125;
                                fishSize = (int)(fishSize * 1.1);
                            }
                            break;
                    }
                }
            } else {
                if (700 < fishSize) {
                    if (null != player) {
                        player.incrementStat(CreativeEditorWandsAchievements.OCEAN_FISH);
                    }
                }
            }

            if (null != player) {
                player.incrementStat(CreativeEditorWandsAchievements.FIRST_UNIQUE_FISH);
                if (_specialFish) {
                    player.incrementStat(CreativeEditorWandsAchievements.SPECIAL_FISH);
                }
                if (150 > fishSize) {
                    player.incrementStat(CreativeEditorWandsAchievements.LITTLE_FISH);
                }
            }
            ((Item)entityBase).item.setDamage(fishSize);
        } else if (Config.config.enableNonVanillaFish) {
            int fishType;
            creativeEditorWands_calculateWaterSurfaceSize(instance);

            if (250 <= _amountOfWaterBlocks) {
                fishType = rand.nextInt(5);
                if (Config.config.calculateWaterSurfaceSize && 1 == fishType) {
                    fishType = rand.nextInt(5);
                }
            } else if (150 <= _amountOfWaterBlocks) {
                fishType = rand.nextInt(4);
            } else if (50 <= _amountOfWaterBlocks) {
                fishType = rand.nextInt(3);
            } else {
                fishType = rand.nextInt(2);
            }

            fishType = fishType - 1;

            switch (fishType) {
                default:
                case -1:
                    /** - Do nothing */
                    break;

                case 0:
                    ((Item) entityBase).item = new ItemInstance(Fish.raw_common_fish, 1);
                    break;

                case 1:
                    ((Item) entityBase).item = new ItemInstance(Fish.raw_river_fish, 1);
                    break;

                case 2:
                    ((Item) entityBase).item = new ItemInstance(Fish.raw_sea_fish, 1);
                    break;

                case 3:
                    ((Item) entityBase).item = new ItemInstance(Fish.raw_ocean_fish, 1);
                    break;
            }
        }

        return instance.spawnEntity(entityBase);
    }

    @Unique
    private int creativeEditorWands_computeNormalFishSize(GammaDistribution gammaDistribution)
    {
        int fishSize = (int)Math.round(600.0 * gammaDistribution.sample());

        if (550 <= fishSize) {
            int ones = rand.nextInt(10) * 10;
            int tenths = rand.nextInt(10);

            fishSize = 600 + ones + tenths;

            if (fishSize == 699) {
                fishSize = fishSize + rand.nextInt(2);
            }

            if (690 <= fishSize && fishSize <= 700) {
                _specialFish = true;
            }
        } else {
            fishSize = fishSize + 100;
        }

        return fishSize;
    }

    @Unique void creativeEditorWands_calculateWaterSurfaceSize(Level instance) {
        if (Config.config.calculateWaterSurfaceSize) {
            /** - Set up water block search */
            _amountOfWaterBlocks = 0;
            _blockY = 0;
            _calcX = (int) Math.floor(this.x);
            _calcY = (int) Math.floor(this.y);
            _calcZ = (int) Math.floor(this.z);

            /** - Search for starting water block */
            int blockId = instance.getTileId(_calcX, _calcY, _calcZ);
            if ((BlockBase.STILL_WATER.id == blockId)
                    || (BlockBase.FLOWING_WATER.id == blockId)
            ) {
                _amountOfWaterBlocks++;
            } else {
                _blockY--;
                blockId = instance.getTileId(_calcX, _calcY + _blockY, _calcZ);
                if ((BlockBase.STILL_WATER.id == blockId)
                        || (BlockBase.FLOWING_WATER.id == blockId)
                ) {
                    _amountOfWaterBlocks++;
                }
            }

            /** - Search for surface water blocks in eight directions from starting water block */
            if (0 < _amountOfWaterBlocks) {
                creativeEditorWands_findWaterInDirection(instance, 1, 1);   // North East
                creativeEditorWands_findWaterInDirection(instance, 1, 0);   // North
                creativeEditorWands_findWaterInDirection(instance, 1, -1);  // North West
                creativeEditorWands_findWaterInDirection(instance, -1, 1);  // South East
                creativeEditorWands_findWaterInDirection(instance, -1, 0);  // South
                creativeEditorWands_findWaterInDirection(instance, -1, -1); // South West
                creativeEditorWands_findWaterInDirection(instance, 0, 1);   // East
                creativeEditorWands_findWaterInDirection(instance, 0, -1);  // West
            }
        } else {
            _amountOfWaterBlocks = 250;
        }
    }

    @Unique
    private void creativeEditorWands_findWaterInDirection(Level instance, int dirX, int dirZ) {
        int searchX = _calcX;
        int searchY = _calcY + _blockY;
        int searchZ = _calcZ;

        while (350 > _amountOfWaterBlocks) {
            searchX = searchX + dirX;
            searchZ = searchZ + dirZ;

            int blockId = instance.getTileId(searchX, searchY, searchZ);
            if (  (BlockBase.STILL_WATER.id == blockId)
               || (BlockBase.FLOWING_WATER.id == blockId)
            ) {
                _amountOfWaterBlocks++;
            } else {
                break;
            }
        }
    }
}

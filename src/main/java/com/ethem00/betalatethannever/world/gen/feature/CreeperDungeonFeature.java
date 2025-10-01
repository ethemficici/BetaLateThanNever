package com.ethem00.betalatethannever.world.gen.feature;

import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.loot.ModLootTables;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Predicate;

public class CreeperDungeonFeature extends Feature<DefaultFeatureConfig> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityType<?>[] MOB_SPAWNER_ENTITIES = new EntityType[]{EntityType.CREEPER, EntityType.CREEPER, EntityType.CREEPER, EntityType.CREEPER};
    private static final BlockState AIR = Blocks.CAVE_AIR.getDefaultState();

    public CreeperDungeonFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        Predicate<BlockState> predicate = Feature.notInBlockTagPredicate(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        int i = 3;
        int j = 2;
        int k = -j - 1;
        int l = j + 1;
        int m = -1;
        int n = 4;
        int o = 2;
        int p = -o - 1;
        int q = o + 1;
        int r = 0;


        for (int s = k; s <= l; s++) {
            for (int t = -1; t <= 5; t++) {
                for (int u = p; u <= q; u++) {
                    BlockPos blockPos2 = blockPos.add(s, t, u);
                    boolean bl = structureWorldAccess.getBlockState(blockPos2).isSolid();
                    if (t == -1 && !bl) {
                        return false;
                    }

                    if (t == 5 && !bl) {
                        return false;
                    }

                    if ((s == k || s == l || u == p || u == q) && t == 0 && structureWorldAccess.isAir(blockPos2) && structureWorldAccess.isAir(blockPos2.up())) {
                        r++;
                    }
                }
            }
        }

        if (r >= 1 && r <= 6) {
            for (int s = k; s <= l; s++) {
                for (int t = 5; t >= -1; t--) {
                    for (int u = p; u <= q; u++) {
                        BlockPos blockPos2x = blockPos.add(s, t, u);
                        BlockState blockState = structureWorldAccess.getBlockState(blockPos2x);
                        if (s == k || t == -1 || u == p || s == l || t == 5 || u == q) {
                            if (!blockState.isOf(Blocks.CHEST)) {
                                if (random.nextInt(3) != 0) {
                                    this.setBlockStateIf(structureWorldAccess, blockPos2x, ModBlocks.MOSSY_DUNGEON_BRICKS.getDefaultState(), predicate);
                                } else {
                                    this.setBlockStateIf(structureWorldAccess, blockPos2x, ModBlocks.DUNGEON_BRICKS.getDefaultState(), predicate);
                                }
                            }
                        } else if (!blockState.isOf(Blocks.CHEST) && !blockState.isOf(Blocks.SPAWNER)) {
                            this.setBlockStateIf(structureWorldAccess, blockPos2x, AIR, predicate);
                        }
                    }
                }
            }

            for (int s = 0; s < 3; s++) {
                for (int t = 0; t < 3; t++) {
                    int ux = blockPos.getX() + random.nextInt(j * 2 + 1) - j;
                    int v = blockPos.getY();
                    int w = blockPos.getZ() + random.nextInt(o * 2 + 1) - o;
                    BlockPos blockPos3 = new BlockPos(ux, v, w);
                    if (structureWorldAccess.isAir(blockPos3)) {
                        int x = 0;

                        for (Direction direction : Direction.Type.HORIZONTAL) {
                            if (structureWorldAccess.getBlockState(blockPos3.offset(direction)).isSolid()) {
                                x++;
                            }
                        }

                        //Randomize Chest Position against wall
                        if (x == 1) {
                            int chestInt1 = (random.nextBetween(-2, 2));
                            int chestInt2;
                            if(chestInt1 == -2 || chestInt1 == 2) {
                                chestInt2 = (random.nextBetween(-2, 2));
                            } else {
                                if(random.nextBoolean()) {chestInt2 = -2;} else {chestInt2 = 2;}
                            }

                            if(random.nextBoolean()) {
                                blockPos3 = new BlockPos(blockPos.getX() + chestInt1, blockPos.getY(), blockPos.getZ() + chestInt2);
                            } else {
                                blockPos3 = new BlockPos(blockPos.getX() + chestInt2, blockPos.getY(), blockPos.getZ() + chestInt1);
                            }

                            int newRandom = random.nextInt(5);

                            if(newRandom == 1 && s != 2) {

                                this.setBlockStateIf(
                                        structureWorldAccess, blockPos3, StructurePiece.orientateChest(structureWorldAccess, blockPos3, Blocks.CHEST.getDefaultState()), predicate
                                );

                                LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, blockPos3, ModLootTables.CREEPER_DUNGEON);

                            } else if (newRandom == 0 && s == 2){

                                this.setBlockState(structureWorldAccess, blockPos3, ModBlocks.DYNAMITE_BLOCK.getDefaultState());
                            }
                            else if (s != 2){

                                this.setBlockStateIf(
                                        structureWorldAccess, blockPos3, StructurePiece.orientateChest(structureWorldAccess, blockPos3, Blocks.TRAPPED_CHEST.getDefaultState()), predicate
                                );
                                for (int tnt = 3; tnt > 0; tnt--) {
                                    if(structureWorldAccess.isAir(blockPos3) || structureWorldAccess.isAir(new BlockPos(blockPos3.getX(), blockPos3.getY() - tnt - 1, blockPos3.getZ()))) {

                                        if(blockPos3.getY() - tnt > 0) {
                                            this.setBlockState(structureWorldAccess, new BlockPos(blockPos3.getX(), blockPos3.getY() - tnt, blockPos3.getZ()), Blocks.STONE.getDefaultState());
                                        }
                                        else {
                                            this.setBlockState(structureWorldAccess, new BlockPos(blockPos3.getX(), blockPos3.getY() - tnt, blockPos3.getZ()), Blocks.DEEPSLATE.getDefaultState());
                                        }
                                    }
                                    else {
                                        if(tnt == 1) {
                                            placeBlocksAtSides(Blocks.TNT.getDefaultState(), ModBlocks.MOSSY_DUNGEON_BRICKS.getDefaultState(), new BlockPos(blockPos3.getX(), blockPos3.getY() - tnt, blockPos3.getZ()), structureWorldAccess);
                                        } else {
                                            placeBlocksAtSides(Blocks.TNT.getDefaultState(), null, new BlockPos(blockPos3.getX(), blockPos3.getY() - tnt, blockPos3.getZ()), structureWorldAccess);
                                        }
                                    }
                                }
                                LootableContainerBlockEntity.setLootTable(structureWorldAccess, random, blockPos3, ModLootTables.CREEPER_DUNGEON);

                            }
                            break;
                        }
                    }
                }
            }

            this.setBlockStateIf(structureWorldAccess, blockPos, Blocks.SPAWNER.getDefaultState(), predicate);
            if (structureWorldAccess.getBlockEntity(blockPos) instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
                mobSpawnerBlockEntity.setEntityType(this.getMobSpawnerEntity(random), random);
            } else {
                LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }

            return true;
        } else {
            return false;
        }
    }

    private void placeBlocksAtSides(BlockState centerBlock, @Nullable BlockState sideBlockOverride, BlockPos centerPos, StructureWorldAccess structureWorldAccess){

        BlockState sideBlocks;
        if(centerPos.getY() - 1 > 0) {
            sideBlocks = Blocks.STONE.getDefaultState();
        } else {
            sideBlocks = Blocks.DEEPSLATE.getDefaultState();
        }

        if(sideBlockOverride != null) {sideBlocks = sideBlockOverride;}

        this.setBlockState(structureWorldAccess, centerPos, centerBlock);
        this.setBlockState(structureWorldAccess, new BlockPos(centerPos.getX() + 1, centerPos.getY(), centerPos.getZ()), sideBlocks);
        this.setBlockState(structureWorldAccess, new BlockPos(centerPos.getX() - 1, centerPos.getY(), centerPos.getZ()), sideBlocks);
        this.setBlockState(structureWorldAccess, new BlockPos(centerPos.getX(), centerPos.getY(), centerPos.getZ() + 1), sideBlocks);
        this.setBlockState(structureWorldAccess, new BlockPos(centerPos.getX(), centerPos.getY(), centerPos.getZ() - 1), sideBlocks);
    }

    private EntityType<?> getMobSpawnerEntity(Random random) {
        return Util.getRandom(MOB_SPAWNER_ENTITIES, random);
    }
}

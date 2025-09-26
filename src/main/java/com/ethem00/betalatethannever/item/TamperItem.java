package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.block.ModBlocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class TamperItem extends Item {
    public TamperItem(Item.Settings settings) {
        super(settings);
    }

    protected static final Map<Block, BlockState> PATH_STATES = Maps.<Block, BlockState>newHashMap(
            new ImmutableMap.Builder()
                    .put(Blocks.GRASS_BLOCK, ModBlocks.GRASS_PATH.getDefaultState())
                    .put(Blocks.PODZOL, ModBlocks.PODZOL_PATH.getDefaultState())
                    .put(Blocks.MYCELIUM, ModBlocks.MYCELIUM_PATH.getDefaultState())
                    .put(Blocks.DIRT, Blocks.DIRT_PATH.getDefaultState())
                    .put(Blocks.COARSE_DIRT, ModBlocks.COARSE_DIRT_PATH.getDefaultState())
                    .put(Blocks.ROOTED_DIRT, ModBlocks.ROOTED_DIRT_PATH.getDefaultState())
                    .put(Blocks.GRAVEL, ModBlocks.GRAVEL_PATH.getDefaultState())
                    .put(ModBlocks.COARSE_GRAVEL, ModBlocks.COARSE_GRAVEL_PATH.getDefaultState())
                    .put(Blocks.SAND, ModBlocks.SAND_PATH.getDefaultState())
                    .put(Blocks.RED_SAND, ModBlocks.RED_SAND_PATH.getDefaultState())
                    .put(Blocks.MUD, ModBlocks.MUD_PATH.getDefaultState())
                    .put(Blocks.CLAY, ModBlocks.CLAY_PATH.getDefaultState())
                    .put(Blocks.SNOW_BLOCK, ModBlocks.SNOW_PATH.getDefaultState())

                    //TODO: If used on replaceable block, instant break.
                    .build()
    );

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (context.getSide() == Direction.DOWN) {
            return ActionResult.PASS;
        } else {
            PlayerEntity playerEntity = context.getPlayer();
            BlockState blockState2 = (BlockState)PATH_STATES.get(blockState.getBlock());
            BlockState blockState3 = null;
            if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {

                if(blockState.getBlock().equals(Blocks.GRASS_BLOCK) || blockState.getBlock().equals(Blocks.PODZOL) || blockState.getBlock().equals(Blocks.MYCELIUM))
                { world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); }
                if(blockState.getBlock().equals(Blocks.GRAVEL) || blockState.getBlock().equals(ModBlocks.COARSE_GRAVEL))
                { world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); }
                if(blockState.getBlock().equals(Blocks.DIRT) || blockState.getBlock().equals(Blocks.COARSE_DIRT) || blockState.getBlock().equals(Blocks.ROOTED_DIRT) || blockState.getBlock().equals(Blocks.CLAY) || blockState.getBlock().equals(Blocks.MUD))
                { world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); }
                if(blockState.getBlock().equals(Blocks.SAND) || blockState.getBlock().equals(Blocks.RED_SAND))
                { world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); }
                if(blockState.getBlock().equals(Blocks.SNOW_BLOCK))
                { world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F); }

                blockState3 = blockState2;
            } else if (blockState.getBlock() instanceof CampfireBlock && (Boolean)blockState.get(CampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, blockPos, 0);
                }

                CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                blockState3 = blockState.with(CampfireBlock.LIT, false);
            } else if(blockState.isReplaceable())
            {
                /*
                world.breakBlock(blockPos, true);
                if (playerEntity != null) {
                    context.getStack().damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
                    return ActionResult.success(world.isClient);
                }
                */
            }

            if (blockState3 != null) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState3));
                    if (playerEntity != null) {
                        context.getStack().damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
                    }
                }

                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        }
    }
}

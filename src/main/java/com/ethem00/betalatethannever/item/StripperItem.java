package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.block.CarvedCoconutBlock;
import com.ethem00.betalatethannever.block.CoconutBlock;
import com.ethem00.betalatethannever.block.ModBlocks;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class StripperItem extends Item {
    public StripperItem(Item.Settings settings) {
        super(settings);
    }

    protected static final Map<Block, Block> STRIPPED_BLOCKS = new ImmutableMap.Builder<Block, Block>()
            .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
            .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
            .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
            .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
            .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
            .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
            .put(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD)
            .put(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG)
            .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
            .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
            .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
            .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
            .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
            .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
            .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
            .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
            .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
            .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
            .put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
            .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
            .put(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK)
            .put(ModBlocks.PALM_LOG, ModBlocks.STRIPPED_PALM_LOG)
            .put(ModBlocks.PALM_WOOD, ModBlocks.STRIPPED_PALM_WOOD)
            .put(Blocks.PUMPKIN, ModBlocks.HAPPY_PUMPKIN)
            .put(ModBlocks.COCONUT, ModBlocks.CARVED_COCONUT)
            .build();

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        Optional<BlockState> optional = this.getStrippedState(blockState, playerEntity, context);
        Optional<BlockState> optional2 = Oxidizable.getDecreasedOxidationState(blockState);
        Optional<BlockState> optional3 = Optional.ofNullable((Block)((BiMap) HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock()))
                .map(block -> block.getStateWithProperties(blockState));
        ItemStack itemStack = context.getStack();
        Optional<BlockState> optional4 = Optional.empty();
        if (optional.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            optional4 = optional;
        } else if (optional2.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, WorldEvents.BLOCK_SCRAPED, blockPos, 0);
            optional4 = optional2;
        } else if (optional3.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, WorldEvents.WAX_REMOVED, blockPos, 0);
            optional4 = optional3;
        }

        if (optional4.isPresent()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            world.setBlockState(blockPos, (BlockState)optional4.get(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, (BlockState)optional4.get()));
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    private Optional<BlockState> getStrippedState(BlockState state, @Nullable PlayerEntity player, ItemUsageContext context) {

        Block stripped = STRIPPED_BLOCKS.get(state.getBlock());
        Direction direction = context.getSide();
        World world = context.getWorld();

        // Check if side is TOP (WITHOUT THIS, THE GAME CRASHES!)
        if(direction.getAxis() == Direction.Axis.Y)
        {
            direction = player.getHorizontalFacing().getOpposite();
        }

        if(state.getBlock() instanceof CoconutBlock)
        {
            if(!world.isClient) {
                ItemEntity itemEntity = new ItemEntity(
                        world,
                        context.getBlockPos().getX() + 0.5 + direction.getOffsetX() * 0.65,
                        context.getBlockPos().getY() + 0.1,
                        context.getBlockPos().getZ() + 0.5 + direction.getOffsetZ() * 0.65,
                        new ItemStack(ModItems.COCONUT_CHUNK, 2)
                );
                itemEntity.setVelocity(
                        0.05 * direction.getOffsetX() + world.random.nextDouble() * 0.02, 0.05, 0.05 * direction.getOffsetZ() + world.random.nextDouble() * 0.02
                );
                world.spawnEntity(itemEntity);
            }
            return Optional.of(ModBlocks.CARVED_COCONUT.getDefaultState().with(CarvedCoconutBlock.FACING, direction));
        }

        if(state.getBlock() instanceof PumpkinBlock)
        {

            if(!world.isClient)
            {
                ItemEntity itemEntity = new ItemEntity(
                        world,
                        context.getBlockPos().getX() + 0.5 + direction.getOffsetX() * 0.65,
                        context.getBlockPos().getY() + 0.1,
                        context.getBlockPos().getZ() + 0.5 + direction.getOffsetZ() * 0.65,
                        new ItemStack(Items.PUMPKIN_SEEDS, 4)
                );
                itemEntity.setVelocity(
                        0.05 * direction.getOffsetX() + world.random.nextDouble() * 0.02, 0.05, 0.05 * direction.getOffsetZ() + world.random.nextDouble() * 0.02
                );
                world.spawnEntity(itemEntity);
            }

            return Optional.of(ModBlocks.HAPPY_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, direction));
        }

        if (stripped == null) { return Optional.empty();}


        BlockState newState = stripped.getDefaultState();

        // Copy FACING if both have it
        if (state.contains(HorizontalFacingBlock.FACING) && newState.contains(HorizontalFacingBlock.FACING)) {
            newState = newState.with(HorizontalFacingBlock.FACING, state.get(HorizontalFacingBlock.FACING));
        }

        // Copy AXIS if both have it
        if (state.contains(PillarBlock.AXIS) && newState.contains(PillarBlock.AXIS)) {
            newState = newState.with(PillarBlock.AXIS, state.get(PillarBlock.AXIS));
        }

        return Optional.of(newState);
    }

}

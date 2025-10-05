package com.ethem00.betalatethannever.block;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class CoconutSprout extends Block implements Fertilizable {
    public static final int MAX_AGE = 2;
    public static final IntProperty AGE = Properties.AGE_2;
    protected static final int field_31062 = 4;
    protected static final int field_31063 = 5;
    protected static final int field_31064 = 2;
    protected static final int field_31065 = 6;
    protected static final int field_31066 = 7;
    protected static final int field_31067 = 3;
    protected static final int field_31068 = 8;
    protected static final int field_31069 = 9;
    protected static final int field_31070 = 4;
    protected static final VoxelShape[] AGE_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(11.0, 7.0, 6.0, 15.0, 12.0, 10.0),
            Block.createCuboidShape(9.0, 5.0, 5.0, 15.0, 12.0, 11.0),
            Block.createCuboidShape(7.0, 3.0, 4.0, 15.0, 12.0, 12.0)
    }; //TODO CHANGE THE OUTLINE SHAPES FOR THE COCONUT SPROUT

    public CoconutSprout(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return (Integer)state.get(AGE) < 2;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.random.nextInt(5) == 0) {
            int i = (Integer)state.get(AGE);
            if (i < 2) {
                world.setBlockState(pos, state.with(AGE, i + 1), Block.NOTIFY_LISTENERS);
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return state.isOf(ModBlocks.PALM_LEAVES);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int i = (Integer)state.get(AGE);
                return AGE_SHAPE[i];
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return (Integer)state.get(AGE) < 2;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(AGE, (Integer)state.get(AGE) + 1), Block.NOTIFY_LISTENERS);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}

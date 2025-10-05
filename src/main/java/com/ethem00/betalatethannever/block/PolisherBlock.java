package com.ethem00.betalatethannever.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PolisherBlock extends AbstractFurnaceBlock {

    protected PolisherBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH)
                .with(LIT, false).with(HAS_CELL, false).with(HAS_WATER, false).with(HAS_GRIT, false));
    }

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty HAS_CELL = BooleanProperty.of("has_cell");
    public static final BooleanProperty HAS_WATER = BooleanProperty.of("has_water");
    public static final BooleanProperty HAS_GRIT = BooleanProperty.of("has_grit");
    public static final VoxelShape BASE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16, 12.0, 16.0);

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FurnaceBlockEntity(pos, state);
    }

    public static final VoxelShape SHAPE_NORTH = VoxelShapes.union(
            Block.createCuboidShape(1.0, 12.0, 9.0, 7.0, 16.0, 15.0), //Bucket 6 long, 6 wide, 4 tall
            Block.createCuboidShape(9.0, 12.0, 5.0, 15, 14.0, 15.0), // Grit 10 long, 6 wide, 2 tall
            BASE_SHAPE
    );
    public static final VoxelShape SHAPE_SOUTH = VoxelShapes.union( // Not mirrored, so swap the X values
            Block.createCuboidShape(9.0, 12.0, 1.0, 15.0, 16.0, 7.0), //Bucket flipped, subtract 8 from Z
            Block.createCuboidShape(1.0, 12.0, 1.0, 7, 14.0, 11.0), //Grit flipped, subtract 4 from Z
            BASE_SHAPE
    );
    public static final VoxelShape SHAPE_EAST = VoxelShapes.union(
            Block.createCuboidShape(1.0, 12.0, 1.0, 7.0, 16.0, 7.0), //
            Block.createCuboidShape(1.0, 12.0, 9.0, 11, 14.0, 15.0),
            BASE_SHAPE
    );
    public static final VoxelShape SHAPE_WEST = VoxelShapes.union(
            Block.createCuboidShape(9.0, 12.0, 9.0, 15.0, 16.0, 15.0), // Inverse of North. Swap Min & Max, Swap X & Y
            Block.createCuboidShape(5.0, 12.0, 1.0, 15.0, 14.0, 7.0),
            BASE_SHAPE
    );

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch ((Direction)state.get(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return BASE_SHAPE;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, BlockEntityType.FURNACE);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);

            //TODO player.incrementStat(Stats.INTERACT_WITH_POLISHER);
            //TODO Tumble And Polish GUI
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, HAS_CELL, HAS_GRIT, HAS_WATER);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {
            double d = pos.getX() + 0.5;
            double e = pos.getY();
            double f = pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                world.playSound(d, e, f, SoundEvents.ENTITY_BOAT_PADDLE_WATER, SoundCategory.BLOCKS, 0.75F, 1.0F, false);
            }
            if (random.nextDouble() < 0.3) {
                world.playSound(d, e, f, SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.25F, false);
            }
            if (random.nextDouble() < 0.2) {
                world.playSound(d, e, f, SoundEvents.BLOCK_METAL_STEP, SoundCategory.BLOCKS, 0.15F, 0.55F, false);
                world.playSound(d, e, f, SoundEvents.BLOCK_METAL_STEP, SoundCategory.BLOCKS, 0.15F, 0.35F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52;
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52 : h;
            double j = random.nextDouble() * 6.0 / 16.0;
            double k = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : h;
            world.addParticle(ParticleTypes.BUBBLE_POP, d + i, e + j, f + k, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.BUBBLE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }
}
package com.ethem00.betalatethannever.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.*;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static org.apache.commons.lang3.RandomUtils.nextFloat;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class BlackpowderTrailBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final BooleanProperty WILL_EXPLODE = BooleanProperty.of("will_explode");
    public static final BooleanProperty EMPOWERED = BooleanProperty.of("empowered");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1, 16.0);

    public BlackpowderTrailBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, false).with(WILL_EXPLODE, false).with(EMPOWERED, false));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, WILL_EXPLODE, EMPOWERED);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        if(neighborState.isOf(Blocks.FIRE) && !state.get(LIT))
        {
            setOnFire(state, world, pos);
        }

        if(!world.getBlockState(pos.down()).isSolid())
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
            ItemScatterer.spawn(
                    ((ServerWorld) world),
                    pos.getX() + 0.5, // center of block
                    pos.getY() + 0.25,
                    pos.getZ() + 0.5,
                    new ItemStack(ModBlocks.BLACKPOWDER_TRAIL));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        //TODO check blocks around for fire
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {

        if(projectile.isOnFire())
        {
            setOnFire(state, world, hit.getBlockPos());
        }

        super.onProjectileHit(world, state, hit, projectile);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            if(state.get(WILL_EXPLODE)) {

                float yMod = 1f;
                if(state.get(EMPOWERED)) {
                    explode(world, pos, yMod, 2.25f);
                    yMod+=0.5;
                }
                explode(world, pos, yMod, 2.25f);
            }

            world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
        }
    }

    public void setOnFire(BlockState state, WorldAccess world, BlockPos pos)
    {
        /*TODO set lit to true, spawn smoke particles, trigger small explosion if a block is directly above, update neighbors, and replace this block with fire block */
        if(world instanceof ServerWorld) {
            world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, nextFloat(0.45f, 0.75f), nextFloat(0.5f, 1.5f));

            //Check if block is above, if so, get lit and explode. If not, only get lit.
            if(!world.isAir(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) {

                if(world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).isOf(Blocks.NETHERRACK) ) {
                    world.setBlockState(pos, state.with(WILL_EXPLODE, true).with(LIT, true).with(EMPOWERED, true), Block.NOTIFY_ALL);
                } else {
                    world.setBlockState(pos, state.with(WILL_EXPLODE, true).with(LIT, true), Block.NOTIFY_ALL);
                }
            }
            else {
                world.setBlockState(pos, state.with(LIT, true), Block.NOTIFY_ALL);
            }

            // Schedule explosion after 20 ticks (1 second)
            world.scheduleBlockTick(pos, this, nextInt(1, 3));
        }
    }

    private void explode(World world, BlockPos pos, float yMod, float powerMod) {
        float f = 4.0F;
        world.createExplosion(null, pos.getX() + 0.5f, pos.getY() + yMod, pos.getZ() + 0.5f, powerMod, true, World.ExplosionSourceType.BLOCK);
    }

}

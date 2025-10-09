package com.ethem00.betalatethannever.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

import static org.apache.commons.lang3.RandomUtils.*;

public class BlackpowderKegBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final BooleanProperty WILL_EXPLODE = BooleanProperty.of("will_explode");
    public static final BooleanProperty UNSTABLE = BooleanProperty.of("unstable");

    public BlackpowderKegBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false).with(WILL_EXPLODE, false).with(UNSTABLE, false));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, WILL_EXPLODE, UNSTABLE);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        if(neighborState.isOf(Blocks.FIRE) && !state.get(LIT) && !state.get(UNSTABLE))
        {
            world.setBlockState(pos, state.with(LIT, true).with(WILL_EXPLODE, true), Block.NOTIFY_ALL);
            //TODO: Westward ignition sound

            int randomInt = nextInt(15, 200);

            world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.BLOCKS, 0.5f, 0.1f);
            world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 0.1f);


            // Schedule explosion after 15~200 ticks (0.75~10.0 seconds)
            world.scheduleBlockTick(pos, this, randomInt);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }


    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            if(state.get(WILL_EXPLODE)) {
                if (world.getBlockState(pos).isOf(this)) {
                    world.removeBlock(pos, false);
                }
                world.addParticle(
                        ParticleTypes.SMOKE,
                        pos.getX() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
                        pos.getY() + 0.4,
                        pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
                        0.0,
                        0.005,
                        0.0
                );
                explode(world, pos, 0.5f, 1.5f);
                explode(world, pos, 0.5f, 4.25f);
                world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(state.get(LIT))
        {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 1.15;
            double f = pos.getZ() + 0.5;
            ((ClientWorld) world).addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            ((ClientWorld) world).addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
            if (random.nextInt(5) == 0) {
                for (int i = 0; i < random.nextInt(1) + 1; i++) {
                    ((ClientWorld) world).addParticle(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextFloat() / 2.0F, 5.0E-5, random.nextFloat() / 2.0F);
                }
            }

            world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.5F, 0.5F, false);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if(state.get(UNSTABLE))
        {
            int randomInt = nextInt(5, 15);

            world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.BLOCKS, 1.0f, 0.1f);
            world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.BLOCKS, 0.5f, 0.1f);
            world.scheduleBlockTick(pos, this, randomInt);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (((ServerWorld) world).getBlockState(pos).isOf(this)) {
            ((ServerWorld) world).removeBlock(pos, false);
        }
        ((ServerWorld) world).setBlockState(pos, ModBlocks.BLACKPOWDER_KEG.getDefaultState().with(LIT, true).with(WILL_EXPLODE, true).with(UNSTABLE, true), Block.NOTIFY_ALL);
    }

    private void explode(World world, BlockPos pos, float yMod, float powerMod) {
        world.createExplosion(null, pos.getX() + 0.5f, pos.getY() + yMod, pos.getZ() + 0.5f, powerMod, true, World.ExplosionSourceType.BLOCK);
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }
}

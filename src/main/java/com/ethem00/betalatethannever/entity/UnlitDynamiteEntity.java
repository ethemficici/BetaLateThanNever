package com.ethem00.betalatethannever.entity;

import com.ethem00.betalatethannever.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import com.ethem00.betalatethannever.block.DynamiteBlock;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class UnlitDynamiteEntity extends Entity implements Ownable {

    private static final TrackedData<Integer> COUNTDOWN = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int DEFAULT_COUNTDOWN = 80;
    @Nullable
    private LivingEntity causingEntity;
    private World passedWorld;

    // <-- THIS constructor signature is what Fabric's factory expects
    public UnlitDynamiteEntity(EntityType<? extends UnlitDynamiteEntity> entityType, World world) {
        super(entityType, world);
        this.passedWorld = world;
        this.intersectionChecked = true;
    }

    // convenience constructor for spawning from code (not used by Fabric factory)
    public UnlitDynamiteEntity(World world, double x, double y, double z, @Nullable LivingEntity thrower) {
        // call the main constructor with your registered entity type
        this(ModEntities.UNLIT_DYNAMITE, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * Math.PI * 2.0;
        this.passedWorld = world;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = thrower;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(COUNTDOWN, 80);
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }


        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98));
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
        }


        //Check velocity, more accurate than comparing a vector3 of doubles.
        //If velocity is lower than threshold, safely assume entity has stopped moving.
        if(this.getVelocity().lengthSquared() < 0.001 && this.isOnGround())
        {

            BlockPos pos = this.getBlockPos();
            BlockState state = getBlockState(pos);


            //Block Placement will happen here before discarding the entity.
            if (state.isReplaceable() || this.passedWorld.isAir(pos)) {
                placeDynamite(pos);
            } else if(this.passedWorld.getBlockState(this.getBlockPos()).getBlock() instanceof DynamiteBlock) {

                if(getBlockState(getPosOffset(pos, 0, 1, 0)).isReplaceable() || this.passedWorld.isAir(getPosOffset(pos, 0, 1, 0))) {
                    // Place dynamite above
                    placeDynamite(getPosOffset(pos, 0, 1, 0));
                } else {
                    int layer = checkViableDynamitePlacement(pos);


                    // -1 means dynamite should drop
                    if(layer != -1){

                        //Place above
                        if(layer == 0) {placeDynamite(getPosOffset(pos, 0, 1, 0));}

                        //Place North
                        if(layer == 1) {placeDynamite(getPosOffset(pos, 1, 0, 0));}
                        //Place North Up
                        if(layer == 5) {placeDynamite(getPosOffset(pos, 1, 1, 0));}

                        //Place East
                        if(layer == 2) {placeDynamite(getPosOffset(pos, 0, 0, 1));}
                        //Place East Up
                        if(layer == 6) {placeDynamite(getPosOffset(pos, 0, 1, 1));}

                        //Place South
                        if(layer == 3) {placeDynamite(getPosOffset(pos, -1, 0, 0));}
                        //Place South Up
                        if(layer == 7) {placeDynamite(getPosOffset(pos, -1, 1, 0));}

                        //Place West
                        if(layer == 4) {placeDynamite(getPosOffset(pos, 0, 0, -1));}
                        //Place West Up
                        if(layer == 8) {placeDynamite(getPosOffset(pos, 0, 1, -1));}

                        //Failsafe
                        if(layer >= 9){dropDynamite(pos);}
                    }

                    else dropDynamite(pos);

                }

            } else {
                dropDynamite(pos);
            }
        }
    }

    public int checkViableDynamitePlacement(BlockPos passedPos)
    {
        //Check 0 Above
        if(getBlockState(getPosOffset(passedPos, 0, 1, 0)).isReplaceable() || this.passedWorld.isAir(getPosOffset(passedPos, 0, 1, 0)))
        {
            return 1;
        }
        //Check 1 North
        if(getBlockState(getPosOffset(passedPos, 1, 0, 0)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, 1, 0, 0))) {return 1;}
        //Check 2 East
        if(getBlockState(getPosOffset(passedPos, 0, 0, 1)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, 0, 0, 1))) {return 2;}
        //Check 3 South
        if(getBlockState(getPosOffset(passedPos, -1, 0, 0)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, -1, 0, 0))) {return 3;}
        //Check 4 West
        if(getBlockState(getPosOffset(passedPos, 0, 0, -1)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, 0, 0, -1))) {return 4;}

        //Check 5 North Above
        if(getBlockState(getPosOffset(passedPos, 1, 1, 0)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, 1, 1, 0))) {return 5;}
        //Check 6 East Above
        if(getBlockState(getPosOffset(passedPos, 0, 1, 1)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, 0, 1, 1))) {return 6;}
        //Check 7 South Above
        if(getBlockState(getPosOffset(passedPos, -1, 1, 0)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, -1, 1, 0))) {return 7;}
        //Check 8 West Above
        if(getBlockState(getPosOffset(passedPos, 0, 1, -1)).isReplaceable() ||
                this.passedWorld.isAir(getPosOffset(passedPos, 0, 1, -1))) {return 8;}

        return -1;
    }

    public int checkDynamiteAboveLoop(BlockPos passedPos, int layer)
    {
        if(this.passedWorld.getBlockState(getPosOffset(passedPos, 0, 1, 0)).getBlock() instanceof DynamiteBlock)
        {
            return checkDynamiteAboveLoop(getPosOffset(passedPos, 0, 1, 0), layer + 1);
        }

        return -1;
    }

    public void placeDynamite(BlockPos pos)
    {
        this.passedWorld.setBlockState(pos, ModBlocks.DYNAMITE_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
        passedWorld.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1f, 1f);
        passedWorld.emitGameEvent(causingEntity, GameEvent.BLOCK_PLACE, pos);
        this.discard();
    }

    public BlockState getBlockState(BlockPos pos)
    {
        return this.passedWorld.getBlockState(pos);
    }

    public BlockPos getPosOffset(BlockPos pos, int xMod, int yMod, int zMod)
    {
        return new BlockPos(pos.getX() + xMod, pos.getY() + yMod, pos.getZ() + zMod);
    }

    private void dropDynamite(BlockPos pos)
    {
        ItemScatterer.spawn(
                this.getWorld(),
                pos.getX() + 0.5, // center of block
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                new ItemStack(ModBlocks.DYNAMITE_BLOCK));
        passedWorld.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1f, 1f);
        this.discard();
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Nullable
    public LivingEntity getOwner() {
        return this.causingEntity;
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15F;
    }

    public void setCountdown(int countdown) {
        this.dataTracker.set(COUNTDOWN, countdown);
    }

    public int getCountdown() {
        return this.dataTracker.get(COUNTDOWN);
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }
}


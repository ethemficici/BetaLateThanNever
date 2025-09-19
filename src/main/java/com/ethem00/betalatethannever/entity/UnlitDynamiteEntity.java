package com.ethem00.betalatethannever.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UnlitDynamiteEntity extends Entity implements Ownable {

    private static final TrackedData<Integer> COUNTDOWN = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int DEFAULT_COUNTDOWN = 80;
    @Nullable
    private LivingEntity causingEntity;

    // <-- THIS constructor signature is what Fabric's factory expects
    public UnlitDynamiteEntity(EntityType<? extends UnlitDynamiteEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    // convenience constructor for spawning from code (not used by Fabric factory)
    public UnlitDynamiteEntity(World world, double x, double y, double z, @Nullable LivingEntity thrower) {
        // call the main constructor with your registered entity type
        this(ModEntities.UNLIT_DYNAMITE, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * Math.PI * 2.0;
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
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putShort("Countdown", (short)this.getCountdown());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.setCountdown(nbt.getShort("Countdown"));
    }

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


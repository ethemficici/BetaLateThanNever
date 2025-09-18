package com.ethem00.betalatethannever.entity;

import net.minecraft.entity.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DynamiteEntity extends TntEntity {
    private static final int DEFAULT_FUSE = 80;
    @Nullable
    private LivingEntity causingEntity;

    // <-- THIS constructor signature is what Fabric's factory expects
    public DynamiteEntity(EntityType<? extends DynamiteEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    // convenience constructor for spawning from code (not used by Fabric factory)
    public DynamiteEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        // call the main constructor with your registered entity type
        this(ModEntities.DYNAMITE, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * Math.PI * 2.0;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2, -Math.cos(d) * 0.02);
        this.setFuse(DEFAULT_FUSE);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.causingEntity;
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

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.getWorld().isClient) {
                this.explode();
            }
        } else {
            this.updateWaterState();
            if(!this.getWorld().isClient) {
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.75, this.getZ(), 1, 0.02, 0.02, 0.02, 0.0);
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.FLAME, this.getX(), this.getY() + 0.95, this.getZ(), 1, 0.02, 0.02, 0.02, 0.0);
            }
        }
    }

    private void explode() {
        float f = 4.0F;
        this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.05), this.getZ(), 3.5F, World.ExplosionSourceType.TNT);
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }
}

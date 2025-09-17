package com.ethem00.betalatethannever.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.LivingEntity;
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
}

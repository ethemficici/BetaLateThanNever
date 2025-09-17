package com.ethem00.betalatethannever.entity;

import com.ethem00.betalatethannever.BetaLateThanNever;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<DynamiteEntity> DYNAMITE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(BetaLateThanNever.MOD_ID, "dynamite"),
            FabricEntityTypeBuilder.<DynamiteEntity>create(SpawnGroup.MISC, DynamiteEntity::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.98F))
                    .fireImmune()
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static void registerModEntities() {
        // no attributes for Dynamite
    }
}

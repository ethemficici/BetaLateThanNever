package com.ethem00.betalatethannever.entity;

import com.ethem00.betalatethannever.BetaLateThanNever;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<DynamiteEntity> DYNAMITE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(BetaLateThanNever.MOD_ID, "dynamite"),
            FabricEntityTypeBuilder.<DynamiteEntity>create(SpawnGroup.MISC, DynamiteEntity::new)
                    .dimensions(EntityDimensions.fixed(4/16f, 12f/16f))
                    .fireImmune()
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static final EntityType<? extends UnlitDynamiteEntity> UNLIT_DYNAMITE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(BetaLateThanNever.MOD_ID, "unlit_dynamite"),
            FabricEntityTypeBuilder.<UnlitDynamiteEntity>create(SpawnGroup.MISC, UnlitDynamiteEntity::new)
                    .dimensions(EntityDimensions.fixed(4/16f, 12f/16f))
                    .fireImmune()
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static void registerModEntities() {
        // no attributes for Dynamite
    }
}

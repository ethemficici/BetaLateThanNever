package com.ethem00.betalatethannever.world.gen.feature;

import com.ethem00.betalatethannever.BetaLateThanNever;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

public class ModFeatures {

    // LAPIS DUNGEON
    public static final Identifier LAPIS_DUNGEON_FEATURE_ID = new Identifier(BetaLateThanNever.MOD_ID, "lapis_dungeon");
    public static final Feature<DefaultFeatureConfig> LAPIS_DUNGEON_FEATURE =
            registerFeature("lapis_dungeon", LAPIS_DUNGEON_FEATURE_ID, new LapisDungeonFeature(DefaultFeatureConfig.CODEC));
    public static final Identifier LAPIS_DUNGEON_DEEP_FEATURE_ID = new Identifier(BetaLateThanNever.MOD_ID, "lapis_dungeon_deep");
    public static final Feature<DefaultFeatureConfig> LAPIS_DUNGEON_DEEP_FEATURE =
            registerFeature("lapis_dungeon_deep", LAPIS_DUNGEON_DEEP_FEATURE_ID, new LapisDungeonFeature(DefaultFeatureConfig.CODEC));

    // CREEPER DUNGEON
    public static final Identifier CREEPER_DUNGEON_FEATURE_ID = new Identifier(BetaLateThanNever.MOD_ID, "creeper_dungeon");
    public static final Feature<DefaultFeatureConfig> CREEPER_DUNGEON_FEATURE =
            registerFeature("creeper_dungeon", CREEPER_DUNGEON_FEATURE_ID, new CreeperDungeonFeature(DefaultFeatureConfig.CODEC));
    public static final Identifier CREEPER_DUNGEON_DEEP_FEATURE_ID = new Identifier(BetaLateThanNever.MOD_ID, "creeper_dungeon_deep");
    public static final Feature<DefaultFeatureConfig> CREEPER_DUNGEON_DEEP_FEATURE =
            registerFeature("creeper_dungeon_deep", CREEPER_DUNGEON_DEEP_FEATURE_ID, new CreeperDungeonFeature(DefaultFeatureConfig.CODEC));



    private static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, Identifier id, F feature) {
        return Registry.register(Registries.FEATURE, id, feature);
    }

    public static void registerModFeatures() {
        // no attributes for Dynamite
    }

}

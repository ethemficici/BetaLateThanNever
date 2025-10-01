package com.ethem00.betalatethannever;

import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.entity.ModEntities;
import com.ethem00.betalatethannever.item.ModItems;
import com.ethem00.betalatethannever.loot.ModLootTables;
import com.ethem00.betalatethannever.world.gen.feature.ModFeatures;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetaLateThanNever implements ModInitializer {
	public static final String MOD_ID = "betalatethannever";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        // Force class loading, ensures static initializers run
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModEntities.registerModEntities();
        ModFeatures.registerModFeatures();
        ModLootTables.registerModLootTables();

		LOGGER.info("[Beta Late Than Never] Initializing");


        // Features
        RegistryKey<PlacedFeature> lapisDungeonPlaced = RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                new Identifier(BetaLateThanNever.MOD_ID, "lapis_dungeon")
        );
        RegistryKey<PlacedFeature> creeperDungeonPlaced = RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                new Identifier(BetaLateThanNever.MOD_ID, "creeper_dungeon")
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                lapisDungeonPlaced
        );
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_STRUCTURES,
                creeperDungeonPlaced
        );


	}
}
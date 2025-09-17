package com.ethem00.betalatethannever;

import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.entity.ModEntities;
import com.ethem00.betalatethannever.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetaLateThanNever implements ModInitializer {
	public static final String MOD_ID = "betalatethannever";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModEntities.registerModEntities();

		LOGGER.info("[Beta Late Than Never] Initializing");
	}
}
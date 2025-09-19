package com.ethem00.betalatethannever.block;

import com.ethem00.betalatethannever.BetaLateThanNever;
import com.ethem00.betalatethannever.item.ThrowableDynamiteItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

public class ModBlocks {

    // Block Registries
    public static final Block COARSE_GRAVEL = registerBlockMethod("coarse_gravel",
            new CoarseGravelBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.SNARE).strength(0.75F).sounds(BlockSoundGroup.GRAVEL)));

    public static final Block COCONUT = registerBlockMethod("coconut",
            new PillarBlock(AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).strength(1.0F).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY).instrument(Instrument.BASEDRUM)));

    public static final Block BLACKPOWDER_TRAIL = registerBlockMethod("blackpowder_trail",
            new Block(AbstractBlock.Settings.create().noCollision().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block DYNAMITE_BLOCK = registerBlockMethod("dynamite_block",
            new DynamiteBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).notSolid().noBlockBreakParticles().pistonBehavior(PistonBehavior.DESTROY).breakInstantly().sounds(BlockSoundGroup.GRASS).burnable().solidBlock(Blocks::never).nonOpaque()));

    // Helper Methods
    private static Block registerBlockMethod(String name, Block block) {
        registerBlockItemMethod(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(BetaLateThanNever.MOD_ID, name), block);
    }

    private static void registerBlockItemMethod(String name, Block block) {
        String passedName = name;

        if(name.equals("blackpowder_trail")) {passedName = "blackpowder";}

        if (name.equals("dynamite_block")) {
            passedName = "dynamite";
            Registry.register(
                    Registries.ITEM,
                    Identifier.of(BetaLateThanNever.MOD_ID, passedName),
                    new ThrowableDynamiteItem(block, new Item.Settings().maxCount(16)) // use the `block` parameter!
            );
            return; // stop here, don’t register again
        }

        // Default Registry
            Registry.register(Registries.ITEM, Identifier.of(BetaLateThanNever.MOD_ID, passedName),
                    new BlockItem(block, new Item.Settings()));

    }


    public static void registerModBlocks() {
        BetaLateThanNever.LOGGER.info("Registering modded blocks from " + BetaLateThanNever.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(COCONUT);
            entries.add(COARSE_GRAVEL);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(BLACKPOWDER_TRAIL);
            entries.add(DYNAMITE_BLOCK);
        });

        //Register Flammable Blocks
        FlammableBlockRegistry.getDefaultInstance().add(COCONUT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(DYNAMITE_BLOCK, 100, 15);
    }
}

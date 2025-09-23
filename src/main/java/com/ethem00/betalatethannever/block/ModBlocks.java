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

    // -- Block Registries --

    // Misc
    public static final Block COARSE_GRAVEL = registerBlockMethod("coarse_gravel",
            new CoarseGravelBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.SNARE).strength(0.75F).sounds(BlockSoundGroup.GRAVEL)));

    public static final Block BLACKPOWDER_TRAIL = registerBlockMethod("blackpowder_trail",
            new Block(AbstractBlock.Settings.create().noCollision().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block DYNAMITE_BLOCK = registerBlockMethod("dynamite_block",
            new DynamiteBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).notSolid().noBlockBreakParticles().pistonBehavior(PistonBehavior.DESTROY).breakInstantly().sounds(BlockSoundGroup.GRASS).burnable().solidBlock(Blocks::never).nonOpaque()));


    //Block Entities
    public static final Block MENDER_CHEST = registerBlockMethod("mender_chest",
            new Block(AbstractBlock.Settings.copy(Blocks.ENDER_CHEST)));

    //Decoration Blocks
    public static final Block POLISHED_LAPIS_BLOCK = registerBlockMethod("polished_lapis_block",
            new Block(AbstractBlock.Settings.copy(Blocks.LAPIS_BLOCK)));
    public static final Block LAZULI_COBBLESTONE = registerBlockMethod("lazuli_cobblestone",
            new Block(AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE)));
    public static final Block BROWN_BRICKS = registerBlockMethod("brown_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN))); //GIVE THIS AN ACHIEVEMENT WHEN YOU CRAFT IT YOU MORON!
    public static final Block BLUE_BRICKS = registerBlockMethod("blue_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.LAPIS_BLUE)));

    // Coconut and Palm Tree Variants
    public static final Block COCONUT = registerBlockMethod("coconut",
            new CoconutBlock(CoconutBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).strength(1.0F).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY).instrument(Instrument.BASEDRUM)));
    public static final Block CARVED_COCONUT = registerBlockMethod("carved_coconut",
            new CoconutBlock(CoconutBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).strength(1.0F).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY).instrument(Instrument.BASEDRUM)));

    public static final Block PALM_LEAVES = registerBlockMethod("palm_leaves",
            Blocks.createLeavesBlock(BlockSoundGroup.GRASS)); // TRANSPARENT BLOCKS NEED CUTOUT RENDER LAYER WHEN CLIENT IS INITIALIZED
    public static final Block PALM_LOG = registerBlockMethod("palm_log",
            Blocks.createLogBlock(MapColor.PALE_YELLOW, MapColor.OAK_TAN)); // LOG is bark on sides and top has rings
    public static final Block STRIPPED_PALM_LOG = registerBlockMethod("stripped_palm_log",
            Blocks.createLogBlock(MapColor.PALE_YELLOW, MapColor.PALE_YELLOW)); // Top color :: Side color
    public static final Block PALM_WOOD = registerBlockMethod("palm_wood",
            Blocks.createLogBlock(MapColor.OAK_TAN, MapColor.OAK_TAN)); // WOOD is the bark on all sides.
    public static final Block STRIPPED_PALM_WOOD = registerBlockMethod("stripped_palm_wood",
            Blocks.createLogBlock(MapColor.PALE_YELLOW, MapColor.PALE_YELLOW)); // Top color :: Side color
    public static final Block PALM_PLANKS = registerBlockMethod("palm_planks",
            new Block(AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).instrument(Instrument.BASS).strength(1.5F, 2.5F).sounds(BlockSoundGroup.WOOD).burnable()));

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
            entries.add(POLISHED_LAPIS_BLOCK);
            entries.add(LAZULI_COBBLESTONE);
            entries.add(BLUE_BRICKS);
            entries.add(BROWN_BRICKS);

            entries.add(PALM_LOG);
            entries.add(PALM_WOOD);
            entries.add(STRIPPED_PALM_LOG);
            entries.add(STRIPPED_PALM_WOOD);
            entries.add(PALM_PLANKS);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(COARSE_GRAVEL);

            entries.add(COCONUT);
            entries.add(CARVED_COCONUT);

            entries.add(PALM_LEAVES);
            entries.add(PALM_LOG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(BLACKPOWDER_TRAIL);
            entries.add(DYNAMITE_BLOCK);
        });

        //Register Flammable Blocks
        FlammableBlockRegistry.getDefaultInstance().add(DYNAMITE_BLOCK, 100, 15);

        FlammableBlockRegistry.getDefaultInstance().add(COCONUT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CARVED_COCONUT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_PALM_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_PALM_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_PLANKS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_LEAVES, 30, 60);
    }
}

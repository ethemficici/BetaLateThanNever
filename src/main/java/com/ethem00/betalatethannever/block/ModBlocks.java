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
import net.minecraft.item.Items;
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
    public static final Block CHARGED_SOUL_FIRE = registerBlockMethod("charged_soul_fire",
            new CustomSoulFireBlock(AbstractBlock.Settings.copy(Blocks.SOUL_FIRE)));

    //Decoration Blocks
    public static final Block HAPPY_PUMPKIN = registerBlockMethod("happy_pumpkin",
            new WearableCarvedPumpkinBlock(AbstractBlock.Settings.copy(Blocks.CARVED_PUMPKIN))); //TODO: Some things make me smile. (ACHIEVEMENT FOR USING STRIPPER ON PUMPKIN)
    public static final Block HAPPY_JACK_O_LANTERN = registerBlockMethod("happy_jack_o_lantern",
            new CarvedPumpkinBlock(AbstractBlock.Settings.copy(Blocks.JACK_O_LANTERN)));
    public static final Block POLISHED_LAPIS_BLOCK = registerBlockMethod("polished_lapis_block",
            new Block(AbstractBlock.Settings.copy(Blocks.LAPIS_BLOCK)));
    public static final Block LAZULI_COBBLESTONE = registerBlockMethod("lazuli_cobblestone",
            new Block(AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE).mapColor(MapColor.STONE_GRAY)));


    public static final Block PINK_BRICKS = registerBlockMethod("pink_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.PINK)));
    public static final Block MAGENTA_BRICKS = registerBlockMethod("magenta_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.MAGENTA)));
    public static final Block PURPLE_BRICKS = registerBlockMethod("purple_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.PURPLE)));
    public static final Block BLUE_BRICKS = registerBlockMethod("blue_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.LAPIS_BLUE)));
    public static final Block LIGHT_BLUE_BRICKS = registerBlockMethod("light_blue_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.LIGHT_BLUE)));
    public static final Block CYAN_BRICKS = registerBlockMethod("cyan_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.CYAN)));
    public static final Block GREEN_BRICKS = registerBlockMethod("green_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.GREEN)));
    public static final Block LIME_BRICKS = registerBlockMethod("lime_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.LIME)));
    public static final Block YELLOW_BRICKS = registerBlockMethod("yellow_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.YELLOW)));
    public static final Block ORANGE_BRICKS = registerBlockMethod("orange_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.ORANGE)));
    public static final Block RED_BRICKS = registerBlockMethod("red_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.RED)));
    public static final Block BROWN_BRICKS = registerBlockMethod("brown_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BROWN))); //TODO: GIVE THIS AN ACHIEVEMENT WHEN YOU CRAFT IT YOU MORON!
    public static final Block BLACK_BRICKS = registerBlockMethod("black_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.BLACK)));
    public static final Block GRAY_BRICKS = registerBlockMethod("gray_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.GRAY)));
    public static final Block LIGHT_GRAY_BRICKS = registerBlockMethod("light_gray_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.LIGHT_GRAY)));
    public static final Block WHITE_BRICKS = registerBlockMethod("white_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.WHITE)));

    public static final Block DUNGEON_BRICKS = registerBlockMethod("dungeon_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.STONE_GRAY)));
    public static final Block MOSSY_DUNGEON_BRICKS = registerBlockMethod("mossy_dungeon_bricks",
            new Block(AbstractBlock.Settings.copy(Blocks.BRICKS).mapColor(MapColor.STONE_GRAY)));

    // Tamper Path Blocks
    public static final Block GRASS_PATH = registerBlockMethod("grass_path",
            new DirtPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).strength(0.65F).sounds(BlockSoundGroup.GRASS).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block PODZOL_PATH = registerBlockMethod("podzol_path",
            new DirtPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).strength(0.65F).sounds(BlockSoundGroup.GRASS).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block MYCELIUM_PATH = registerBlockMethod("mycelium_path",
            new DirtPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).strength(0.65F).sounds(BlockSoundGroup.GRASS).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block COARSE_DIRT_PATH = registerBlockMethod("coarse_dirt_path",
            new CoarseDirtPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).strength(0.65F).sounds(BlockSoundGroup.GRASS).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block ROOTED_DIRT_PATH = registerBlockMethod("rooted_dirt_path",
            new RootedDirtPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).strength(0.65F).sounds(BlockSoundGroup.GRASS).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block GRAVEL_PATH = registerBlockMethod("gravel_path",
            new GravelPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(0.65F).sounds(BlockSoundGroup.GRAVEL).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block COARSE_GRAVEL_PATH = registerBlockMethod("coarse_gravel_path",
            new CoarseGravelPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.DIRT_BROWN).strength(0.65F).sounds(BlockSoundGroup.GRAVEL).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block SAND_PATH = registerBlockMethod("sand_path",
            new SandPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_YELLOW).strength(0.65F).sounds(BlockSoundGroup.SAND).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block RED_SAND_PATH = registerBlockMethod("red_sand_path",
            new RedSandPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_ORANGE).strength(0.65F).sounds(BlockSoundGroup.SAND).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block MUD_PATH = registerBlockMethod("mud_path",
            new MudPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.TERRACOTTA_CYAN).strength(0.65F).sounds(BlockSoundGroup.PACKED_MUD).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block CLAY_PATH = registerBlockMethod("clay_path",
            new ClayPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).strength(0.65F).sounds(BlockSoundGroup.GRAVEL).blockVision(Blocks::always).suffocates(Blocks::always)));
    public static final Block BRICK_PATH = registerBlockMethod("brick_path",
            new DirtPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.RED).strength(0.65F).sounds(BlockSoundGroup.STONE).blockVision(Blocks::always).suffocates(Blocks::always)));

    public static final Block SNOW_PATH = registerBlockMethod("snow_path",
            new SnowPathBlock(AbstractBlock.Settings.create().mapColor(MapColor.WHITE_GRAY).strength(0.35F).sounds(BlockSoundGroup.SNOW).blockVision(Blocks::always).suffocates(Blocks::always)));

    // Coconut
    public static final Block COCONUT = registerBlockMethod("coconut", //TODO: CARVED CONVERSION
            new CoconutBlock(CoconutBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).strength(1.5F).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY).instrument(Instrument.BASEDRUM)));
    public static final Block CARVED_COCONUT = registerBlockMethod("carved_coconut",
            new CarvedCoconutBlock(CarvedCoconutBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).strength(1.5F).sounds(BlockSoundGroup.WOOD).pistonBehavior(PistonBehavior.DESTROY).instrument(Instrument.BASEDRUM)));
    public static final Block COCO_LANTERN = registerBlockMethod("coco_lantern",
            new CarvedCoconutBlock(CarvedCoconutBlock.Settings.create().mapColor(MapColor.TERRACOTTA_BROWN).strength(1.5F).sounds(BlockSoundGroup.WOOD).luminance(state -> 15).allowsSpawning(Blocks::always).pistonBehavior(PistonBehavior.DESTROY).instrument(Instrument.BASEDRUM)));
    // Palm Tree
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

            entries.addAfter(Items.MOSSY_STONE_BRICK_WALL, MOSSY_DUNGEON_BRICKS);
            entries.addAfter(Items.MOSSY_STONE_BRICK_WALL, DUNGEON_BRICKS);

            entries.addAfter(Items.BRICKS, BRICK_PATH);

            //TODO: create Lazuli Cobblestone stairs, slabs, walls, etc.
            entries.addAfter(Items.LAPIS_BLOCK, POLISHED_LAPIS_BLOCK);
            entries.addAfter(Items.LAPIS_BLOCK, LAZULI_COBBLESTONE);

            //TODO: create Palm Wood stairs, slabs, etc.
            entries.addAfter(Items.CHERRY_BUTTON, PALM_PLANKS);
            entries.addAfter(Items.CHERRY_BUTTON, STRIPPED_PALM_WOOD);
            entries.addAfter(Items.CHERRY_BUTTON, STRIPPED_PALM_LOG);
            entries.addAfter(Items.CHERRY_BUTTON, PALM_WOOD);
            entries.addAfter(Items.CHERRY_BUTTON, PALM_LOG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> {

            //Colored Order:
            //White, Light Gray, Gray, Black, Brown, Red, Orange, Yellow, Lime, Green, Cyan, Light Blue, Blue, Purple, Magenta, Pink
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, PINK_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, MAGENTA_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, PURPLE_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, BLUE_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, LIGHT_BLUE_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, CYAN_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, GREEN_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, LIME_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, YELLOW_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, ORANGE_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, RED_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, BROWN_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, BLACK_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, GRAY_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, LIGHT_GRAY_BRICKS);
            entries.addAfter(Items.PINK_GLAZED_TERRACOTTA, WHITE_BRICKS);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {

            entries.addAfter(Items.SNOW_BLOCK, SNOW_PATH);
            entries.addAfter(Items.RED_SAND, RED_SAND_PATH);
            entries.addAfter(Items.SAND, SAND_PATH);
            entries.addAfter(Items.CLAY, CLAY_PATH);
            entries.addAfter(Items.MUD, MUD_PATH);
            entries.addAfter(Items.ROOTED_DIRT, ROOTED_DIRT_PATH);
            entries.addAfter(Items.COARSE_DIRT, COARSE_DIRT_PATH);
            entries.addAfter(Items.MYCELIUM, MYCELIUM_PATH);
            entries.addAfter(Items.PODZOL, PODZOL_PATH);
            entries.addAfter(Items.GRASS_BLOCK, GRASS_PATH);

            entries.addAfter(Items.GRAVEL, COARSE_GRAVEL_PATH);
            entries.addAfter(Items.GRAVEL, COARSE_GRAVEL);
            entries.addAfter(Items.GRAVEL, GRAVEL_PATH);

            entries.addAfter(Items.JACK_O_LANTERN, COCO_LANTERN);
            entries.addAfter(Items.JACK_O_LANTERN, CARVED_COCONUT);
            entries.addAfter(Items.JACK_O_LANTERN, COCONUT);

            entries.addAfter(Items.CARVED_PUMPKIN, HAPPY_PUMPKIN);
            entries.addAfter(Items.JACK_O_LANTERN, HAPPY_JACK_O_LANTERN);

            entries.addAfter(Items.CHERRY_LEAVES, PALM_LEAVES);
            entries.addAfter(Items.CHERRY_LOG, PALM_LOG);

        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.FLINT_AND_STEEL, BLACKPOWDER_TRAIL);
            entries.addAfter(Items.ELYTRA, DYNAMITE_BLOCK);
        });

        //Register Flammable Blocks
        FlammableBlockRegistry.getDefaultInstance().add(DYNAMITE_BLOCK, 100, 15);

        FlammableBlockRegistry.getDefaultInstance().add(COCONUT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CARVED_COCONUT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(COCO_LANTERN, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_PALM_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_PALM_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_PLANKS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(PALM_LEAVES, 30, 60);
    }
}

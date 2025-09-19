package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.BetaLateThanNever;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item CHERT = registerItemMethod("chert", new Item(new Item.Settings()));
    public static final Item COCONUT_CHUNK = registerItemMethod("coconut_chunk", new Item(new Item.Settings().food(ModFoodComponents.COCONUT_CHUNK)));

    private static Item registerItemMethod(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BetaLateThanNever.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BetaLateThanNever.LOGGER.info("Registering modded items from " + BetaLateThanNever.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(CHERT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(COCONUT_CHUNK);
        });

    }
}

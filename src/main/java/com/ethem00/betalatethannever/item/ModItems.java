package com.ethem00.betalatethannever.item;

import com.ethem00.betalatethannever.BetaLateThanNever;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item CHERT = registerItemMethod("chert", new Item(new Item.Settings()));
    public static final Item COCONUT_CHUNK = registerItemMethod("coconut_chunk", new Item(new Item.Settings().food(ModFoodComponents.COCONUT_CHUNK)));
    public static final Item SOUL_CHARGE = registerItemMethod("soul_charge", new SoulChargeItem(new Item.Settings()));
    public static final Item STRIPPER = registerItemMethod("stripper", new StripperItem(new Item.Settings().maxCount(1).maxDamage(256)));
    public static final Item TAMPER = registerItemMethod("tamper", new TamperItem(new Item.Settings().maxCount(1).maxDamage(256)));


    private static Item registerItemMethod(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BetaLateThanNever.MOD_ID, name), item);
    }

    public static void registerModItems() {
        BetaLateThanNever.LOGGER.info("Registering modded items from " + BetaLateThanNever.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.FLINT, CHERT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.FIRE_CHARGE, SOUL_CHARGE);
            entries.addAfter(Items.SHEARS, TAMPER);
            entries.addAfter(Items.SHEARS, STRIPPER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addAfter(Items.MELON_SLICE, COCONUT_CHUNK);
        });

    }
}

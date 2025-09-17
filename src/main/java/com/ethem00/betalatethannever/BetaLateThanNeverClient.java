package com.ethem00.betalatethannever;

import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.client.model.DynamiteEntityModel;
import com.ethem00.betalatethannever.client.render.entity.DynamiteEntityRenderer;
import com.ethem00.betalatethannever.entity.DynamiteEntity;
import com.ethem00.betalatethannever.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class BetaLateThanNeverClient implements ClientModInitializer {

    public static final EntityModelLayer MODEL_DYNAMITE_LAYER = new EntityModelLayer(Identifier.of("betalatethannever", "cube"), "main");

    @Override
    public void onInitializeClient() {
        // To make some parts of the block transparent (like glass, saplings and doors):
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYNAMITE_BLOCK, RenderLayer.getCutout());

        EntityRendererRegistry.register(ModEntities.DYNAMITE, DynamiteEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_DYNAMITE_LAYER, DynamiteEntityModel::getTexturedModelData);
    }
}

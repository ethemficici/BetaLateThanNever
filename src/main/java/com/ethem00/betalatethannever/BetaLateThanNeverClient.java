package com.ethem00.betalatethannever;

import com.ethem00.betalatethannever.block.ModBlocks;
import com.ethem00.betalatethannever.client.model.DynamiteEntityModel;
import com.ethem00.betalatethannever.client.render.entity.DynamiteEntityRenderer;
import com.ethem00.betalatethannever.client.render.entity.UnlitDynamiteEntityRenderer;
import com.ethem00.betalatethannever.entity.ModEntities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BetaLateThanNeverClient implements ClientModInitializer {

    public static final Identifier HAPPY_PUMPKIN_BLUR = new Identifier("betalatethannever", "textures/misc/happy_pumpkinblur.png");

    public static final EntityModelLayer MODEL_DYNAMITE_LAYER = new EntityModelLayer(Identifier.of("betalatethannever", "cube"), "main");
    public static final EntityModelLayer MODEL_UNLIT_DYNAMITE_LAYER = new EntityModelLayer(Identifier.of("betalatethannever", "cube"), "main");

    @Override
    public void onInitializeClient() {

        // Grab the hub
        InGameHud inGameHud = MinecraftClient.getInstance().inGameHud;


        // To make some parts of the block transparent (like glass, saplings and doors) use BlockRenderLayerMap
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DYNAMITE_BLOCK, RenderLayer.getCutout());
        EntityRendererRegistry.register(ModEntities.DYNAMITE, DynamiteEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.UNLIT_DYNAMITE, UnlitDynamiteEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_DYNAMITE_LAYER, DynamiteEntityModel::getTexturedModelData);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHARGED_SOUL_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALM_LEAVES, RenderLayer.getCutout());
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x7cbd6b, ModBlocks.PALM_LEAVES);


        // Event bus.
        HudRenderCallback.EVENT.register((context, tickDelta) -> {

            RenderSystem.enableBlend();
            if (!MinecraftClient.isFancyGraphicsOrBetter()) {RenderSystem.enableDepthTest();}

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.options.getPerspective().isFirstPerson()) {
                ItemStack helmet = client.player.getInventory().getArmorStack(3);
                if (helmet.isOf(ModBlocks.HAPPY_PUMPKIN.asItem())) {
                    InGameHud hud = client.inGameHud;
                    renderCustomOverlay(context, HAPPY_PUMPKIN_BLUR, 1.0F);
                }
            }
        });
    }

    private void renderCustomOverlay(DrawContext context, Identifier texture, float opacity) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();



        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        context.setShaderColor(1.0F, 1.0F, 1.0F, opacity);

        RenderSystem.setShader(GameRenderer::getRenderTypeTextSeeThroughProgram);
        context.drawTexture(
                texture,
                0, 0,        // x, y
                -180,              // Negative pushes backward
                0.0F, 0.0F,     // u, v texture coords
                width, height,     // draw size
                width, height      // texture size
        );
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}

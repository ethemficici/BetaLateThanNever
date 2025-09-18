package com.ethem00.betalatethannever.client.render.entity;

import com.ethem00.betalatethannever.BetaLateThanNeverClient;
import com.ethem00.betalatethannever.client.model.DynamiteEntityModel;
import com.ethem00.betalatethannever.entity.DynamiteEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class DynamiteEntityRenderer extends EntityRenderer<DynamiteEntity> {
    private final BlockRenderManager blockRenderManager;

    public DynamiteEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new DynamiteEntityModel(context.getPart(BetaLateThanNeverClient.MODEL_DYNAMITE_LAYER));
        this.shadowRadius = 0.125F;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    private final DynamiteEntityModel model;

    @Override
    public void render(DynamiteEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // === TNT "inflate before exploding" animation ===
        int fuse = entity.getFuse();
        if (fuse - tickDelta + 1.0F < 10.0F) {
            float h = 1.0F - (fuse - tickDelta + 1.0F) / 10.0F;
            h = MathHelper.clamp(h, 0.0F, 1.0F);
            h *= h;
            h *= h;
            float scale = 1.0F + h * 0.3F;
            matrices.scale(scale, scale, scale);
        }

        // === Draw your custom model ===
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                this.model.getLayer(getTexture(entity))
        );
        this.model.render(
                matrices,
                vertexConsumer,
                light,
                OverlayTexture.getUv(fuse / 5 % 2, false), // white flash, no hurt tint
                1.0F, 1.0F, 1.0F, 1.0F
        );

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public Identifier getTexture(DynamiteEntity dynamiteEntity) {
        return Identifier.of("betalatethannever", "textures/entity/primed_dynamite.png");
    }
}

package com.ethem00.betalatethannever.client.render.entity;

import com.ethem00.betalatethannever.BetaLateThanNeverClient;
import com.ethem00.betalatethannever.client.model.DynamiteEntityModel;
import com.ethem00.betalatethannever.entity.DynamiteEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
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
        this.shadowRadius = 0.5F;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    private final DynamiteEntityModel model;

    public void render(DynamiteEntity dynamiteEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0F, 0.5F, 0.0F);
        int j = dynamiteEntity.getFuse();
        if (j - g + 1.0F < 10.0F) {
            float h = 1.0F - (j - g + 1.0F) / 10.0F;
            h = MathHelper.clamp(h, 0.0F, 1.0F);
            h *= h;
            h *= h;
            float k = 1.0F + h * 0.3F;
            matrixStack.scale(k, k, k);
        }

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5F, -0.5F, 0.5F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        TntMinecartEntityRenderer.renderFlashingBlock(this.blockRenderManager, Blocks.TNT.getDefaultState(), matrixStack, vertexConsumerProvider, i, j / 5 % 2 == 0);
        matrixStack.pop();
        super.render(dynamiteEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(DynamiteEntity dynamiteEntity) {
        return Identifier.of("betalatethannever", "textures/block/dynamite.png");
    }
}

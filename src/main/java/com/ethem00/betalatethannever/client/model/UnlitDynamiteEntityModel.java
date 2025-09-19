package com.ethem00.betalatethannever.client.model;

import com.ethem00.betalatethannever.entity.UnlitDynamiteEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class UnlitDynamiteEntityModel extends EntityModel<UnlitDynamiteEntity> {
    private final ModelPart dynamite;

    public UnlitDynamiteEntityModel(ModelPart root) {
        this.dynamite = root.getChild("dynamite");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        ModelPartData dynamite = root.addChild("dynamite",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-2.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        // Fuse pieces
        dynamite.addChild("fuse1",
                ModelPartBuilder.create()
                        .uv(28, -2)
                        .cuboid(0.0F, -12.0F, -1.0F, 0.0F, 4.0F, 2.0F),
                ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        dynamite.addChild("fuse2",
                ModelPartBuilder.create()
                        .uv(28, -2)
                        .cuboid(0.0F, -12.0F, -1.0F, 0.0F, 4.0F, 2.0F),
                ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(UnlitDynamiteEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        // You can animate fuse or add wobbling here if desired
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer,
                       int light, int overlay, float red, float green, float blue, float alpha) {
        dynamite.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
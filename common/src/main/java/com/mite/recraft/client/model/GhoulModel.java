package com.mite.recraft.client.model;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

/**
 * 食尸鬼模型。几何体与原版人形模型一致，区别在于层定义按 64x32 构建，
 * 因此可以直接使用 MITE 的原始贴图，不需要补齐到 64x64。
 *
 * <p>姿态取自 MITE 的 ModelInvisibleStalker：手臂前伸，随步行动作摆动。</p>
 */
@Environment(EnvType.CLIENT)
public class GhoulModel<S extends HumanoidRenderState> extends HumanoidModel<S> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(MiteIds.id("ghoul"), "main");

    public GhoulModel(ModelPart root) {
        super(root);
    }

    /** 层定义按 64x32 构建，对应 MITE 1.6.4 的贴图布局。 */
    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32);
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);

        float swing = Mth.sin(state.walkAnimationPos * (float) Math.PI);
        float lift = Mth.sin((1.0F - (1.0F - state.walkAnimationPos) * (1.0F - state.walkAnimationPos)) * (float) Math.PI);

        rightArm.zRot = 0.0F;
        leftArm.zRot = 0.0F;
        rightArm.yRot = -(0.1F - swing * 0.6F);
        leftArm.yRot = 0.1F - swing * 0.6F;
        rightArm.xRot = -((float) Math.PI / 2F) - (swing * 1.2F - lift * 0.4F);
        leftArm.xRot = -((float) Math.PI / 2F) - (swing * 1.2F - lift * 0.4F);

        rightArm.zRot += Mth.cos(state.ageInTicks * 0.09F) * 0.05F + 0.05F;
        leftArm.zRot -= Mth.cos(state.ageInTicks * 0.09F) * 0.05F + 0.05F;
        rightArm.xRot += Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
        leftArm.xRot -= Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
    }
}

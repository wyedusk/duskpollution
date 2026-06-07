package dev.wyedusk.duskpollution.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wyedusk.duskpollution.entity.DPGasEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class DPGasEntityRenderer extends EntityRenderer<DPGasEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public DPGasEntityRenderer(
            EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(
            DPGasEntity entity, float entityYaw, float partialTicks,
            @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        var state = entity.getBlockState();
        if (state.getRenderShape() == net.minecraft.world.level.block.RenderShape.MODEL) {
            poseStack.pushPose();
            poseStack.translate(-0.5D, 0.0D, -0.5D);
            this.blockRenderer.renderSingleBlock(
                    state,
                    poseStack,
                    buffer,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    RenderType.translucent()
            );
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(
            @NotNull DPGasEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
package dev.mim1q.mixin.client.render;

import dev.mim1q.structuredev.mixindelegates.client.StructureBlockBlockEntityRendererMixinDelegate;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlockBlockEntityRenderer.class)
abstract public class StructureBlockBlockEntityRendererMixin implements BlockEntityRenderer<StructureBlockBlockEntity> {
    @Inject(
        method = "render(Lnet/minecraft/block/entity/StructureBlockBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At("HEAD")
    )
    private void structuredev$injectRender(
        StructureBlockBlockEntity structureBlockBlockEntity,
        float f,
        MatrixStack matrixStack,
        VertexConsumerProvider vertexConsumerProvider,
        int i,
        int j,
        CallbackInfo ci
    ) {
        StructureBlockBlockEntityRendererMixinDelegate.INSTANCE.injectRender(
            structureBlockBlockEntity,
            matrixStack,
            vertexConsumerProvider
        );
    }
}

package dev.mim1q.structuredev.mixindelegates.client

import net.minecraft.block.entity.StructureBlockBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d


object StructureBlockBlockEntityRendererMixinDelegate {
    fun injectRender(
        blockEntity: StructureBlockBlockEntity,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider
    ) {
        val distance = MinecraftClient.getInstance().player?.squaredDistanceTo(Vec3d.ofCenter(blockEntity.pos)) ?: 0.0
        if (distance < 16 * 16) {
            matrixStack.push()
            matrixStack.translate(0.5, 1.75, 0.5)
            matrixStack.multiply(MinecraftClient.getInstance().entityRenderDispatcher.rotation)
            matrixStack.scale(-1 / 64f, -1 / 64f, 1 / 64f)

//            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f))
            MinecraftClient.getInstance().textRenderer.draw(
                blockEntity.structurePath,
                -MinecraftClient.getInstance().textRenderer.getWidth(blockEntity.structurePath) / 2f,
                0f,
                0xFFFFFF,
                false,
                matrixStack.peek().getPositionMatrix(),
                vertexConsumerProvider,
                TextRenderer.TextLayerType.NORMAL,
                0x80000000.toInt(),
                0xF000F0,
                false
            )
            matrixStack.pop()
        }
    }
}
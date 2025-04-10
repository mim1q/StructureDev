package dev.mim1q.structuredev.mixin;

import dev.mim1q.structuredev.mixindelegates.client.BlockItemMixinDelegate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class BlockItemMixin {
    @Inject(
        method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void structuredev$injectGetName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
        var result = BlockItemMixinDelegate.INSTANCE.getName(stack, cir.getReturnValue());
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}

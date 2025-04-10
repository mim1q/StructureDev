package dev.mim1q.structuredev.mixin;

import dev.mim1q.structuredev.access.ServerPlayerMessageRedirectAccess;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMessageRedirectMixin implements ServerPlayerMessageRedirectAccess {
  @Unique
  private @Nullable CommandOutput structuredev$outputRedirect = null;

  /// Redirect messages from the player to a configured output
  ///
  /// @reason Some mods (e.g. WorldEdit) may send the messages directly to the Player instead of the given CommandOutput
  @Inject(
    method = "sendMessage(Lnet/minecraft/text/Text;)V",
    at = @At("HEAD")
  )
  private void structuredev$redirectSendMessage(Text message, CallbackInfo ci) {
    if (this.structuredev$outputRedirect != null) {
      this.structuredev$outputRedirect.sendMessage(message);
    }
  }

  @Override
  public void structuredev$redirectMessagesTo(CommandOutput output) {
    this.structuredev$outputRedirect = output;
  }
}

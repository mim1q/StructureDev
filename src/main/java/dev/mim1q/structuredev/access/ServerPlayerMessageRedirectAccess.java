package dev.mim1q.structuredev.access;

import net.minecraft.server.command.CommandOutput;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ServerPlayerMessageRedirectAccess {
  void structuredev$redirectMessagesTo(@Nullable CommandOutput output);
  default void structuredev$stopRedirectingMessages() { structuredev$redirectMessagesTo(null); }
}

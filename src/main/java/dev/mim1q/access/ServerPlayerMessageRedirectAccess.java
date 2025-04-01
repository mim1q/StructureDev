package dev.mim1q.access;

import net.minecraft.server.command.CommandOutput;
import org.jetbrains.annotations.Nullable;

public interface ServerPlayerMessageRedirectAccess {
  void structuredev$redirectMessagesTo(@Nullable CommandOutput output);
  default void structuredev$stopRedirectingMessages() { structuredev$redirectMessagesTo(null); }
}

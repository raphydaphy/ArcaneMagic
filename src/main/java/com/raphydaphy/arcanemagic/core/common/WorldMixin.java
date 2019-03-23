package com.raphydaphy.arcanemagic.core.common;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin
{
	@Inject(at = @At("HEAD"), method = "getChunk", cancellable = true)
	private void getChunk(int int_1, int int_2, ChunkStatus chunkStatus_1, boolean boolean_1, CallbackInfoReturnable<Chunk> info)
	{
		if (((World) (Object) this) instanceof ClientWorld)
		{

		}
	}
}

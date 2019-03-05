package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.PlayerDataUpdatePacket;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements DataHolder
{
	private boolean additionalDataNeedsSync = true;

	@Inject(at = @At("TAIL"), method = "method_14203")
	private void method_14203(ServerPlayerEntity playerEntity, boolean keepEverything, CallbackInfo info) // copyFrom
	{
		this.setAdditionalData(((DataHolder) playerEntity).getAdditionalData());
		markAdditionalDataDirty();
	}

	@Inject(at = @At("TAIL"), method="method_14226")
	private void method_14226(CallbackInfo info)
	{
		if (additionalDataNeedsSync)
		{
			additionalDataNeedsSync = false;
			ArcaneMagicPacketHandler.sendToClient(new PlayerDataUpdatePacket(this.getAdditionalData()), (ServerPlayerEntity) (Object) this);
		}
	}

	@Override
	public void markAdditionalDataDirty()
	{
		additionalDataNeedsSync = true;
	}
}

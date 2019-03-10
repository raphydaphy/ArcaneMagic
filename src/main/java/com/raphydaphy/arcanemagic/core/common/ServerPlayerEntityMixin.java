package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.api.docs.IParchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.PlayerDataUpdatePacket;
import com.raphydaphy.arcanemagic.parchment.DiscoveryParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements DataHolder
{
	private boolean additionalDataNeedsSync = true;

	@Inject(at = @At("HEAD"), method = "method_14203")
	private void method_14203(ServerPlayerEntity playerEntity, boolean keepEverything, CallbackInfo info) // copyFrom
	{
		this.setAdditionalData(((DataHolder) playerEntity).getAdditionalData());
		if (this.getAdditionalData().getBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY))
		{
			this.getAdditionalData().putBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY, false);
			((PlayerEntity)(Object)this).addChatMessage(new TranslatableTextComponent("message.arcanemagic.parchment_lost").setStyle(new Style().setColor(TextFormat.DARK_PURPLE)), false);
		}
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

	@Inject(at=@At(value="HEAD"), method="onDeath")
	private void onDeath(DamageSource source, CallbackInfo info)
	{
		if (!((PlayerEntity)(Object)this).world.isClient && !((PlayerEntity)(Object)this).world.getGameRules().getBoolean("keepInventory") && !getAdditionalData().getBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY))
		{
			for (int slot = 0; slot < ((PlayerEntity)(Object)this).inventory.getInvSize(); slot++)
			{
				ItemStack stack = ((PlayerEntity)(Object)this).inventory.getInvStack(slot);
				if (stack.getItem() == ModRegistry.WRITTEN_PARCHMENT)
				{
					IParchment parchment = ParchmentRegistry.getParchment(stack);
					if (parchment instanceof DiscoveryParchment)
					{
						getAdditionalData().putBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY, true);
						getAdditionalData().putBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY, true);
						markAdditionalDataDirty();
						break;
					}
				}
			}
		}
	}

	@Override
	public void markAdditionalDataDirty()
	{
		additionalDataNeedsSync = true;
	}
}

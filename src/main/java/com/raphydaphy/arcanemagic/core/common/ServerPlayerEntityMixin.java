package com.raphydaphy.arcanemagic.core.common;

import com.mojang.authlib.GameProfile;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.Parchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.parchment.DiscoveryParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
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
public abstract class ServerPlayerEntityMixin
{
	@Inject(at = @At("HEAD"), method = "copyFrom")
	private void onPlayerClone(ServerPlayerEntity playerEntity, boolean keepEverything, CallbackInfo info) // copyFrom
	{
		if (((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY))
		{
			((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY, false);
			((PlayerEntity) (Object) this).addChatMessage(new TranslatableTextComponent("message.arcanemagic.parchment_lost").setStyle(new Style().setColor(TextFormat.DARK_PURPLE)), false);
			ArcaneMagicUtils.unlockRecipe((PlayerEntity) (Object) this, "written_parchment");
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "onDeath")
	private void onDeath(DamageSource source, CallbackInfo info)
	{
		if (!((PlayerEntity) (Object) this).world.isClient && !((PlayerEntity) (Object) this).world.getGameRules().getBoolean("keepInventory") && !((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY))
		{
			for (int slot = 0; slot < ((PlayerEntity) (Object) this).inventory.getInvSize(); slot++)
			{
				ItemStack stack = ((PlayerEntity) (Object) this).inventory.getInvStack(slot);
				if (stack.getItem() == ModRegistry.WRITTEN_PARCHMENT)
				{
					Parchment parchment = ParchmentRegistry.getParchment(stack);
					if (parchment instanceof DiscoveryParchment)
					{
						((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY, true);
						((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY, true);
						((DataHolder) this).markAdditionalDataDirty();
						break;
					}
				}
			}
		}
	}
}

package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.client.network.packet.GuiSlotUpdateS2CPacket;
import net.minecraft.container.CraftingTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.recipe.crafting.CraftingRecipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(CraftingTableContainer.class)
public abstract class CraftingTableContainerMixin implements RecipeUnlocker
{
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "java/util/Optional.get()Ljava/lang/Object;"), method = "method_17399", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void method_17399(int int_1, World world_1, PlayerEntity playerEntity_1, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo info, ServerPlayerEntity player, ItemStack stack, Optional optionalRecipe)
	{
		CraftingRecipe recipe = (CraftingRecipe) optionalRecipe.get();
		if (recipe.getId().getNamespace().equals(ArcaneMagic.DOMAIN) && !recipe.isIgnoredInRecipeBook() && !player.getRecipeBook().contains(recipe))
		{
			resultInventory.setInvStack(0, stack);
			player.networkHandler.sendPacket(new GuiSlotUpdateS2CPacket(int_1, 0, stack));
			info.cancel();
		}
	}
}

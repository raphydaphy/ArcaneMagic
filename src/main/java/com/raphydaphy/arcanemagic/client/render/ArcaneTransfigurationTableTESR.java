package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.recipe.IArcaneTransfigurationItem;
import com.raphydaphy.arcanemagic.api.recipe.IArcaneTransfigurationRecipe;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityArcaneTransfigurationTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class ArcaneTransfigurationTableTESR extends TileEntitySpecialRenderer<TileEntityArcaneTransfigurationTable>
{
	@Override
	public void render(TileEntityArcaneTransfigurationTable te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha)
	{
		te.frameAge++;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < cap.getSlots(); i++)
		{
			renderItem(cap.getStackInSlot(i), i);
		}

		EntityPlayer player = Minecraft.getMinecraft().player;
		World world = Minecraft.getMinecraft().world;

		ItemStack held = player.getHeldItemMainhand();

		if (held.isEmpty() || !(held.getItem() instanceof IArcaneTransfigurationItem))
		{
			held = player.getHeldItemOffhand();
		}

		if (!held.isEmpty() && held.getItem() instanceof IArcaneTransfigurationItem)
		{
			NonNullList<ItemStack> recipeInputs = NonNullList.withSize(9, ItemStack.EMPTY);

			for (int i = 0; i < 9; i++)
			{
				recipeInputs.set(i, cap.getStackInSlot(i));
			}

			IArcaneTransfigurationRecipe foundRecipe = ArcaneMagicAPI.getArcaneTransfigurationRecipe(player, held, recipeInputs, world);

			if (foundRecipe != null)
			{
				GLHelper.renderItemStackFancy(foundRecipe.getRecipeOutput(), new Vec3d(0, 1.5, 0), true, Color.RED,
						te.frameAge / 4, te.getWorld().getSeed());
			}
		}

		GlStateManager.popMatrix();
	}

	private void renderItem(ItemStack stack, int slot)
	{

		if (stack != null && !stack.isEmpty())
		{
			int slotX = slot;
			int slotY = 0;

			if (slot >= 6)
			{
				slotX = slot - 6;
				slotY = 2;
			} else if (slot >= 3)
			{
				slotX = slot - 3;
				slotY = 1;
			}
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate((slotX / 5.35d) + 0.315, .675, (slotY / 5.35d) + 0.315);
			GlStateManager.scale(.1f, .1f, .1f);
			if (!(stack.getItem() instanceof ItemBlock))
			{
				GlStateManager.rotate(-90, 1, 0, 0);
				GlStateManager.translate(0, 0, -0.5);
			}
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}
}

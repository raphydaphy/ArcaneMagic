package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityElementalCraftingTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class ElementalCraftingTableTESR extends TileEntitySpecialRenderer<TileEntityElementalCraftingTable>
{
	@Override
	public void render(TileEntityElementalCraftingTable te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < cap.getSlots(); i++)
		{
			renderItem(cap.getStackInSlot(i), i);
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
			GlStateManager.translate((slotX / 5.35d) + 0.315, .62, (slotY / 5.35d) + 0.315);
			GlStateManager.scale(.1f, .1f, .1f);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}
}

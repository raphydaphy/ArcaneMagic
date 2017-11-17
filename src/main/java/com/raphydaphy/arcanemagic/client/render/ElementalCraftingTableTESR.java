package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.recipe.IElementalCraftingItem;
import com.raphydaphy.arcanemagic.api.recipe.IElementalRecipe;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityElementalCraftingTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
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

		EntityPlayer player = Minecraft.getMinecraft().player;
		World world = Minecraft.getMinecraft().world;

		ItemStack held = player.getHeldItemMainhand();

		if (held.isEmpty() || !(held.getItem() instanceof IElementalCraftingItem))
		{
			held = player.getHeldItemOffhand();
		}

		if (!held.isEmpty() && held.getItem() instanceof IElementalCraftingItem)
		{
			NonNullList<ItemStack> recipeInputs = NonNullList.withSize(9, ItemStack.EMPTY);

			for (int i = 0; i < 9; i++)
			{
				recipeInputs.set(i, cap.getStackInSlot(i));
			}

			IElementalRecipe foundRecipe = ArcaneMagicAPI.getElementalCraftingRecipe(player, held, recipeInputs, world);

			if (foundRecipe != null)
			{
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();

				float frequency = 0.1f;
				double r = Math.sin(frequency * (te.getAge())) * 127 + 128;
				double g = Math.sin(frequency * (te.getAge()) + 2) * 127 + 128;
				double b = Math.sin(frequency * (te.getAge()) + 4) * 127 + 128;

				Color c = new Color((int) r, (int) g, (int) b);
				
				GLHelper.renderFancyBeams(0.5, 1.5, 0.5, c, te.getWorld().getSeed(), te.getAge(), 16, 0.5f, 30, 10);
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();

				GlStateManager.pushMatrix();

				RenderHelper.enableStandardItemLighting();
				GlStateManager.enableLighting();
				GlStateManager.translate(0.5, .48, 0.5);
				GlStateManager.scale(.18f, .18f, .18f);

				float age = -te.getAge() * 1.5f;
				GlStateManager.rotate(age, 0, 1, 0);

				GlStateManager.scale(3.5, 3.5, 3.5);
				GlStateManager.translate(0, 1.5, 0);
				GlStateManager.translate(0, Math.sin(0.2 * (te.getAge() / 2)) / 10, 0);
				GLHelper.renderItemWithTransform(te.getWorld(), foundRecipe.getRecipeOutput(),
						ItemCameraTransforms.TransformType.GROUND);

				GlStateManager.popMatrix();
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

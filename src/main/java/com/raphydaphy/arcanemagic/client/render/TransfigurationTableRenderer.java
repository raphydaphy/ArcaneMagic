package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.block.BlockItem;

public class TransfigurationTableRenderer extends BlockEntityRenderer<TransfigurationTableBlockEntity>
{
	public void render(TransfigurationTableBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		GlStateManager.pushMatrix();
		GlStateManager.translated(renderX, renderY, renderZ);
		GlStateManager.disableRescaleNormal();



		if (entity != null)
		{
			for (int slot = 0; slot < 9; slot++)
			{
				ItemStack stack = entity.getInvStack(slot);

				if (!stack.isEmpty())
				{
					int row = slot % 3;
					int col = slot / 3;

					GuiLighting.enable();
					GlStateManager.enableLighting();
					GlStateManager.pushMatrix();
					GlStateManager.translated(.69 - .19 * row, 0.695, .69 - .19 * col);
					if (!(stack.getItem() instanceof BlockItem))
					{
						GlStateManager.translated(0, -0.05, 0);
						GlStateManager.rotated(90, 1, 0, 0);
					}
					GlStateManager.scaled(.14, .14, .14);
					MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.NONE);
					//MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.REDSTONE), ModelTransformation.Type.NONE);
					GlStateManager.popMatrix();
				}
			}
		}

		GlStateManager.popMatrix();
	}
}

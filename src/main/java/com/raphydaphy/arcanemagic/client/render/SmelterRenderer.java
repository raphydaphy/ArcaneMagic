package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class SmelterRenderer extends BlockEntityRenderer<SmelterBlockEntity>
{
	public void render(SmelterBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		if (entity != null && entity.bottom)
		{
			ItemStack input = entity.getInvStack(0);
			ItemStack output1 = entity.getInvStack(1);
			ItemStack output2 = entity.getInvStack(2);

			GlStateManager.pushMatrix();
			GlStateManager.translated(renderX, renderY, renderZ);
			BlockState state = getWorld().getBlockState(entity.getPos());

			if (state.getBlock() instanceof SmelterBlock)
			{
				if (!output1.isEmpty())
				{
					if (!output2.isEmpty())
					{
						boolean depth = MinecraftClient.getInstance().getItemRenderer().getModel(output1).hasDepthInGui() || MinecraftClient.getInstance().getItemRenderer().getModel(output2).hasDepthInGui();
						GlStateManager.pushMatrix();
						renderItemPre(state);
						if (depth)
						{
							GlStateManager.translated(.125, 0, 0);
						}
						renderItem(output1);
						GlStateManager.popMatrix();

						GlStateManager.pushMatrix();
						renderItemPre(state);
						if (depth)
						{
							GlStateManager.translated(-.125, 0, 0);
						} else
						{
							GlStateManager.translated(0, .02, 0);
						}
						renderItem(output2);
						GlStateManager.popMatrix();

					} else
					{
						renderItemPre(state);
						renderItem(output1);
					}
				} else if (!input.isEmpty())
				{
					renderItemPre(state);
					renderItem(input);
				}
			}
			GlStateManager.popMatrix();
		}
	}

	private void renderItemPre(BlockState state)
	{
		ArcaneMagicUtils.rotateTo(state.get(SmelterBlock.FACING));

		GuiLighting.enable();
		GlStateManager.enableLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.translated(0.5, 0.065, 0.12);

	}

	private void renderItem(ItemStack stack)
	{
		if (!MinecraftClient.getInstance().getItemRenderer().getModel(stack).hasDepthInGui())
		{
			GlStateManager.translated(0, .068, -0.06);
			GlStateManager.rotated(90, 1, 0, 0);
			GlStateManager.scaled(0.5, 0.5, 0.5);
		}
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.GROUND);
	}
}

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
			ItemStack stack = entity.getInvStack(0);
			float ticks = ArcaneMagicUtils.lerp(entity.ticks - 1, entity.ticks, partialTicks);

			if (!stack.isEmpty())
			{
				GlStateManager.pushMatrix();
				BlockState state = getWorld().getBlockState(entity.getPos());

				if (state.getBlock() instanceof SmelterBlock)
				{
					Direction facing = state.get(SmelterBlock.FACING);
					GlStateManager.translated(renderX, renderY, renderZ);
					if (facing == Direction.EAST)
					{
						GlStateManager.rotated(-90, 0, 1, 0);
						GlStateManager.translated(0, 0, -1);
					} else if (facing == Direction.SOUTH)
					{
						GlStateManager.rotated(-180, 0, 1, 0);
						GlStateManager.translated(-1, 0, -1);
					} else if (facing == Direction.WEST)
					{
						GlStateManager.rotated(-270, 0, 1, 0);
						GlStateManager.translated(-1, 0, 0);
					}

					GuiLighting.enable();
					GlStateManager.enableLighting();
					GlStateManager.disableRescaleNormal();
					GlStateManager.translated(0.5, 0.065, 0.12);
					if (!MinecraftClient.getInstance().getItemRenderer().getModel(stack).hasDepthInGui())
					{
						GlStateManager.translated(0, .068, -0.06);
						GlStateManager.rotated(90, 1, 0, 0);
						GlStateManager.scaled(0.5, 0.5, 0.5);
					}
					MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.GROUND);
				}
				GlStateManager.popMatrix();
			}
		}
	}
}

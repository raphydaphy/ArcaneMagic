package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class SmelterRenderer extends BlockEntityRenderer<SmelterBlockEntity>
{
	private static Identifier coverTexture = new Identifier(ArcaneMagic.DOMAIN, "textures/block/smelter_interior.png");
	private static RenderUtils.TextureBounds[] cover = {
			new RenderUtils.TextureBounds(0, 0, 0, 0), // Bottom
			new RenderUtils.TextureBounds(0, 0, 0, 0), // Top
			new RenderUtils.TextureBounds(0, 0, 0, 0), // North
			new RenderUtils.TextureBounds(0, 0, 8, 6), // South
			new RenderUtils.TextureBounds(0, 0, 0, 0), // West
			new RenderUtils.TextureBounds(0, 0, 0, 0)}; // East

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
				if (entity.getSmeltTime() > 0)
				{
					RenderUtils.rotateTo(state.get(SmelterBlock.FACING));

					MinecraftClient.getInstance().getTextureManager().bindTexture(coverTexture);
					GlStateManager.disableCull();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

					Tessellator tess = Tessellator.getInstance();
					BufferBuilder builder = tess.getBufferBuilder();

					builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);
					double pixel = 1d / 16d;

					//GlStateManager.translated(0, 0, -(14 * pixel));
					RenderUtils.renderBox(builder, pixel * 4, pixel * 2, 0, pixel * 12, pixel * 8, pixel * 1, cover, new int[]{1, 1, 1, 1, 1, 1});

					tess.draw();
				} else
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
			}
			GlStateManager.popMatrix();
		}
	}

	private void renderItemPre(BlockState state)
	{
		RenderUtils.rotateTo(state.get(SmelterBlock.FACING));

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

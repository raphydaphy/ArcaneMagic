package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.UVSet;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.cooking.BlastingRecipe;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public class SmelterRenderer extends BlockEntityRenderer<SmelterBlockEntity>
{
	private static float animTime = 4;
	Identifier detail = new Identifier(ArcaneMagic.DOMAIN, "textures/block/smelter_detail.png");

	private static RenderUtils.TextureBounds[] teeth = {
			new UVSet(8, 0, 2, 8), // Bottom
			new UVSet(8, 0, 2, 8), // Top
			new UVSet(0, 0, 8, 6), // North
			new UVSet(0, 0, 8, 6), // South
			new UVSet(0, 0, 2, 6), // West
			new UVSet(0, 0, 2, 6)}; // East

	public void render(SmelterBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		if (entity != null && entity.isBottom())
		{
			ItemStack input = entity.getInvStack(0).copy();
			ItemStack output1 = entity.getInvStack(1).copy();
			ItemStack output2 = entity.getInvStack(2).copy();

			GlStateManager.pushMatrix();
			GlStateManager.translated(renderX, renderY, renderZ);
			BlockState state = getWorld().getBlockState(entity.getPos());

			if (state.getBlock() instanceof SmelterBlock)
			{
				animTime = 8f;
				int smeltTime = entity.getSmeltTime();
				boolean finishing = smeltTime >= SmelterBlockEntity.TOTAL_SMELTING_TIME - animTime;

				GlStateManager.pushMatrix();
				MinecraftClient.getInstance().getTextureManager().bindTexture(detail);
				RenderUtils.rotateTo(state.get(SmelterBlock.FACING));

				GlStateManager.disableCull();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

				Tessellator tess = Tessellator.getInstance();
				BufferBuilder builder = tess.getBufferBuilder();

				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				float startY = 2;

				if (smeltTime > 0)
				{
					if (smeltTime < animTime)
					{
						startY = ArcaneMagicUtils.lerp(startY + 4, startY, ArcaneMagicUtils.lerp(smeltTime - 1, smeltTime, partialTicks) / animTime);
					} else if (finishing)
					{
						int remaining = SmelterBlockEntity.TOTAL_SMELTING_TIME - smeltTime;
						startY = ArcaneMagicUtils.lerp(startY + 4, startY, ArcaneMagicUtils.lerp(remaining + 1, remaining, partialTicks) / (animTime + 1));
					}
				} else
				{
					startY = 6;
				}

				RenderUtils.renderCube(builder, 4, startY, 2, 8,  6, 2, teeth);
				tess.draw();
				GlStateManager.popMatrix();
				if (smeltTime < animTime || finishing)
				{
					if (finishing && !input.isEmpty())
					{
						Optional<BlastingRecipe> optionalRecipe = MinecraftClient.getInstance().world.getRecipeManager().get(RecipeType.BLASTING, new BasicInventory(input), MinecraftClient.getInstance().world);
						if (optionalRecipe.isPresent())
						{
							int remaining = SmelterBlockEntity.TOTAL_SMELTING_TIME - smeltTime;
							float interpolatedRemaining = ArcaneMagicUtils.lerp( remaining + 1, remaining, partialTicks) / (animTime + 1);

							if (interpolatedRemaining < 3.5f)
							{
								output1 = optionalRecipe.get().getOutput();
							}
							if (interpolatedRemaining < 3.2f)
							{
								output2 = optionalRecipe.get().getOutput();
							}
						}
					}
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

						GlStateManager.translated(0, 0, 0.25); // 0.128 y
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

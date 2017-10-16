package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnalyzer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnalyzerTESR extends TileEntitySpecialRenderer<TileEntityAnalyzer>
{
	@Override
	public void render(TileEntityAnalyzer te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();

		GlStateManager.translate(0, 0, 0);

		renderItem(0, te);

		GlStateManager.translate(0, 1, 0);
		renderItem(1, te);

		GlStateManager.popMatrix();
	}

	private void renderItem(int slot, TileEntityAnalyzer te)
	{
		ItemStack stack = te.getStacks()[slot];
		if (stack != null && !stack.isEmpty())
		{
			if (slot == 1)
			{
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();

				float frequency = 0.3f;
				double r = Math.sin(frequency * te.getAge()) * 127 + 128;
				double g = Math.sin(frequency * te.getAge() + 2) * 127 + 128;
				double b = Math.sin(frequency * te.getAge() + 4) * 127 + 128;

				Color rainbow = new Color((int) r, (int) g, (int) b);
				//Essence.getFromBiome(te.getWorld().getBiome(new BlockPos(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()))).getColor()
				GLHelper.renderFancyBeams(0.5, 0.55, 0.5, rainbow, te.getWorld().getSeed(), te.getAge(), 16, 0.5f, 30,
						10);
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
			}
			GlStateManager.pushMatrix();

			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.translate(0.5, .54, 0.5);
			GlStateManager.scale(.18f, .18f, .18f);

			if (!(stack.getItem() instanceof ItemBlock) && slot == 0)
			{
				GlStateManager.scale(1.2f, 1.2f, 1.2f);
				GlStateManager.rotate(90, 1, 0, 0);

				GlStateManager.rotate(-te.getAge() * 1.5f, 0, 0, 1);
			} else
			{
				float age = te.getAge() * 1.5f;
				if (slot == 1)
				{
					age = -age;
					age += 138;
				}
				GlStateManager.rotate(age, 0, 1, 0);
			}

			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

			GlStateManager.popMatrix();
		}
	}
}

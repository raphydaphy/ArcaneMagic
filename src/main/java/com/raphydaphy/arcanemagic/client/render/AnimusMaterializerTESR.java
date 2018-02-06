package com.raphydaphy.arcanemagic.client.render;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimusMaterializerTESR extends TileEntitySpecialRenderer<TileEntityAnimusMaterializer>
{
	@Override
	public void render(TileEntityAnimusMaterializer te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.enableCull();
		GlStateManager.disableAlpha();
		// pre-alpha
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);

		GlStateManager.pushMatrix();

		GlStateManager.popMatrix();
		if (lighting)

		{
			GL11.glEnable(GL11.GL_LIGHTING);
		}

		GlStateManager.depthMask(true);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
}

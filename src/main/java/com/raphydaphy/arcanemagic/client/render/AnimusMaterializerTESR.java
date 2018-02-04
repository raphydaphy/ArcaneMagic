package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimusMaterializerTESR extends TileEntitySpecialRenderer<TileEntityAnimusMaterializer> {
	@Override
	public void render(TileEntityAnimusMaterializer te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
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
		World world = Minecraft.getMinecraft().world;
		for (int x2 = -10; x2 < 10; x2++) {
			for (int y2 = -10; y2 < 10; y2++) {
				for (int z2 = -10; z2 < 10; z2++) {
					if (world.getBlockState(
							new BlockPos(te.getPos().getX() + x2, te.getPos().getY() + y2, te.getPos().getZ() + z2))
							.getBlock() == ModRegistry.ANIMA_CONJURER) {


						Color color = Anima
								.getFromBiome(world.getBiome(
										new BlockPos(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ())))
								.getColor();

						float distX = x2 - (float)x;
						float vx = 0.01625f * distX;
						
						float distY = y2 - (float)(y);
						float vy = 0.01625f * distY;
						
						ParticleUtil.spawnParticleGlowTest(world, -1.5f, 4.8f, 5.5f, vx, .053f, vy,
								color.getRed() / 256f, color.getGreen() / 256f, color.getBlue() / 256f, alpha,
								Math.min(Math.max(world.rand.nextFloat() * 6, 1.5f), 2), (int)(111));

					}
				}
			}
		}
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

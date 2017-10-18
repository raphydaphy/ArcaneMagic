package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityCrystallizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CrystallizerTESR extends TileEntitySpecialRenderer<TileEntityCrystallizer>
{
	@Override
	public void render(TileEntityCrystallizer te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
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
		World world = Minecraft.getMinecraft().world;
		for (int x2 = -10; x2 < 10; x2++)
		{
			for (int y2 = -10; y2 < 10; y2++)
			{
				for (int z2 = -10; z2 < 10; z2++)
				{
					BlockPos second = new BlockPos(x + x2, y + y2, z + z2);

					if (world.getBlockState(second).getBlock() == ModRegistry.ESSENCE_CONCENTRATOR)
					{
						Vec3d to = new Vec3d(x + 0.5, y + 2.3, z + 0.5);
						Vec3d from = new Vec3d(second.getX() + 0.5, second.getY() + 2.2, second.getZ() + 0.5);
						Vec3d dist = new Vec3d(Math.pow(to.x - from.x, 2), Math.pow(to.y - from.y, 2),
								Math.pow(to.z - from.z, 2));
						Vec3d lineFrom = new Vec3d(from.x, from.y, from.z);
						// sqrt(pow((endA-startA), 2)+pow((endB-startB), 2));
						Color color = Essence.getFromBiome(world.getBiome(new BlockPos(from.x, from.y, from.z)))
								.getColor();

						int r = color.getRed();
						int g = color.getGreen();
						int b = color.getBlue();

						GL11.glLineWidth(10);
						Tessellator tes = Tessellator.getInstance();
						BufferBuilder vb = tes.getBuffer();

						RenderHelper.disableStandardItemLighting();

						vb.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

						double radius = 0.5;

						for (int deg = 0; deg < 360; deg++)
						{
							double radians = Math.toRadians(deg);
							Vec3d vertex = new Vec3d(from.x + Math.cos(radians) * radius, from.y,
									from.z + Math.sin(radians) * radius);
							Vec3d newDist = new Vec3d(Math.pow(to.x - vertex.x, 2), Math.pow(to.y - vertex.y, 2),
									Math.pow(to.z - vertex.z, 2));
							if (newDist.x <= dist.x && newDist.z <= dist.z)
							{
								dist = newDist;
								lineFrom = vertex;
							}

							vb.pos(vertex.x, vertex.y, vertex.z).color(r, g, b, 0).endVertex();
							;
						}

						tes.draw();

						vb.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

						vb.pos(lineFrom.x, lineFrom.y, lineFrom.z).color(r, g, b, 1).endVertex();
						vb.pos(to.x, to.y, to.z).color(r, g, b, 0).endVertex();

						tes.draw();

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

package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.PipeBlock;
import com.raphydaphy.arcanemagic.block.entity.PipeBlockEntity;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.lwjgl.opengl.GL11;

public class PipeRenderer extends BlockEntityRenderer<PipeBlockEntity>
{
	private static Identifier tex = new Identifier(ArcaneMagic.DOMAIN, "textures/block/pipe.png");

	private static RenderUtils.TextureBounds[] center = {
			new RenderUtils.TextureBounds(22, 4, 18, 0, 32, 32), // Bottom
			new RenderUtils.TextureBounds(22, 4, 18, 0, 32, 32), // Top
			new RenderUtils.TextureBounds(22, 8, 18, 4, 32, 32), // North
			new RenderUtils.TextureBounds(22, 8, 18, 4, 32, 32), // South
			new RenderUtils.TextureBounds(22, 8, 18, 4, 32, 32), // West
			new RenderUtils.TextureBounds(22, 8, 18, 4, 32, 32)}; // East

	private static RenderUtils.TextureBounds[] centerConnection = {
			new RenderUtils.TextureBounds(22, 4, 18, 0, 32, 32), // Bottom
			new RenderUtils.TextureBounds(22, 4, 18, 0, 32, 32), // Top
			new RenderUtils.TextureBounds(22, 12, 18, 8, 32, 32), // North
			new RenderUtils.TextureBounds(22, 12, 18, 8, 32, 32), // South
			new RenderUtils.TextureBounds(22, 8, 18, 4, 32, 32), // West
			new RenderUtils.TextureBounds(22, 8, 18, 4, 32, 32)}; // East

	private static RenderUtils.TextureBounds[] northWestUp = {
			new RenderUtils.TextureBounds(6, 4, 0, 0, 32, 32), // Bottom
			new RenderUtils.TextureBounds(6, 4, 0, 0, 32, 32), // Top
			new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // North
			new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // South
			new RenderUtils.TextureBounds(6, 16, 0, 12, 32, 32), // West
			new RenderUtils.TextureBounds(6, 16, 0, 12, 32, 32)}; // East

	private static RenderUtils.TextureBounds[] southEastDown = {
			new RenderUtils.TextureBounds(14, 4, 8, 0, 32, 32), // Bottom
			new RenderUtils.TextureBounds(14, 4, 8, 0, 32, 32), // Top
			new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // North
			new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // South
			new RenderUtils.TextureBounds(16, 16, 10, 12, 32, 32), // West
			new RenderUtils.TextureBounds(16, 16, 10, 12, 32, 32)}; // East

	private static RenderUtils.TextureBounds[] longAll = {
			new RenderUtils.TextureBounds(17, 28, 1, 24, 32, 32), // Bottom
			new RenderUtils.TextureBounds(17, 28, 1, 24, 32, 32), // Top
			new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // North
			new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // South
			new RenderUtils.TextureBounds(17, 32, 1, 28, 32, 32), // West
			new RenderUtils.TextureBounds(17, 32, 1, 28, 32, 32)}; // East

	public void render(PipeBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);


		if (entity != null)
		{
			GlStateManager.pushMatrix();
			MinecraftClient.getInstance().getTextureManager().bindTexture(tex);
			GlStateManager.disableCull();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder builder = tess.getBufferBuilder();

			Direction.Axis centerAxis = null;

			PipeBlock.PipeConnection connectionUp = PipeBlock.PipeConnection.NONE;
			PipeBlock.PipeConnection connectionDown = PipeBlock.PipeConnection.NONE;
			PipeBlock.PipeConnection connectionNorth = PipeBlock.PipeConnection.NONE;
			PipeBlock.PipeConnection connectionSouth = PipeBlock.PipeConnection.NONE;
			PipeBlock.PipeConnection connectionEast = PipeBlock.PipeConnection.NONE;
			PipeBlock.PipeConnection connectionWest = PipeBlock.PipeConnection.NONE;

			RenderUtils.TextureBounds[] bigConnector = {
					new RenderUtils.TextureBounds(24, 8, 22, 0, 32, 32), // Bottom
					new RenderUtils.TextureBounds(24, 8, 22, 0, 32, 32), // Top
					new RenderUtils.TextureBounds(32, 8, 24, 0, 32, 32), // North
					new RenderUtils.TextureBounds(32, 8, 24, 0, 32, 32), // South
					new RenderUtils.TextureBounds(24, 8, 22, 0, 32, 32), // West
					new RenderUtils.TextureBounds(24, 8, 22, 0, 32, 32)}; // East

			boolean renderCenter = true;

			if (entity.getWorld() != null)
			{
				BlockState state = entity.getWorld().getBlockState(entity.getPos());

				if (state.getProperties().contains(PipeBlock.UP))
				{
					connectionUp = state.get(PipeBlock.UP);
					connectionDown = state.get(PipeBlock.DOWN);
					connectionNorth = state.get(PipeBlock.NORTH);
					connectionSouth = state.get(PipeBlock.SOUTH);
					connectionWest = state.get(PipeBlock.WEST);
					connectionEast = state.get(PipeBlock.EAST);

					if (connectionUp.hasConnection() || connectionDown.hasConnection())
					{
						centerAxis = Direction.Axis.Y;

						if (connectionNorth.hasConnection() || connectionSouth.hasConnection() || connectionEast.hasConnection() || connectionWest.hasConnection())
						{
							centerAxis = null;
						} else if (connectionUp.isPipe() && connectionDown.isPipe())
						{
							renderCenter = false;
						}
					} else if (connectionNorth.hasConnection() || connectionSouth.hasConnection())
					{
						centerAxis = Direction.Axis.Z;

						if (connectionEast.hasConnection() || connectionWest.hasConnection())
						{
							centerAxis = null;
						} else if (connectionNorth.isPipe() && connectionSouth.isPipe())
						{
							renderCenter = false;
						}
					} else if (connectionEast.hasConnection() || connectionWest.hasConnection())
					{
						centerAxis = Direction.Axis.X;
						if (connectionEast.isPipe() && connectionWest.isPipe())
						{
							renderCenter = false;
						}
					}
				}
			}

			double pixel = 1d / 16d;
			GlStateManager.translated(renderX, renderY, renderZ);

			if (connectionNorth.hasConnection() || connectionSouth.hasConnection() || renderCenter)
			{
				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				if (!renderCenter)
					RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, 1, longAll, new int[]{1, 1, 1, 1, 1, 1});
				else
				{
					if (connectionNorth.isPipe())
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, pixel * 6, northWestUp, new int[]{1, 1, 1, 1, 1, 1});
					else if (connectionNorth.isBlock())
					{
						RenderUtils.renderBox(builder, pixel * 4, pixel * 4, 0, pixel * 12, pixel * 12, pixel * 2, bigConnector, new int[]{1, 1, 1, 1, 1, 1});
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 2, pixel * 10, pixel * 10, pixel * 6, center, new int[]{1, 1, 1, 1, 1, 1});
					}
					if (connectionSouth.isPipe())
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, 1, southEastDown, new int[]{1, 1, -1, -1, 1, 1});
					else if (connectionSouth.isBlock())
					{
						RenderUtils.renderBox(builder, pixel * 4, pixel * 4, pixel * 14, pixel * 12, pixel * 12, 1, bigConnector, new int[]{1, 1, 1, 1, 1, 1});
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, pixel * 14, center, new int[]{1, 1, -1, -1, 1, 1});
					}
					if (centerAxis == null || centerAxis == Direction.Axis.Z)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, centerAxis == null ? center : centerConnection, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();
			}

			GlStateManager.rotated(90, 0, 1, 0);
			GlStateManager.translated(-1, 0, 0);

			if (connectionEast.hasConnection() || connectionWest.hasConnection())
			{
				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				if (!renderCenter)
					RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, 1, longAll, new int[]{1, 1, 1, 1, 1, 1});
				else
				{
					if (connectionEast.isPipe())
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, 1, southEastDown, new int[]{1, 1, 1, 1, 1, 1});
					else if (connectionEast.isBlock())
					{
						RenderUtils.renderBox(builder, pixel * 4, pixel * 4, pixel * 14, pixel * 12, pixel * 12, 1, bigConnector, new int[]{1, 1, 1, 1, 1, 1});
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, pixel * 14, center, new int[]{1, 1, 1, 1, 1, 1});
					}
					if (connectionWest.isPipe())
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 0, pixel * 10, pixel * 10, pixel * 6, northWestUp, new int[]{1, 1, -1, -1, 1, 1});
					else if (connectionWest.isBlock())
					{
						RenderUtils.renderBox(builder, pixel * 4, pixel * 4, 0, pixel * 12, pixel * 12, pixel * 2, bigConnector, new int[]{1, 1, 1, 1, 1, 1});
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 2, pixel * 10, pixel * 10, pixel * 6, center, new int[]{1, 1, 1, 1, 1, 1});
					}
					if (centerAxis == Direction.Axis.X)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, centerConnection, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();
			}

			GlStateManager.rotated(90, 1, 0, 0);
			GlStateManager.translated(0, 0, -1);

			if (connectionUp.hasConnection() || connectionDown.hasConnection())
			{
				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				if (!renderCenter)
					RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, 1, longAll, new int[]{1, 1, 1, 1, 1, 1});
				else
				{
					if (connectionUp.isPipe())
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, pixel * 6, northWestUp, new int[]{1, 1, 1, 1, 1, 1});
					else if (connectionUp.isBlock())
					{
						RenderUtils.renderBox(builder, pixel * 4, pixel * 4, 0, pixel * 12, pixel * 12, pixel * 2, bigConnector, new int[]{1, 1, 1, 1, 1, 1});
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 2, pixel * 10, pixel * 10, pixel * 6, center, new int[]{1, 1, 1, 1, 1, 1});
					}
					if (connectionDown.isPipe())
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, 1, southEastDown, new int[]{1, 1, -1, -1, 1, 1});
					else if (connectionDown.isBlock())
					{
						RenderUtils.renderBox(builder, pixel * 4, pixel * 4, pixel * 14, pixel * 12, pixel * 12, 1, bigConnector, new int[]{1, 1, 1, 1, 1, 1});
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, pixel * 14, center, new int[]{1, 1, -1, -1, 1, 1});
					}
					if (centerAxis == Direction.Axis.Y)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, centerConnection, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();
			}
			GlStateManager.popMatrix();
		}
	}
}

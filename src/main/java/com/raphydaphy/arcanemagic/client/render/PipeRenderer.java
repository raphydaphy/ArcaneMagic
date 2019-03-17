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

			boolean connectsUp = false;
			boolean connectsDown = false;
			boolean connectsNorth = false;
			boolean connectsSouth = false;
			boolean connectsEast = false;
			boolean connectsWest = false;

			boolean renderCenter = true;

			if (entity.getWorld() != null)
			{
				BlockState state = entity.getWorld().getBlockState(entity.getPos());

				if (state.getProperties().contains(PipeBlock.UP))
				{
					connectsUp = state.get(PipeBlock.UP);
					connectsDown = state.get(PipeBlock.DOWN);
					connectsNorth = state.get(PipeBlock.NORTH);
					connectsSouth = state.get(PipeBlock.SOUTH);
					connectsWest = state.get(PipeBlock.WEST);
					connectsEast = state.get(PipeBlock.EAST);

					if (connectsUp || connectsDown)
					{
						centerAxis = Direction.Axis.Y;

						if (connectsNorth || connectsSouth || connectsEast || connectsWest)
						{
							centerAxis = null;
						} else if (connectsUp && connectsDown)
						{
							renderCenter = false;
						}
					} else if (connectsNorth || connectsSouth)
					{
						centerAxis = Direction.Axis.Z;

						if (connectsEast || connectsWest)
						{
							centerAxis = null;
						} else if (connectsNorth && connectsSouth)
						{
							renderCenter = false;
						}
					} else if (connectsEast || connectsWest)
					{
						centerAxis = Direction.Axis.X;
						if (connectsEast && connectsWest)
						{
							renderCenter = false;
						}
					}
				}
			}

			double pixel = 1d / 16d;
			GlStateManager.translated(renderX, renderY, renderZ);

			RenderUtils.TextureBounds[] longAll = {
					new RenderUtils.TextureBounds(17, 28, 1, 24, 32, 32), // Bottom
					new RenderUtils.TextureBounds(17, 28, 1, 24, 32, 32), // Top
					new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // North
					new RenderUtils.TextureBounds(0, 0, 0, 0, 32, 32), // South
					new RenderUtils.TextureBounds(17, 32, 1, 28, 32, 32), // West
					new RenderUtils.TextureBounds(17, 32, 1, 28, 32, 32)}; // East

			if (connectsNorth || connectsSouth || renderCenter)
			{
				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				if (!renderCenter)
					RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, 1, longAll, new int[]{1, 1, 1, 1, 1, 1});
				else
				{
					if (connectsNorth)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, pixel * 6, northWestUp, new int[]{1, 1, 1, 1, 1, 1});
					if (connectsSouth)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, 1, southEastDown, new int[]{1, 1, -1, -1, 1, 1});
					if (centerAxis == null || centerAxis == Direction.Axis.Z)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, centerAxis == null ? center : centerConnection, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();
			}

			GlStateManager.rotated(90, 0, 1, 0);
			GlStateManager.translated(-1, 0, 0);

			if (connectsEast || connectsWest)
			{
				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				if (!renderCenter)
					RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, 1, longAll, new int[]{1, 1, 1, 1, 1, 1});
				else
				{
					if (connectsEast)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, 1, southEastDown, new int[]{1, 1, 1, 1, 1, 1});
					if (connectsWest)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 0, pixel * 10, pixel * 10, pixel * 6, northWestUp, new int[]{1, 1, -1, -1, 1, 1});
					if (centerAxis == Direction.Axis.X)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, centerConnection, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();
			}

			GlStateManager.rotated(90, 1, 0, 0);
			GlStateManager.translated(0, 0, -1);

			if (connectsUp || connectsDown)
			{
				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

				if (!renderCenter)
					RenderUtils.renderBox(builder, pixel * 6, pixel * 6, 0, pixel * 10, pixel * 10, 1, longAll, new int[]{1, 1, 1, 1, 1, 1});
				else
				{
					if (connectsUp)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 0, pixel * 10, pixel * 10, pixel * 6, northWestUp, new int[]{1, 1, 1, 1, 1, 1});
					if (connectsDown)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, 1, southEastDown, new int[]{1, 1, -1, -1, 1, 1});
					if (centerAxis == Direction.Axis.Y)
						RenderUtils.renderBox(builder, pixel * 6, pixel * 6, pixel * 6, pixel * 10, pixel * 10, pixel * 10, centerConnection, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();
			}
			GlStateManager.popMatrix();
		}
	}
}

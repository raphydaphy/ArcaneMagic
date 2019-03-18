package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.PumpBlock;
import com.raphydaphy.arcanemagic.block.entity.PumpBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import com.raphydaphy.arcanemagic.util.UVSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class PumpRenderer extends BlockEntityRenderer<PumpBlockEntity>
{
	private static Identifier tex = new Identifier(ArcaneMagic.DOMAIN, "textures/block/pump.png");

	private static RenderUtils.TextureBounds[] top = {
			new UVSet(12, 0, 4, 4), // Bottom
			new UVSet(0, 0, 4, 4), // Top
			new UVSet(0, 10, 4, 2), // North
			new UVSet(0, 12, 4, 2), // South
			new UVSet(0, 14, 4, 2), // West
			new UVSet(0, 14, 4, 2)}; // East

	public void render(PumpBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		if (entity != null && !entity.isBottom() && entity.getWorld() != null)
		{
			BlockEntity bottom = entity.getWorld().getBlockEntity(entity.getPos().down());
			if (bottom instanceof PumpBlockEntity)
			{
				PumpBlockEntity pump = (PumpBlockEntity) bottom;
				float ticks = ArcaneMagicUtils.lerp(pump.prevTicks, pump.ticks, partialTicks);

				BlockState state = getWorld().getBlockState(entity.getPos());

				if (state.getBlock() instanceof PumpBlock)
				{
					GlStateManager.pushMatrix();
					GlStateManager.translated(renderX, renderY, renderZ);
					MinecraftClient.getInstance().getTextureManager().bindTexture(tex);
					RenderUtils.rotateTo(state.get(PumpBlock.FACING));

					GlStateManager.disableCull();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

					Tessellator tess = Tessellator.getInstance();
					BufferBuilder builder = tess.getBufferBuilder();

					builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);

					RenderUtils.renderCube(builder, 4, 4 + Math.sin((Math.PI / 180) * (ticks * 4)) * 1.9, 4, 8, 4, 8, top);
					tess.draw();
					GlStateManager.popMatrix();
				}
			}
		}
	}
}

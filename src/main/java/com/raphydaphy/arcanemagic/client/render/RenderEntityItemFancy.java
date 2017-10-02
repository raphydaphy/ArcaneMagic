package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.util.GLHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityItemFancy extends RenderEntityItem
{

	public RenderEntityItemFancy(RenderManager renderManager)
	{
		super(renderManager, Minecraft.getMinecraft().getRenderItem());

	}

	@Override
	public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GLHelper.renderFancyBeams(x, y + 0.5, z, Essence.getFromBiome(entity.world.getBiome(new BlockPos(x, y, z))).getColor(),
				entity.world.getSeed(), entity.getAge(), 16, 0.7f, 30, 10);

		GlStateManager.pushMatrix();

		ItemStack stack = entity.getItem();
		if (!stack.isEmpty())
		{
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
		GlStateManager.popMatrix();
	}

	public static class Factory implements IRenderFactory<EntityItemFancy>
	{

		@Override
		public Render<? super EntityItemFancy> createRenderFor(RenderManager manager)
		{
			return new RenderEntityItemFancy(manager);
		}

	}

	

}
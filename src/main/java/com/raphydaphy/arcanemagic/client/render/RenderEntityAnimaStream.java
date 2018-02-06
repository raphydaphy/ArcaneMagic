package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.common.entity.EntityAnimaStream;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityAnimaStream extends Render<EntityAnimaStream>
{

	public ResourceLocation texture = new ResourceLocation("arcanemagic:misc/plus");

	protected RenderEntityAnimaStream(RenderManager renderManager)
	{
		super(renderManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnimaStream entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doRender(EntityAnimaStream entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		

	}

	public static class Factory implements IRenderFactory<EntityAnimaStream>
	{

		@Override
		public Render<? super EntityAnimaStream> createRenderFor(RenderManager manager)
		{
			return new RenderEntityAnimaStream(manager);
		}

	}

}

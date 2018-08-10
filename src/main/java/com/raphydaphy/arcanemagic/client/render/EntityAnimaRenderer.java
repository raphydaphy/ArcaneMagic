package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.entity.EntityAnima;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class EntityAnimaRenderer extends Render<EntityAnima>
{
    public EntityAnimaRenderer(RenderManager manager)
    {
        super(manager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityAnima entityAnima)
    {
        return null;
    }
}

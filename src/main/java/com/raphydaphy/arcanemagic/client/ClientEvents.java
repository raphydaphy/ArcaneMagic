package com.raphydaphy.arcanemagic.client;

import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.render.EntityAnimaRenderer;
import com.raphydaphy.arcanemagic.client.render.PedestalRenderer;
import com.raphydaphy.arcanemagic.entity.EntityAnima;
import com.raphydaphy.arcanemagic.tileentity.TileEntityPedestal;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.dimdev.rift.listener.client.ClientTickable;
import org.dimdev.rift.listener.client.EntityRendererAdder;
import org.dimdev.rift.listener.client.TextureAdder;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClientEvents implements ClientTickable, TileEntityRendererAdder, EntityRendererAdder, TextureAdder
{
    @Override
    public void clientTick()
    {
        World world = Minecraft.getMinecraft().world;

        if (world != null && !Minecraft.getMinecraft().isGamePaused())
        {
            ParticleRenderer.getInstance().updateParticles();
        }
    }

    @Override
    public void addTileEntityRenderers(Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers)
    {
        renderers.put(TileEntityPedestal.class, new PedestalRenderer());
    }

    @Override
    public void addEntityRenderers(Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap, RenderManager renderManager)
    {
        entityRenderMap.put(EntityAnima.class, new EntityAnimaRenderer(renderManager));
    }

    @Override
    public Collection<? extends ResourceLocation> getBuiltinTextures()
    {
        List<ResourceLocation> textures = new ArrayList<>();
        textures.add(new ResourceLocation(ArcaneMagicResources.MOD_ID, "particle/plus"));
        System.out.println("registered textures");
        return textures;
    }
}

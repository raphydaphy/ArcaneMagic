package com.raphydaphy.arcanemagic.fluid;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.client.render.fluid.FluidRenderHandlerRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;

public class ModFluidRenderHandler implements SimpleSynchronousResourceReloadListener {
    private final Sprite[] liquidSoulTextures = new Sprite[2];

    @Override
    public Identifier getFabricId() {
        return new Identifier(ArcaneMagic.DOMAIN, "fluid_handlers");
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return Arrays.asList(ResourceReloadListenerKeys.MODELS, ResourceReloadListenerKeys.TEXTURES);
    }

    @Override
    public void apply(ResourceManager resourceManager) {
        SpriteAtlasTexture spriteAtlas = MinecraftClient.getInstance().getSpriteAtlas();
        this.liquidSoulTextures[0] = MinecraftClient.getInstance().getBakedModelManager().getBlockStateMaps().getModel(ModRegistry.LIQUIFIED_SOUL_BLOCK.getDefaultState()).getSprite();
        this.liquidSoulTextures[1] = spriteAtlas.getSprite(ArcaneMagicConstants.FLOWING_LIQUID_SOUL_TEXTURE);

        FluidRenderHandler liquidSoulHandler = (view, pos, state) -> this.liquidSoulTextures;

        FluidRenderHandlerRegistryImpl.INSTANCE.register(ModRegistry.LIQUIFIED_SOUL, liquidSoulHandler);
        FluidRenderHandlerRegistryImpl.INSTANCE.register(ModRegistry.FLOWING_LIQUIFIED_SOUL, liquidSoulHandler);
    }
}

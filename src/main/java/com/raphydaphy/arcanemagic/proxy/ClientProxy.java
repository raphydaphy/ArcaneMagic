package com.raphydaphy.arcanemagic.proxy;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.block.BlockOre;
import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.client.render.EssenceConcentratorTESR;
import com.raphydaphy.arcanemagic.client.render.RenderEntityItemFancy;
import com.raphydaphy.arcanemagic.data.EnumPrimal;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceConcentrator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		registerColors();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEssenceConcentrator.class,
				new EssenceConcentratorTESR());
	}

	public static void registerColors()
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) ->
		{
			return EnumPrimal.values()[stack.getMetadata()].getColorMultiplier();
		}, Item.getItemFromBlock(ModRegistry.INFUSED_ORE));

		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) ->
		{
			if (tintIndex == 0)
				return -1;
			return state.getValue(BlockOre.PRIMAL).getColorMultiplier();
		}, ModRegistry.INFUSED_ORE);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void registerModels(ModelRegistryEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityItemFancy.class, new RenderEntityItemFancy.Factory());
		for (Item i : ModRegistry.ITEMS)
			if (i instanceof IHasModel)
				((IHasModel) i).initModels(event);
		for (Block b : ModRegistry.BLOCKS)
			if (b instanceof IHasModel)
				((IHasModel) b).initModels(event);
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void onTextureStitch(TextureStitchEvent event)
	{
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/particle_star"));
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/particles"));
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/orb"));
		event.getMap().registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/ball"));
		for (int essenceParticle = 1; essenceParticle < 15; essenceParticle++)
		{
			event.getMap()
					.registerSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/essence/essence" + essenceParticle));
		}
		ScepterRegistry.getKeys().forEach(loc -> event.getMap().registerSprite(loc));
		System.out.println("Stiched textures!");
	}
}

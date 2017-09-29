package com.raphydaphy.arcanemagic.proxy;

import java.util.Collection;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.block.BlockOre;
import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.client.particle.ParticleEssence;
import com.raphydaphy.arcanemagic.client.render.EssenceConcentratorTESR;
import com.raphydaphy.arcanemagic.client.render.RenderEntityItemFancy;
import com.raphydaphy.arcanemagic.data.EnumPrimal;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceConcentrator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
	@SubscribeEvent
	public static void renderTooltipPostBackground(RenderTooltipEvent.PostBackground ev)
	{
		if (ev.getStack().getItem() == ModRegistry.SCEPTER)
		{

			ItemStack stack = ev.getStack();
			IEssenceStorage handler = stack.getCapability(IEssenceStorage.CAP, null);

			int y = ev.getY();
			for (int line = 0; line < ev.getLines().size(); line++)
			{
				if (ev.getLines().get(line).equals("§7"))
				{

					break;
				}
				y += 11;
			}
			if (handler != null)
			{
				Collection<EssenceStack> storedEssence = handler.getStored().values();

				if (storedEssence.size() > 0)
				{
					int x = ev.getX();
					int curYCounter = 0;

					for (EssenceStack essence : storedEssence)
					{

						String thisString = essence.getCount() + " "
								+ I18n.format(essence.getEssence().getTranslationName()) + " ";
						ev.getFontRenderer().drawStringWithShadow(thisString, x, y, essence.getEssence().getColorHex());

						x += 70;
						curYCounter++;

						if (curYCounter % 2 == 0)
						{
							y += 10;
							x = ev.getX();
						}
					}

				}
			}
		}
	}
	
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

	@Override
	public void spawnEssenceParticles(World world, Vec3d pos, Vec3d speed, Essence essence, Vec3d travelPos)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(
				new ParticleEssence(world, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z, essence, travelPos));
	}
}

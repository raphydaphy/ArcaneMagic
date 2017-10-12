package com.raphydaphy.arcanemagic.client.proxy;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.client.model.SceptreModel;
import com.raphydaphy.arcanemagic.client.particle.ParticleEssence;
import com.raphydaphy.arcanemagic.client.render.ElementalCraftingTableTESR;
import com.raphydaphy.arcanemagic.client.render.EssenceConcentratorTESR;
import com.raphydaphy.arcanemagic.client.render.RenderEntityItemFancy;
import com.raphydaphy.arcanemagic.client.render.RenderEntityMagicCircles;
import com.raphydaphy.arcanemagic.client.toast.CategoryUnlockedToast;
import com.raphydaphy.arcanemagic.common.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.common.entity.EntityMagicCircles;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.proxy.CommonProxy;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityElementalCraftingTable;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityEssenceConcentrator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{

	@Override
	public void sendEssenceSafe(EssenceStack essence, Vec3d from, Vec3d to, boolean spawnParticles)
	{
		Essence.sendEssence(Minecraft.getMinecraft().world, essence, from, to, false, spawnParticles);
	}

	@Override
	public void addCategoryUnlockToast(NotebookCategory category)
	{
		Minecraft.getMinecraft().getToastGui().add(new CategoryUnlockedToast(category));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
		ModelLoaderRegistry.registerLoader(SceptreModel.Loader.INSTANCE);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		registerColors();

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEssenceConcentrator.class,
				new EssenceConcentratorTESR());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElementalCraftingTable.class,
				new ElementalCraftingTableTESR());
	}

	public static void registerColors()
	{
	}

	@Override
	public void spawnEssenceParticles(World world, Vec3d pos, Vec3d speed, Essence essence, Vec3d travelPos,
			boolean isCosmetic)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleEssence(world, pos.x, pos.y, pos.z, speed.x,
				speed.y, speed.z, essence, travelPos, isCosmetic));
	}

	@Override
	public String translate(String key, Object... args)
	{
		return I18n.format(key, args);
	}
}

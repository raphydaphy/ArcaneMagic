package com.raphydaphy.thaumcraft.proxy;

import javax.annotation.Nullable;

import com.raphydaphy.thaumcraft.entity.EntityItemFancy;
import com.raphydaphy.thaumcraft.init.ModBlocks;
import com.raphydaphy.thaumcraft.init.ModItems;
import com.raphydaphy.thaumcraft.particle.ParticleRenderer;
import com.raphydaphy.thaumcraft.render.RenderEntityItemFancy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy 
{
	public static ParticleRenderer particleRenderer = new ParticleRenderer();
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		registerColors();
	}
	
	public static void registerColors()
	{
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
        {
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
            {
                return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : 0x377434;
            }
        }, ModBlocks.leaves_greatwood);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor()
		{
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) 
			{
				return 0x377434;
			}
		}, ModBlocks.leaves_greatwood);
		
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor()
		{
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) 
			{
				return 0x377434;
			}
		}, ModItems.shard);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityItemFancy.class, new RenderEntityItemFancy.Factory());
		
		ModBlocks.initModels();
		ModItems.initModels();
	}
}

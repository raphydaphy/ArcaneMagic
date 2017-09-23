package com.raphydaphy.thaumcraft.item;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.entity.EntityItemFancy;
import com.raphydaphy.thaumcraft.handler.ThaumcraftSoundHandler;
import com.raphydaphy.thaumcraft.init.ModBlocks;
import com.raphydaphy.thaumcraft.init.ModItems;
import com.raphydaphy.thaumcraft.particle.ParticleUtil;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWand extends ItemBase {

	public ItemWand(String name) {
		super(name);
		maxStackSize = 1;
	}
	
	public static ResourceLocation getCap(ItemStack wand)
	{
		return new ResourceLocation(Thaumcraft.MODID, "textures/items/wand/wand_cap_iron.png");
	}
	
	public static ResourceLocation getRod(ItemStack wand)
	{
		return new ResourceLocation(Thaumcraft.MODID, "textures/items/wand/wand_rod_wood.png");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		Block block = world.getBlockState(pos).getBlock(); 
		if (block.equals(Blocks.BOOKSHELF))
		{
			world.setBlockToAir(pos);
			
			for (int i = 0; i < 1000; i++)
			{
				ParticleUtil.spawnParticleStar(world, pos.getX(), pos.getY() + 1, pos.getZ(), 1, 1, 1, 168, 37, 142, 0.01f, 50);
				
				//world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + world.rand.nextFloat(), pos.getY() + world.rand.nextFloat(), pos.getZ() + world.rand.nextFloat(), 0f, 0f, 0f, 234);
			}
			
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), ThaumcraftSoundHandler.randomWandSound(), SoundCategory.MASTER, 1f, 1f, false);
			if (!world.isRemote)
			{
				EntityItemFancy ei = new EntityItemFancy(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, new ItemStack(ModItems.thaumonomicon));
		        ei.setDefaultPickupDelay();
		        ei.motionX = 0;
		        ei.motionY = 0;
		        ei.motionZ = 0;
				ei.setPickupDelay(15);
				world.spawnEntity(ei);
			}
			else
			{
				// spawn particles client-side
				
			}
			return EnumActionResult.SUCCESS;
		}
		else if (block.equals(ModBlocks.table))
		{
			world.setBlockState(pos, ModBlocks.arcane_worktable.getDefaultState());
		}
		
        return EnumActionResult.PASS;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void initModel() 
	{
		super.initModel();
		ModelLoader.setCustomMeshDefinition(this, (stack) -> {return new ModelResourceLocation("thaumcraft:wand", "inventory");});
	}
}

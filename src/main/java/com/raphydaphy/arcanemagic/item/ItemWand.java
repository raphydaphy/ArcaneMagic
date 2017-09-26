package com.raphydaphy.arcanemagic.item;

import java.util.HashMap;
import java.util.Map;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.api.ThaumcraftAPI;
import com.raphydaphy.arcanemagic.api.vis.IItemVisAcceptor;
import com.raphydaphy.arcanemagic.api.vis.Vis;
import com.raphydaphy.arcanemagic.api.wand.IWandCap;
import com.raphydaphy.arcanemagic.api.wand.IWandRod;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.handler.MeshHandler;
import com.raphydaphy.arcanemagic.handler.ThaumcraftSoundHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.init.VanillaThaumcraftParts;
import com.raphydaphy.arcanemagic.model.ModelWand;
import com.raphydaphy.arcanemagic.particle.ParticleStar;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWand extends ItemBase implements IItemVisAcceptor
{
	public static final String KEY_ROD = "rodType";
	public static final String KEY_CAP = "capType";
	
	public ItemWand(String name)
	{
		super(name);
		maxStackSize = 1;
	}

	public static IWandCap getCap(ItemStack wand)
	{
		if (wand.hasTagCompound())
		{
			return ThaumcraftAPI.WAND_CAPS.get(wand.getTagCompound().getString(KEY_CAP));
		}
		return VanillaThaumcraftParts.cap_iron;
	}

	public static IWandRod getRod(ItemStack wand)
	{
		if (wand.hasTagCompound())
		{
			return ThaumcraftAPI.WAND_RODS.get(wand.getTagCompound().getString(KEY_ROD));
		}
		return VanillaThaumcraftParts.rod_wood;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleStar(world, pos.getX()+ 0.5f, pos.getY() + 1.5f, pos.getZ()+ 0.5f, 0, 0, 0, 86, 13, 124));
		Block block = world.getBlockState(pos).getBlock();
		if (block.equals(Blocks.BOOKSHELF))
		{
			world.setBlockToAir(pos);

			for (int i = 0; i < 10; i++)
			{
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleStar(world, pos.getX()+ 0.5f, pos.getY() + 0.5f, pos.getZ()+ 0.5f, 0, 0, 0, 86, 13, 124));

				// world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() +
				// world.rand.nextFloat(), pos.getY() + world.rand.nextFloat(), pos.getZ() +
				// world.rand.nextFloat(), 0f, 0f, 0f, 234);
			}

			world.playSound(pos.getX(), pos.getY(), pos.getZ(), ThaumcraftSoundHandler.randomWandSound(),
					SoundCategory.MASTER, 1f, 1f, false);
			if (!world.isRemote)
			{
				EntityItemFancy ei = new EntityItemFancy(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
						new ItemStack(ModRegistry.THAUMONOMICON));
				ei.setDefaultPickupDelay();
				ei.motionX = 0;
				ei.motionY = 0;
				ei.motionZ = 0;
				ei.setPickupDelay(15);
				world.spawnEntity(ei);
			} else
			{
				// spawn particles client-side

			}
			return EnumActionResult.SUCCESS;
		} else if (block.equals(ModRegistry.TABLE))
		{
			world.setBlockState(pos, ModRegistry.WORKTABLE.getDefaultState());
		} else if (block.equals(ModRegistry.INFUSED_ORE))
		{
			ItemStack stack = player.getHeldItem(hand);
			if (!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
				
			}
			
			stack.getTagCompound().setString("rodType", VanillaThaumcraftParts.rod_wood.getUnlocalizedName());
			stack.getTagCompound().setString("capType", VanillaThaumcraftParts.cap_gold.getUnlocalizedName());
		}

		return EnumActionResult.PASS;
	}
	
	@SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelLocation(ItemStack stack)
    {
        return new ModelResourceLocation(Thaumcraft.MODID + ":wand", "inventory");
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void initModels(ModelRegistryEvent e)
	{
		ModelLoader.registerItemVariants(ModRegistry.WAND, new ModelResourceLocation(Thaumcraft.MODID + ":wand", "inventory"));
        ModelLoader.setCustomMeshDefinition(ModRegistry.WAND, MeshHandler.instance());
		ModelLoaderRegistry.registerLoader(ModelWand.ModelWandLoader.instance);
	}

	@Override
	public Map<Vis, Integer> getVisStored(ItemStack stack)
	{
		Map<Vis, Integer> visStored = new HashMap<Vis, Integer>();
		if (stack.hasTagCompound())
		{
			for (Vis vis : Vis.values())
			{
				visStored.put(vis, stack.getTagCompound().getInteger(vis.getMultiKey()));
			}
			return visStored;
		}
		return visStored;
	}

	@Override
	public Map<Vis, Integer> getVisCapacity(ItemStack stack)
	{
		Map<Vis, Integer> capacities = new HashMap<Vis, Integer>();
		if (stack.hasTagCompound())
		{
			for (Vis vis : Vis.values())
			{
				capacities.put(vis, stack.getTagCompound().getInteger(vis.getMultiMaxKey()));
			}
			return capacities;
		}
		return capacities;
	}

	@Override
	public Map<Vis, Float> getVisDiscount(ItemStack stack)
	{
		Map<Vis, Float> discounts = new HashMap<Vis, Float>();
		if (stack.hasTagCompound())
		{
			for (Vis vis : Vis.values())
			{
				discounts.put(vis, stack.getTagCompound().getFloat(vis.getMultiDiscountKey()));
			}
			return discounts;
		}
		return discounts;
	}
}

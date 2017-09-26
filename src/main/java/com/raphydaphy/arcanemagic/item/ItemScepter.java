package com.raphydaphy.arcanemagic.item;

import java.util.HashMap;
import java.util.Map;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.IItemEssenceAcceptor;
import com.raphydaphy.arcanemagic.api.wand.IScepterCap;
import com.raphydaphy.arcanemagic.api.wand.IScepterRod;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.handler.MeshHandler;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.init.ScepterRegistry;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScepter extends ItemBase implements IItemEssenceAcceptor
{
	public static final String KEY_CORE = "coreType";
	public static final String KEY_TIP = "tipType";

	public ItemScepter(String name)
	{
		super(name);
		maxStackSize = 1;
	}

	public static IScepterCap getTip(ItemStack wand)
	{
		if (wand.hasTagCompound())
		{
			return ArcaneMagicAPI.SCEPTER_TIPS.get(wand.getTagCompound().getString(KEY_TIP));
		}
		return ScepterRegistry.tip_iron;
	}

	public static IScepterRod getCore(ItemStack wand)
	{
		if (wand.hasTagCompound())
		{
			return ArcaneMagicAPI.SCEPTER_CORES.get(wand.getTagCompound().getString(KEY_CORE));
		}
		return ScepterRegistry.core_wood;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(
				new ParticleStar(world, pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f, 0, 0, 0, 86, 13, 124));
		Block block = world.getBlockState(pos).getBlock();
		if (block.equals(Blocks.BOOKSHELF))
		{
			world.setBlockToAir(pos);

			for (int i = 0; i < 10; i++)
			{
				Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleStar(world, pos.getX() + 0.5f,
						pos.getY() + 0.5f, pos.getZ() + 0.5f, 0, 0, 0, 86, 13, 124));

				// world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() +
				// world.rand.nextFloat(), pos.getY() + world.rand.nextFloat(), pos.getZ() +
				// world.rand.nextFloat(), 0f, 0f, 0f, 234);
			}

			world.playSound(pos.getX(), pos.getY(), pos.getZ(), ArcaneMagicSoundHandler.randomWandSound(),
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

			stack.getTagCompound().setString(KEY_CORE, ScepterRegistry.core_wood.getUnlocalizedName());
			stack.getTagCompound().setString(KEY_TIP, ScepterRegistry.tip_gold.getUnlocalizedName());
		}

		return EnumActionResult.PASS;
	}

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return new ModelResourceLocation(ArcaneMagic.MODID + ":scepter", "inventory");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initModels(ModelRegistryEvent e)
	{
		ModelLoader.registerItemVariants(ModRegistry.WAND,
				new ModelResourceLocation(ArcaneMagic.MODID + ":scepter", "inventory"));
		ModelLoader.setCustomMeshDefinition(ModRegistry.WAND, MeshHandler.instance());
	}

	@Override
	public Map<Essence, Integer> getEssenceStored(ItemStack stack)
	{
		Map<Essence, Integer> essenceStored = new HashMap<Essence, Integer>();
		if (stack.hasTagCompound())
		{
			for (Essence essence : Essence.values())
			{
				essenceStored.put(essence, stack.getTagCompound().getInteger(essence.getMultiKey()));
			}
			return essenceStored;
		}
		return essenceStored;
	}

	@Override
	public Map<Essence, Integer> getEssenceCapacity(ItemStack stack)
	{
		Map<Essence, Integer> capacities = new HashMap<Essence, Integer>();
		if (stack.hasTagCompound())
		{
			for (Essence essence : Essence.values())
			{
				capacities.put(essence, stack.getTagCompound().getInteger(essence.getMultiMaxKey()));
			}
			return capacities;
		}
		return capacities;
	}
}

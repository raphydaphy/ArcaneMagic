package com.raphydaphy.arcanemagic.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart.PartCategory;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.client.particle.ParticleStar;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.handler.MeshHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.scepter.ScepterCap;
import com.raphydaphy.arcanemagic.scepter.ScepterCore;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScepter extends ItemBase
{
	public static final String KEY_CORE = "coreType";
	public static final String KEY_TIP = "tipType";

	public ItemScepter(String name)
	{
		super(name);
		maxStackSize = 1;
	}

	@Nullable
	public static ScepterPart getCap(ItemStack wand)
	{
		if (!wand.hasTagCompound())
			return null;
		return ScepterRegistry.getPart(new ResourceLocation(wand.getTagCompound().getString(KEY_TIP)));
	}

	@Nullable
	public static ScepterPart getCore(ItemStack wand)
	{
		if (!wand.hasTagCompound())
			return null;
		return ScepterRegistry.getPart(new ResourceLocation(wand.getTagCompound().getString(KEY_CORE)));
	}

	@Nonnull
	public static ScepterPart getCapOrDefault(ItemStack wand)
	{
		ScepterPart sp = getCap(wand);
		return sp == null ? ScepterCap.IRON : sp;
	}

	@Nonnull
	public static ScepterPart getCoreOrDefault(ItemStack wand)
	{
		ScepterPart sp = getCore(wand);
		return sp == null ? ScepterCore.WOOD : sp;
	}

	public static void applyCapAndCore(ItemStack wand, ScepterPart cap, ScepterPart core)
	{
		if (!wand.hasTagCompound())
			wand.setTagCompound(new NBTTagCompound());
		Preconditions.checkArgument(cap.getType() == PartCategory.CAP, "You can only assign a cap to the cap slot!");
		Preconditions.checkArgument(core.getType() == PartCategory.CORE, "You can only assign a core the core slot!");
		wand.getTagCompound().setString(KEY_CORE, ScepterCore.WOOD.getRegistryName().toString());
		wand.getTagCompound().setString(KEY_TIP, ScepterCap.GOLD.getRegistryName().toString());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
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

			world.playSound(pos.getX(), pos.getY(), pos.getZ(), ArcaneMagicSoundHandler.randomScepterSound(),
					SoundCategory.MASTER, 1f, 1f, false);
			if (!world.isRemote)
			{
				EntityItemFancy ei = new EntityItemFancy(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
						new ItemStack(ModRegistry.NOTEBOOK));
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
		}
		return EnumActionResult.PASS;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		System.out.println(
				"Wow u sure are dedicated you used your whole wand duration thats " + Integer.MAX_VALUE + " ticks..");
		return stack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		System.out.println("Stopped using it ! The thing you stopped using was a " + stack.getItem().getRegistryName());
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected)
	{
		
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		Essence.writeToNBT(stack.getTagCompound(), new EssenceStack(Essence.DEPTH, 1));
		//player.activeItemStack = stack;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
       // return !oldStack.equals(newStack); //!ItemStack.areItemStacksEqual(oldStack, newStack);
        return false;
    }

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand)
	{
		Essence.initDefaultEssence(player.getHeldItem(hand));
		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack)
	{
		return I18n.format(this.getUnlocalizedName(stack) + ".name").trim();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (stack.hasTagCompound())
		{
			List<EssenceStack> storedEssence = Essence.readFromNBT(stack.getTagCompound());
			
			if (storedEssence.size() > 0)
			{
				String combinedTooltip = "";
				for (EssenceStack essence : storedEssence)
				{
					combinedTooltip += essence.getCount() + " " + I18n.format(essence.getEssence().getTranslationName()) + " "; 
					//combinedTooltip+= essence.toString();
				}
				tooltip.add(combinedTooltip);
			}
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}

	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return new ModelResourceLocation(getRegistryName(), "inventory");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initModels(ModelRegistryEvent e)
	{
		ModelLoader.registerItemVariants(ModRegistry.SCEPTER,
				new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(ModRegistry.SCEPTER, MeshHandler.instance());
	}
}

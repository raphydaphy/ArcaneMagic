package com.raphydaphy.arcanemagic.item;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart.PartCategory;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;
import com.raphydaphy.arcanemagic.data.EnumBasicEssence;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.PacketItemEssenceChanged;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
	public static ScepterPart getTip(ItemStack scepter)
	{
		if (!scepter.hasTagCompound())
			return null;
		return ScepterRegistry.getPart(new ResourceLocation(scepter.getTagCompound().getString(KEY_TIP)));
	}

	@Nullable
	public static ScepterPart getCore(ItemStack scepter)
	{
		if (!scepter.hasTagCompound())
			return null;
		return ScepterRegistry.getPart(new ResourceLocation(scepter.getTagCompound().getString(KEY_CORE)));
	}

	@Nonnull
	public static ScepterPart getTipOrDefault(ItemStack scepter)
	{
		ScepterPart sp = getTip(scepter);
		return sp == null ? ItemTip.Type.IRON : sp;
	}

	@Nonnull
	public static ScepterPart getCoreOrDefault(ItemStack scepter)
	{
		ScepterPart sp = getCore(scepter);
		return sp == null ? ItemCore.Type.WOOD : sp;
	}

	public static void applyTipAndCore(ItemStack scepter, ScepterPart tip, ScepterPart core)
	{
		if (!scepter.hasTagCompound())
			scepter.setTagCompound(new NBTTagCompound());
		Preconditions.checkArgument(tip.getType() == PartCategory.TIP, "You can only assign a tip to the tip slot!");
		Preconditions.checkArgument(core.getType() == PartCategory.CORE, "You can only assign a core the core slot!");
		scepter.getTagCompound().setString(KEY_CORE, core.getRegistryName().toString());
		scepter.getTagCompound().setString(KEY_TIP, tip.getRegistryName().toString());
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
		{
			for (ScepterPart core : ScepterRegistry.getCores())
				for (ScepterPart tip : ScepterRegistry.getTips())
				{
					ItemStack stack = new ItemStack(this);
					applyTipAndCore(stack, tip, core);
					EssenceStack.writeDefaultEssence(stack.getTagCompound());
					items.add(stack);
				}
		}
	}

	public static NBTTagCompound getTagCompoundSafe(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			return EssenceStack.writeDefaultEssence(stack.getTagCompound());
		}
		return stack.getTagCompound();
	}
	
	private int stupidGetSlot(InventoryPlayer inv, ItemStack stack)
	{
		for (int i = 0; i < inv.mainInventory.size(); ++i)
        {
            if (!((ItemStack)inv.mainInventory.get(i)).isEmpty())
            {
            	ItemStack stack1 = inv.mainInventory.get(i);
            	
            	// are stacks exactly equal
            	if (stack.getItem() == stack1.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == stack1.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, stack1))
            	{
            		return i;
            	}
                
            }
        }

        return -1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		if (!world.isRemote)
		{
			IEssenceStorage cap = stack.getCapability(IEssenceStorage.CAP, null);
			cap.store(new EssenceStack(EnumBasicEssence.values()[itemRand.nextInt(6)].getEssence(), 50), false);
			if (player instanceof EntityPlayerMP)
			{
				ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketItemEssenceChanged(cap, stupidGetSlot(player.inventory, stack), stack), (EntityPlayerMP)player);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{

		return EnumActionResult.PASS;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		System.out.println("Wow u sure are dedicated you used your whole scepter duration thats " + Integer.MAX_VALUE
				+ " ticks..");
		return stack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		System.out.println("Stopped using it ! The thing you stopped using was a " + stack.getItem().getRegistryName());
		// TODO do on capsss
		// Essence.resetEssence(stack.getTagCompound());
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected)
	{

	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		IEssenceStorage handler = stack.getCapability(IEssenceStorage.CAP, null);
		
		if (handler != null && !player.world.isRemote)
		{
			for (Essence essence : Essence.REGISTRY.getValues())
			{
				handler.store(new EssenceStack(essence, itemRand.nextInt(2)), false);
			}
			if (player instanceof EntityPlayerMP)
			{
				System.out.println("capper is very strong!");
				ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketItemEssenceChanged(handler, stupidGetSlot(((EntityPlayer)player).inventory, stack), stack), (EntityPlayerMP)player);
			}
		}
		
		if (handler != null && handler.getStored().get(Essence.INFERNO) != null)
		{
			System.out.println("Storing " + handler.getStored().get(Essence.INFERNO).getCount());
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		// TODO: this should not always be false lol
		return false;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand)
	{
		EssenceStack.writeDefaultEssence(player.getHeldItem(hand));
		return EnumActionResult.PASS;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return TextFormatting.DARK_GREEN.toString() + ArcaneMagic.proxy.translate(getUnlocalizedName(stack) + ".name",
				ArcaneMagic.proxy.translate(getTipOrDefault(stack).getUnlocalizedName() + ".name"),
				ArcaneMagic.proxy.translate(getCoreOrDefault(stack).getUnlocalizedName() + ".name"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		//TODO make this not do this if there's no essence to display or always display essence
		tooltip.addAll(Arrays.asList("", "", ""));
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initModels(ModelRegistryEvent e)
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	private static void drawBar(int x, int y, float r, float g, float b, int essence, float rotation)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.rotate(rotation, 0, 0, 1);
		GlStateManager.scale(0.8, 0.8, 0.8);
		GlStateManager.color(1, 1, 1);
		GlStateManager.clearColor(1, 1, 1, 1);

		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/scepter.png"));

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, 181, 16, 10, 33);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y + 33, 181, 68, 10, 4);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 0.2f);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, 29);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, 0.7f);
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, essence);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	public static List<Pair<Integer, Integer>> barPositions = Arrays.asList(Pair.of(37, 1), Pair.of(24, -12),
			Pair.of(7, -20), Pair.of(-12, -20), Pair.of(-29, -14), Pair.of(-42, -1));

	@SideOnly(Side.CLIENT)
	public static void renderHUD(Minecraft mc, ScaledResolution res, ItemStack stack)
	{
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.translate(res.getScaledWidth() - 60, res.getScaledHeight() - 70, 0);
		GlStateManager.scale(0.8, 0.8, 0.8);

		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/scepter.png"));
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(8, 8, 7, 7, 50, 50);
		GlStateManager.scale(1 / 0.8, 1 / 0.8, 1 / 0.8);

		float rot = 76.1f;

		IEssenceStorage handler = stack.getCapability(IEssenceStorage.CAP, null);
		if (handler != null)
		{
			Collection<EssenceStack> storedEssence = handler.getStored().values();
			EssenceStack[] storedEssenceArray = new EssenceStack[storedEssence.size()];
			storedEssence.toArray(storedEssenceArray);
			if (storedEssence.size() > 0)
			{
				for (int curEssence = 0; curEssence < storedEssence.size(); curEssence++)
				{
					EssenceStack essence = storedEssenceArray[curEssence];
					Pair<Integer, Integer> essencePos = barPositions.get(curEssence);
					Color color = essence.getEssence().getColor();
					// System.out.println(color.toString());
					drawBar(essencePos.getLeft(), essencePos.getRight(), color.getRed() / 256, color.getGreen() / 256f,
							color.getBlue() / 256f, essence.getCount() / 28, rot);
					rot += 23;
				}
			}
		}

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new EssenceStorage();// This is serialisable, so Forge should handle save/load
	}

}

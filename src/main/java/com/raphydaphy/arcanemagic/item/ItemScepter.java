package com.raphydaphy.arcanemagic.item;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart.PartCategory;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;
import com.raphydaphy.arcanemagic.client.particle.ParticleStar;
import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.handler.MeshHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.scepter.ScepterCap;
import com.raphydaphy.arcanemagic.scepter.ScepterCore;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Nullable
	public static ScepterPart getCap(ItemStack scepter)
	{
		if (!scepter.hasTagCompound())
			return null;
		return ScepterRegistry.getPart(new ResourceLocation(getTagCompoundSafe(scepter).getString(KEY_TIP)));
	}

	@Nullable
	public static ScepterPart getCore(ItemStack scepter)
	{
		if (!scepter.hasTagCompound())
			return null;
		return ScepterRegistry.getPart(new ResourceLocation(getTagCompoundSafe(scepter).getString(KEY_CORE)));
	}

	@Nonnull
	public static ScepterPart getCapOrDefault(ItemStack scepter)
	{
		ScepterPart sp = getCap(scepter);
		return sp == null ? ScepterCap.IRON : sp;
	}

	@Nonnull
	public static ScepterPart getCoreOrDefault(ItemStack scepter)
	{
		ScepterPart sp = getCore(scepter);
		return sp == null ? ScepterCore.WOOD : sp;
	}

	public static void applyCapAndCore(ItemStack scepter, ScepterPart cap, ScepterPart core)
	{
		if (!scepter.hasTagCompound())
			scepter.setTagCompound(new NBTTagCompound());
		Preconditions.checkArgument(cap.getType() == PartCategory.CAP, "You can only assign a cap to the cap slot!");
		Preconditions.checkArgument(core.getType() == PartCategory.CORE, "You can only assign a core the core slot!");
		getTagCompoundSafe(scepter).setString(KEY_CORE, ScepterCore.WOOD.getRegistryName().toString());
		getTagCompoundSafe(scepter).setString(KEY_TIP, ScepterCap.GOLD.getRegistryName().toString());
	}

	public static NBTTagCompound getTagCompoundSafe(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			return Essence.initDefaultEssence(stack.getTagCompound());
		}
		return stack.getTagCompound();
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
		if (handler != null)
		{
			for (Essence essence : Essence.REGISTRY.getValues())
			{
				handler.store(new EssenceStack(essence, itemRand.nextInt(2)), false);
			}
		}
		// player.activeItemStack = stack;
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
		Essence.initDefaultEssence(player.getHeldItem(hand));
		return EnumActionResult.PASS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack)
	{
		return TextFormatting.DARK_GREEN.toString() + I18n.format(this.getUnlocalizedName(stack) + ".name").trim();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.addAll(Arrays.asList("", "", ""));
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
		return super.getModelLocation(stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initModels(ModelRegistryEvent e)
	{
		// super.initModels(e);
		ModelLoader.registerItemVariants(ModRegistry.SCEPTER,
				new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(ModRegistry.SCEPTER, MeshHandler.instance());
	}

	@SubscribeEvent
	public void renderTooltipPostBackground(RenderTooltipEvent.PostBackground ev)
	{
		if (ev.getStack().getItem().getRegistryName().equals(this.getRegistryName()))
		{

			ItemStack stack = ev.getStack();
			IEssenceStorage handler = stack.getCapability(IEssenceStorage.CAP, null);
			if (handler != null)
			{
				Collection<EssenceStack> storedEssence = handler.getStored().values();

				if (storedEssence.size() > 0)
				{
					int x = ev.getX();
					int curYCounter = 0;
					int y = ev.getY() + 11;
					for (EssenceStack essence : storedEssence)
					{

						String thisString = essence.getCount() + " "
								+ I18n.format(essence.getEssence().getTranslationName()) + " ";
						ev.getFontRenderer().drawStringWithShadow(thisString, x, y, essence.getEssence().getColorHex());

						x += thisString.length() * 6;
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

	/**
	 * Hack to let scepters continue use when nbt changes.
	 */
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent ev)
	{
		if (ev.phase == TickEvent.Phase.START)
		{
			if (ev.player.isHandActive() && !ev.player.activeItemStack.isEmpty())
			{
				ItemStack held = ev.player.getHeldItem(ev.player.getActiveHand());
				if (ev.player.activeItemStack.getItem().getRegistryName().equals(this.getRegistryName())
						&& held.getItem().getRegistryName().equals(this.getRegistryName()))
				{
					if (ev.player.activeItemStack != held)
					{// TODO please check proper (anything that won't change)
						ev.player.activeItemStack = held;
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static void drawBar(int x, int y, float r, float g, float b, int essentia, float rotation)
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
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, essentia);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	public static List<Pos2> barPositions = Arrays.asList(new Pos2(37, 1), new Pos2(24, -12), new Pos2(7, -20),
			new Pos2(-12, -20), new Pos2(-29, -14), new Pos2(-42, -1));

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
					Pos2 essencePos = barPositions.get(curEssence);
					Vec3i color = essence.getEssence().getColorRGB();
					//System.out.println(color.toString());
					drawBar(essencePos.getX(), essencePos.getY(), color.getX() / 256, color.getY() / 256f, color.getZ() / 256f, essence.getCount() / 28, rot);
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

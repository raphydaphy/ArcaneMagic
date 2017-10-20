package com.raphydaphy.arcanemagic.common.item;

import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.entity.EntityMagicCircles;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookToastExpanded;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookToastOrFail;
import com.raphydaphy.arcanemagic.common.notebook.NotebookCategories;

import akka.japi.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemParchment extends ItemBase
{
	public static final String TITLE = "unlocTitle";
	public static final String CATEGORY = "unlocCategory";
	public static final String PARAGRAPHS = "numParagraphs";

	public ItemParchment(String name)
	{
		super(name, TextFormatting.YELLOW);
		setMaxStackSize(1);
	}

	@Override
	public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem)
	{
		super.onEntityItemUpdate(entityItem);

		World world = entityItem.world;

		for (int x = (int) entityItem.posX - 5; x < (int) entityItem.posX + 5; x++)
		{
			for (int y = (int) entityItem.posY - 5; y < (int) entityItem.posY + 5; y++)
			{
				for (int z = (int) entityItem.posZ - 5; z < (int) entityItem.posZ + 5; z++)
				{
					BlockPos here = new BlockPos(x, y, z);
					if (world.getBlockState(here).getBlock().equals(Blocks.BEDROCK))
					{
						if (itemRand.nextInt(2000) == 1)
						{
							// just for looks..
							ArcaneMagic.proxy.spawnEssenceParticles(world, new Vec3d(x + 0.5, y + 0.5, z + 0.5),
									new Vec3d(0, 0, 0), Essence.getFromBiome(world.getBiome(here)),
									entityItem.getPositionVector().addVector(0, 0.4, 0), true);
						}
					}
				}
			}
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public FontRenderer getParchmentRenderer(ItemStack stack)
	{
		if (stack.getItem().equals(ModRegistry.ANCIENT_PARCHMENT))
		{
			return Minecraft.getMinecraft().fontRenderer;
		} else
		{
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TITLE)
					&& !stack.getTagCompound().getString(TITLE).equals("arcanemagic.notebook.category.unknown_realms"))
			{
				return Minecraft.getMinecraft().fontRenderer;
			}

			return Minecraft.getMinecraft().standardGalacticFontRenderer;
		}
	}

	@SideOnly(Side.CLIENT)
	public static String getLocalizedTitle(ItemStack stack)
	{
		if (stack.getItem().equals(ModRegistry.ANCIENT_PARCHMENT))
		{
			return I18n.format("arcanemagic.message.ancient_parchment");
		} else
		{
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TITLE))
			{
				return I18n.format(stack.getTagCompound().getString(TITLE));
			}

			return I18n.format("arcanemagic.notebook.category.unknown_realms");
		}
	}

	@SideOnly(Side.CLIENT)
	public static String getLocalizedDesc(ItemStack stack)
	{
		if (stack.getItem().equals(ModRegistry.ANCIENT_PARCHMENT))
		{
			return I18n.format("arcanemagic.message.ancient_parchment.0") + "\n\n"
					+ I18n.format("arcanemagic.message.ancient_parchment.1");
		} else
		{
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TITLE))
			{
				String formattedDesc = "";
				for (int p = 0; p < stack.getTagCompound().getInteger(PARAGRAPHS); p++)
				{
					formattedDesc += I18n.format(stack.getTagCompound().getString(TITLE) + "." + p) + "\n\n";
				}
				return formattedDesc;
			}

			return I18n.format("arcanemagic.notebook.category.unknown_realms.0") + "\n\n"
					+ I18n.format("arcanemagic.notebook.category.unknown_realms.1");
		}
	}

	@Nullable
	public static Pair<NotebookCategory, Boolean> getToUnlock(ItemStack stack)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(CATEGORY))
		{
			for (NotebookCategory potentialCat : NotebookCategory.REGISTRY)
			{
				if (potentialCat.getUnlocalizedName().equals(stack.getTagCompound().getString(CATEGORY)))
				{
					return new Pair<NotebookCategory, Boolean>(potentialCat, false);
				}
			}

			for (NotebookCategory subCat : NotebookCategory.SUB_REGISTRY)
			{
				if (subCat.getUnlocalizedName().equals(stack.getTagCompound().getString(CATEGORY)))
				{
					return new Pair<NotebookCategory, Boolean>(subCat, true);
				}
			}
		}
		return null;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem().equals(ModRegistry.WRITTEN_PARCHMENT))
		{
			Pair<NotebookCategory, Boolean> catInfo = getToUnlock(stack);
			if (catInfo != null)
			{
				if (!world.isRemote)
				{
					NotebookInfo cap = player.getCapability(NotebookInfo.CAP, null);
					if (cap != null)
					{
						NotebookCategory cat = catInfo.first();
						if (cat != null)
						{
							if (!cat.equals(NotebookCategories.UNKNOWN_REALMS)
									&& cap.isUnlocked(cat.getPrerequisiteTag())
									&& !cap.isUnlocked(cat.getRequiredTag()))
							{
								cap.setUnlocked(cat.getRequiredTag());
							}
							if (catInfo.second())
							{
								for (NotebookCategory mightBeParent : NotebookCategory.REGISTRY.getValues())
								{
									if (mightBeParent != null && mightBeParent.getRequiredTag() != null)
									{
										if (mightBeParent.getRequiredTag().equals(cat.getPrerequisiteTag()))
										{
											ArcaneMagicPacketHandler.INSTANCE
													.sendTo(new PacketNotebookToastExpanded(mightBeParent,
															cat.getRequiredTag(), true), (EntityPlayerMP) player);
										}
									}
								}
							} else
							{
								ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToastOrFail(cat, true),
										(EntityPlayerMP) player);
							}

						} else
						{
							ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToastOrFail(cat, true),
									(EntityPlayerMP) player);
						}
					}
				}
			}

			world.playSound(player, player.getPosition(), ArcaneMagicSoundHandler.randomWriteSound(),
					SoundCategory.PLAYERS, 1, 1);
			player.setHeldItem(hand, ItemStack.EMPTY);
			player.swingArm(hand);
			if (!world.isRemote)
			{
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		if (player.getHeldItem(hand).getItem().equals(ModRegistry.ANCIENT_PARCHMENT))
		{
			if (world.isRemote)
			{
				return EnumActionResult.PASS;
			}
			Block block = world.getBlockState(pos).getBlock();

			if (player.isSneaking() && block == Blocks.BEDROCK)
			{
				player.getCooldownTracker().setCooldown(player.getHeldItem(hand).getItem(), 150);

				player.getHeldItem(hand).shrink(1);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ArcaneMagicSoundHandler.spell,
						SoundCategory.MASTER, 1f, 1f);

				world.spawnEntity(new EntityMagicCircles(world, pos.getX(), pos.getY(), pos.getZ()));

				return EnumActionResult.SUCCESS;
			}

		}
		return EnumActionResult.PASS;
	}

}

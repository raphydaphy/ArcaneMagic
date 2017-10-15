package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.entity.EntityMagicCircles;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemParchment extends ItemBase
{
	public static final String TITLE = "unlocTitle";
	public static final String DESC = "unlocDesc";
	public static final String PARAGRAPHS = "numParagraphs";

	public ItemParchment(String name)
	{
		super(name);
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
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TITLE) && stack.getTagCompound().hasKey(DESC))
			{
				return Minecraft.getMinecraft().fontRenderer;
			}

			return Minecraft.getMinecraft().standardGalacticFontRenderer;
		}
	}

	@SideOnly(Side.CLIENT)
	public String getLocalizedTitle(ItemStack stack)
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
	public String getLocalizedDesc(ItemStack stack)
	{
		if (stack.getItem().equals( ModRegistry.ANCIENT_PARCHMENT))
		{
			return I18n.format("arcanemagic.message.ancient_parchment.0") + "\n\n"
					+ I18n.format("arcanemagic.message.ancient_parchment.1");
		} else
		{
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey(DESC))
			{
				String formattedDesc = "";
				if (stack.getTagCompound().hasKey(PARAGRAPHS))
				{
					for (int p = 0; p < stack.getTagCompound().getInteger(PARAGRAPHS); p++)
					{
						formattedDesc += I18n.format(stack.getTagCompound().getString(DESC + "." + p)) + "\n\n";
					}
				} else
				{
					formattedDesc = I18n.format(stack.getTagCompound().getString(DESC));
				}
				return formattedDesc;
			}

			return I18n.format("arcanemagic.notebook.category.unknown_realms.0") + "\n\n" + I18n.format("arcanemagic.notebook.category.unknown_realms.1");
		}
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
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ArcaneMagicSoundHandler.craft_start,
						SoundCategory.MASTER, 1f, 1f);

				world.spawnEntity(new EntityMagicCircles(world, pos.getX(), pos.getY(), pos.getZ()));

				return EnumActionResult.SUCCESS;
			}
			
		}
		return EnumActionResult.PASS;
	}

}

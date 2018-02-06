package com.raphydaphy.arcanemagic.common.item;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;

public class ItemIlluminator extends ItemBase implements IHasRecipe
{
	public ItemIlluminator()
	{
		super("mystical_illuminator", TextFormatting.GRAY);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ)
	{
		IBlockState state = world.getBlockState(pos);
		String fancyDebug = "";
		for (NotebookCategory cat : ArcaneMagicAPI.getAnalyzer().getAnalysisResults(state))
		{
			fancyDebug += cat.getRegistryName() + ", ";
		}
		ArcaneMagic.LOGGER.info("State " + state.toString()
				+ (fancyDebug.isEmpty() ? " reveals no categories." : " reveals categories " + fancyDebug));

		if (!player.isSneaking())
		{
			if (!ArcaneMagicAPI.getAnalyzer().getAnalysisResults(state).isEmpty())
			{
				ArcaneMagic.proxy.addIlluminatorParticle(this, world, pos, facing, hitX, hitY, hitZ);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void initRecipes(Register<IRecipe> e)
	{
		RecipeHelper.addShaped(this, 3, 3, null, "dustRedstone", null, "dustRedstone", Items.COMPASS, "dustRedstone",
				null, "dustRedstone", null);
	}
}

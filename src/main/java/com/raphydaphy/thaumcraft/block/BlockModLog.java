package com.raphydaphy.thaumcraft.block;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockModLog extends BlockLog
{
	public BlockModLog(String name)
	{
		super();
		setUnlocalizedName(Thaumcraft.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(Thaumcraft.creativeTab);
		setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, EnumAxis.Y));
		setHarvestLevel("axe", 0);
		setHardness(3f);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() 
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	
	private static EnumAxis getAxis(int meta) 
	{
		switch (meta & 12) 
		{
			case 0:
				return EnumAxis.Y;
			case 4:
				return EnumAxis.X;
			case 8:
				return EnumAxis.Z;
			default:
				return EnumAxis.NONE;
		}
	}
	
	@Override
	public final IBlockState getStateFromMeta(int meta) 
	{
		EnumAxis axis = getAxis(meta);
		return getDefaultState().withProperty(LOG_AXIS, axis);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public final int getMetaFromState(IBlockState state) 
	{
		int i = damageDropped(state);

		switch (state.getValue(LOG_AXIS)) 
		{
			case X:
				i |= 4;
				break;
			case Z:
				i |= 8;
				break;
			case NONE:
				i |= 12;
		}

		return i;
	}
	
	@Override
	protected final BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, LOG_AXIS);
	}
}

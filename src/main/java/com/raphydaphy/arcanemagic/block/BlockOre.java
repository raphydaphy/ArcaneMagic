package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.data.EnumPrimal;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOre extends BlockBase {
	public static final PropertyEnum<EnumPrimal> PRIMAL = PropertyEnum.create("primal", EnumPrimal.class);

	public BlockOre(String name, float hardness) {
		super(name, Material.ROCK, hardness);
		setDefaultState(this.blockState.getBaseState().withProperty(PRIMAL, EnumPrimal.AER));
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PRIMAL).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PRIMAL, EnumPrimal.values()[meta]);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PRIMAL);
	}

	@Override
	public void initModels(ModelRegistryEvent e) {
		for (EnumPrimal p : EnumPrimal.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), p.ordinal(), new ModelResourceLocation(getRegistryName(), "primal=" + p.getName()));
		}
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(PRIMAL, EnumPrimal.values()[meta]);
	}

	@Override
	public ItemBlock createItemBlock() {
		return (ItemBlock) new ItemBlock(this) {
			
			@Override
			public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
				if (isInCreativeTab(tab)) {
					for (EnumPrimal p : EnumPrimal.values()) {
						items.add(new ItemStack(this, 1, p.ordinal()));
					}
				}
			}
			
			@Override
			public int getMetadata(int damage) {
				return damage;
			}

		}.setHasSubtypes(true).setRegistryName(getRegistryName());
	}

}

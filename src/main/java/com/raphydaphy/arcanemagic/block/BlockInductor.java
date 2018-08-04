package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.tileentity.TileEntityInductor;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockInductor extends BlockWaterloggableBase implements ITileEntityProvider
{
    private static final VoxelShape shape;
    public static final BooleanProperty BLOCK_MODE;

    public BlockInductor(Builder builder)
    {
        super(builder);
        this.setDefaultState(this.getDefaultState().withProperty(BLOCK_MODE, true));
    }

    @Override
    public VoxelShape getShape(IBlockState p_getShape_1_, IBlockReader p_getShape_2_, BlockPos p_getShape_3_)
    {
        return shape;
    }

    @Override
    protected void addPropertiesToBuilder(net.minecraft.state.StateContainer.Builder<Block, IBlockState> map)
    {
        super.addPropertiesToBuilder(map);
        map.addProperties(BLOCK_MODE);
    }

    @Override
    public boolean onRightClick(IBlockState blockstate, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
    {
        if (player.isSneaking())
        {
            if (world.isRemote)
            {
                return true;
            }
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityInductor)
            {
                TileEntityInductor inductor = (TileEntityInductor) te;
                SoundEvent sound = SoundEvents.BLOCK_NOTE_XYLOPHONE;
                if (inductor.setMode(!inductor.getMode()))
                {
                    sound = SoundEvents.BLOCK_NOTE_SNARE;
                }
                world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeReplacingBlock(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean p_196243_5_)
    {
        ArcaneMagicUtils.beforeReplacingTileEntity(state, world, pos, newState);
        super.beforeReplacingBlock(state, world, pos, newState, p_196243_5_);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(IBlockReader iBlockReader)
    {
        return new TileEntityInductor();
    }

    static
    {
        shape = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
        BLOCK_MODE = BooleanProperty.create("block_mode");
    }
}

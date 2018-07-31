package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.tileentity.TileEntityPedestal;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPedestal extends BlockWaterloggableBase implements ITileEntityProvider
{
    private static final VoxelShape shape;

    public BlockPedestal(Builder builder)
    {
        super(builder);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(IBlockReader iBlockReader)
    {
        return new TileEntityPedestal();
    }

    @Override
    public boolean onRightClick(IBlockState blockstate, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityPedestal)
            {
                TileEntityPedestal pedestal = (TileEntityPedestal)te;
                ItemStack held = player.getHeldItem(hand);
                if (!held.isEmpty())
                {
                    if (pedestal.isItemValidForSlot(0, held))
                    {
                        ItemStack add = held.copy();
                        add.setCount(1);
                        pedestal.setInventorySlotContents(0, add);
                        pedestal.sync();

                        if (!player.capabilities.isCreativeMode)
                        {
                            held.shrink(1);
                            player.openContainer.detectAndSendChanges();
                        }
                    }
                }
                else
                {
                    ItemStack remove = pedestal.getStackInSlot(0).copy();
                    if (!remove.isEmpty())
                    {
                        pedestal.setInventorySlotContents(0, ItemStack.EMPTY);
                        pedestal.sync();

                        if (!player.inventory.addItemStackToInventory(remove))
                        {
                            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), remove);
                        }
                        else
                        {
                            player.openContainer.detectAndSendChanges();
                        }
                    }
                }
            }
            return true;
        }
    }

    @Override
    public VoxelShape getShape(IBlockState p_getShape_1_, IBlockReader p_getShape_2_, BlockPos p_getShape_3_)
    {
        return shape;
    }

    @Override
    public void beforeReplacingBlock(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean p_196243_5_)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
            }
            world.removeTileEntity(pos);
        }
        super.beforeReplacingBlock(state, world, pos, newState, p_196243_5_);
    }

    static
    {
        VoxelShape one = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
        VoxelShape two = Block.makeCuboidShape(6.0D, 2.0D, 6.0D, 10.0D, 6.0D, 10.0D);
        VoxelShape three = Block.makeCuboidShape(4.0D, 6.0D, 4.0D, 12.0D, 10.0D, 12.0D);
        VoxelShape four = Block.makeCuboidShape(6.0D, 10.0D, 6.0D, 10.0D, 12.0D, 10.0D);
        shape = ShapeUtils.func_197872_a(ShapeUtils.func_197872_a(one, two), ShapeUtils.func_197872_a(three, four));
    }
}

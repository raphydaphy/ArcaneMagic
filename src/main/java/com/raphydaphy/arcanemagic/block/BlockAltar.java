package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ShapeUtils;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAltar extends BlockWaterloggableBase implements ITileEntityProvider
{
    private static final VoxelShape without_transformer;
    private static VoxelShape with_transformer;
    public static final BooleanProperty HAS_TRANSFORMER;

    public BlockAltar(Builder builder)
    {
        super(builder);
        this.setDefaultState(this.getDefaultState().withProperty(HAS_TRANSFORMER, false));
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader reader, BlockPos pos)
    {
        return state.getValue(HAS_TRANSFORMER) ? with_transformer : without_transformer;
    }

    @Override
    protected void addPropertiesToBuilder(net.minecraft.state.StateContainer.Builder<Block, IBlockState> map)
    {
        super.addPropertiesToBuilder(map);
        map.addProperties(HAS_TRANSFORMER);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(world, player, pos, state, te, stack);
        if (!world.isRemote && state.getValue(HAS_TRANSFORMER))
        {
            spawnAsEntity(world, pos, new ItemStack(ArcaneMagic.TRANSFORMER));
        }
    }

    static
    {
        VoxelShape bottom = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
        VoxelShape middle = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
        VoxelShape top = Block.makeCuboidShape(0.0D, 6.0D, 0.0D, 16.0D, 12.0D, 16.0D);
        without_transformer = ShapeUtils.func_197872_a(ShapeUtils.func_197872_a(bottom, middle), top);

        // TODO: proper model & bounding box for transformer
        VoxelShape transformer_middle = Block.makeCuboidShape(6, 12, 6, 10, 14, 10);
        with_transformer = ShapeUtils.func_197872_a(transformer_middle, without_transformer);
        HAS_TRANSFORMER = BooleanProperty.create("has_transformer");
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
        return new TileEntityAltar();
    }
}

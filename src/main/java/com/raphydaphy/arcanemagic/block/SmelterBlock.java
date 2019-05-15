package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.base.DoubleBlockBase;
import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import com.raphydaphy.crochet.network.PacketHandler;
import io.github.prospector.silk.fluid.FluidContainer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SmelterBlock extends DoubleBlockBase implements BlockEntityProvider {
    private static final Map<Direction, VoxelShape> bottom = new HashMap<>();
    private static final Map<Direction, VoxelShape> top = new HashMap<>();

    static {
        bottom.put(Direction.NORTH, VoxelShapes.union(Block.createCuboidShape(4, 0, 6, 2, 8, 0), Block.createCuboidShape(14, 12, 12, 12, 14, 6),
                Block.createCuboidShape(4, 12, 12, 2, 14, 6), Block.createCuboidShape(14, 8, 6, 2, 14, 0), Block.createCuboidShape(16, 0, 16, 0, 12, 6),
                Block.createCuboidShape(12, 0, 6, 4, 2, 0), Block.createCuboidShape(12, 14, 14, 4, 12, 4), Block.createCuboidShape(12, 4, 14, 4, 0, 6),
                Block.createCuboidShape(14, 0, 6, 12, 8, 0), Block.createCuboidShape(12, 6, 4, 4, 12, 2)));

        bottom.put(Direction.EAST, VoxelShapes.union(Block.createCuboidShape(10, 0, 12, 16, 8, 14), Block.createCuboidShape(4, 12, 2, 10, 14, 4),
                Block.createCuboidShape(4, 12, 12, 10, 14, 14), Block.createCuboidShape(10, 8, 2, 16, 14, 14), Block.createCuboidShape(0, 0, 0, 10, 12, 16),
                Block.createCuboidShape(10, 0, 4, 16, 2, 12), Block.createCuboidShape(2, 12, 4, 10, 16, 12), //Block.createCuboidShape(2, 2, 4, 12, 4, 12)
                Block.createCuboidShape(10, 0, 2, 16, 8, 4), Block.createCuboidShape(12, 6, 4, 14, 12, 12)));

        bottom.put(Direction.SOUTH, VoxelShapes.union(Block.createCuboidShape(12, 0, 10, 14, 8, 16), Block.createCuboidShape(2, 12, 4, 4, 14, 10),
                Block.createCuboidShape(12, 12, 4, 14, 14, 10), Block.createCuboidShape(2, 8, 10, 14, 14, 16), Block.createCuboidShape(0, 0, 0, 16, 12, 10),
                Block.createCuboidShape(4, 0, 10, 12, 2, 16), Block.createCuboidShape(4, 12, 2, 12, 16, 10), // Block.createCuboidShape(4, 2, 2, 12, 4, 12)
                Block.createCuboidShape(2, 0, 10, 4, 8, 16), Block.createCuboidShape(4, 6, 12, 12, 12, 14)));

        bottom.put(Direction.WEST, VoxelShapes.union(Block.createCuboidShape(6, 0, 4, 0, 8, 2), Block.createCuboidShape(12, 12, 14, 6, 14, 12),
                Block.createCuboidShape(12, 12, 4, 6, 14, 2), Block.createCuboidShape(6, 8, 14, 0, 14, 2), Block.createCuboidShape(16, 0, 16, 6, 12, 0),
                Block.createCuboidShape(6, 0, 12, 0, 2, 4), Block.createCuboidShape(14, 14, 12, 4, 12, 4), Block.createCuboidShape(14, 4, 12, 6, 0, 4),
                Block.createCuboidShape(6, 0, 14, 0, 8, 12), Block.createCuboidShape(4, 6, 12, 2, 12, 4)));

        top.put(Direction.NORTH, VoxelShapes.union(Block.createCuboidShape(10, 4, 12, 6, 6, 8), Block.createCuboidShape(12, 0, 14, 4, 4, 6), Block.createCuboidShape(12, 6, 14, 4, 8, 6)));
        top.put(Direction.EAST, VoxelShapes.union(Block.createCuboidShape(4, 4, 6, 8, 6, 10), Block.createCuboidShape(2, 0, 4, 10, 4, 12), Block.createCuboidShape(2, 6, 4, 10, 8, 12)));
        top.put(Direction.SOUTH, VoxelShapes.union(Block.createCuboidShape(6, 4, 4, 10, 6, 8), Block.createCuboidShape(4, 0, 2, 12, 4, 10), Block.createCuboidShape(4, 6, 2, 12, 8, 10)));
        top.put(Direction.WEST, VoxelShapes.union(Block.createCuboidShape(12, 4, 10, 8, 6, 6), Block.createCuboidShape(14, 0, 12, 6, 4, 4), Block.createCuboidShape(14, 6, 12, 6, 8, 4)));
    }

    public SmelterBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f).build());
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!(blockEntity instanceof SmelterBlockEntity)) {
            return false;
        }

        if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
            if (ArcaneMagicUtils.insertFluidFromBucket(world, player, hand, Direction.DOWN, pos, (FluidContainer) blockEntity, ModRegistry.LIQUIFIED_SOUL)) {
                return true;
            }
            return false;
        }

        if (player.isSneaking()) {
            for (int slot = 2; slot >= 1; slot--) {
                boolean extracted = ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, slot);
                if (extracted) {
                    return true;
                }
            }
            return false;
        }

        return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, 0);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState_1) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ArcaneMagicUtils.calculateComparatorOutput(world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext vep) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? bottom.get(state.get(FACING)) : top.get(state.get(FACING));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new SmelterBlockEntity();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);
        if (!world.isClient && placer instanceof PlayerEntity && !((DataHolder) placer).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.PLACED_SMELTER_KEY)) {
            PacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) placer);
            ((DataHolder) placer).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.PLACED_SMELTER_KEY, true);
            ArcaneMagicUtils.updateNotebookSection(world, (DataHolder) placer, NotebookSectionRegistry.SMELTING.getID().toString(), false);
            ((DataHolder) placer).markAdditionalDataDirty();
        }
    }
}

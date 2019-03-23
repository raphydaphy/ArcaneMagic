package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.DoubleBlockBase;
import com.raphydaphy.arcanemagic.block.entity.MixerBlockEntity;
import com.raphydaphy.arcanemagic.client.render.IExtraRenderLayers;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import com.raphydaphy.cutsceneapi.network.PacketHandler;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.util.ActionType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

public class MixerBlock extends DoubleBlockBase implements BlockEntityProvider, IExtraRenderLayers
{
	private static final Map<Direction, VoxelShape> bottom = new HashMap<>();
	private static final VoxelShape top;

	static
	{
		VoxelShape bottomNorth = VoxelShapes.union(Block.createCuboidShape(12, 0, 8, 4, 2, 4), Block.createCuboidShape(14, 2, 14, 2, 14, 2),
				Block.createCuboidShape(4, 0, 4, 12, 2, 8), Block.createCuboidShape(4, 14, 4, 12, 16, 12), Block.createCuboidShape(16, 4, 12, 14, 12, 4),
				Block.createCuboidShape(2, 4, 12, 0, 12, 4), Block.createCuboidShape(9, 10, 9, 7, 12, 7));

		VoxelShape bottomEast = VoxelShapes.union(Block.createCuboidShape(8, 0, 4, 12, 2, 12), Block.createCuboidShape(2, 2, 2, 14, 14, 14),
				Block.createCuboidShape(4, 0, 4, 12, 2, 8), Block.createCuboidShape(4, 14, 4, 12, 16, 12), Block.createCuboidShape(4, 4, 0, 12, 12, 2),
				Block.createCuboidShape(4, 4, 14, 12, 12, 16), Block.createCuboidShape(7, 10, 7, 9, 12, 9));

		VoxelShape bottomSouth = VoxelShapes.union(Block.createCuboidShape(4, 0, 8, 12, 2, 12), Block.createCuboidShape(2, 2, 2, 14, 14, 14),
				Block.createCuboidShape(4, 0, 4, 12, 2, 8), Block.createCuboidShape(4, 14, 4, 12, 16, 12), Block.createCuboidShape(0, 4, 4, 2, 12, 12),
				Block.createCuboidShape(14, 4, 4, 16, 12, 12), Block.createCuboidShape(7, 10, 7, 9, 12, 9));

		VoxelShape bottomWest = VoxelShapes.union(Block.createCuboidShape(8, 0, 12, 4, 2, 4), Block.createCuboidShape(14, 2, 14, 2, 14, 2),
				Block.createCuboidShape(4, 0, 4, 8, 2, 12), Block.createCuboidShape(4, 14, 4, 12, 16, 12), Block.createCuboidShape(12, 4, 16, 4, 12, 14),
				Block.createCuboidShape(12, 4, 2, 4, 12, 0), Block.createCuboidShape(9, 10, 9, 7, 12, 7));

		top = Block.createCuboidShape(2, 0, 2, 14, 14, 14);

		bottom.put(Direction.NORTH, bottomNorth);
		bottom.put(Direction.EAST, bottomEast);
		bottom.put(Direction.SOUTH, bottomSouth);
		bottom.put(Direction.WEST, bottomWest);

	}

	public MixerBlock()
	{
		super(FabricBlockSettings.of(Material.WOOD).strength(3.5f, 3.5f).build());
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos)
	{
		return state.get(HALF) == DoubleBlockHalf.UPPER;
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockRenderLayer[] getExtraRenderLayers()
	{
		return new BlockRenderLayer[]{BlockRenderLayer.TRANSLUCENT};
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof MixerBlockEntity))
		{
			return false;
		}

		MixerBlockEntity mixer = (MixerBlockEntity) blockEntity;

		ItemStack stack = player.getStackInHand(hand);

		if (!stack.isEmpty() && stack.getItem() instanceof BucketItem)
		{
			if (!mixer.isBottom() && stack.getItem() == Items.WATER_BUCKET)
			{
				if (mixer.tryInsertFluid(hitResult.getSide(), Fluids.WATER, DropletValues.BUCKET, ActionType.PERFORM))
				{
					if (!world.isClient && !player.isCreative())
					{
						// This assumes that buckets cannot stack
						// May want to change this one day if stackable buckets are modded into fabric
						player.setStackInHand(hand, new ItemStack(Items.BUCKET));
					}
					world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCK, 1, 1);
					return true;
				}
			} else if (mixer.isBottom() && stack.getItem() == Items.BUCKET)
			{
				if (mixer.tryExtractFluid(hitResult.getSide(), ModRegistry.LIQUIFIED_SOUL, DropletValues.BUCKET, ActionType.PERFORM))
				{
					if (!world.isClient && !player.isCreative())
					{
						ItemStack extracted = new ItemStack(ModRegistry.LIQUIFIED_SOUL_BUCKET);
						if (stack.getAmount() > 1)
						{
							if (!player.inventory.insertStack(extracted.copy()))
							{
								ItemEntity bucket = new ItemEntity(world, player.getPosVector().x, player.getPosVector().y, player.getPosVector().z, extracted.copy());
								bucket.setVelocity(0, 0, 0);
								world.spawnEntity(bucket);
							}
							stack.subtractAmount(1);
						} else
						{
							player.setStackInHand(hand, extracted.copy());
						}
					}
					world.playSound(player, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCK, 1, 1);
					return true;
				}
			}
		}
		return ArcaneMagicUtils.pedestalInteraction(world, player, mixer, hand, 0);
	}

	@Override
	public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1)
	{
		if (ArcaneMagicUtils.handleTileEntityBroken(this, oldState, world, pos, newState))
		{
			super.onBlockRemoved(oldState, world, pos, newState, boolean_1);
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState blockState_1)
	{
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ArcaneMagicUtils.calculateComparatorOutput(world, pos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition vep)
	{
		return state.get(HALF) == DoubleBlockHalf.LOWER ? bottom.get(state.get(FACING)) : top;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1)
	{
		return new MixerBlockEntity();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		super.onPlaced(world, pos, state, placer, stack);
		if (!world.isClient && placer instanceof PlayerEntity && !((DataHolder) placer).getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_MIXER_KEY))
		{
			PacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) placer);
			((DataHolder) placer).getAdditionalData().putBoolean(ArcaneMagicConstants.PLACED_MIXER_KEY, true);
			ArcaneMagicUtils.updateNotebookSection(world, (DataHolder) placer, NotebookSectionRegistry.LIQUEFACTION.getID().toString(), false);
			((DataHolder) placer).markAdditionalDataDirty();
		}
	}
}

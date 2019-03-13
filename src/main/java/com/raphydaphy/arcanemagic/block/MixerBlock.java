package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.DoubleBlockBase;
import com.raphydaphy.arcanemagic.block.entity.MixerBlockEntity;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.util.ActionType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MixerBlock extends DoubleBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	static
	{
		shape = VoxelShapes.fullCube();
	}

	public MixerBlock()
	{
		super(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f).build());
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
			if (!mixer.bottom && stack.getItem() == Items.WATER_BUCKET)
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
			} else if (mixer.bottom && stack.getItem() == Items.BUCKET)
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
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
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
		return shape;
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
			ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) placer);
			((DataHolder) placer).getAdditionalData().putBoolean(ArcaneMagicConstants.PLACED_MIXER_KEY, true);
			ArcaneMagicUtils.updateNotebookSection(world, (DataHolder) placer, NotebookSectionRegistry.LIQUEFACTION.getID().toString(), false);
			((DataHolder) placer).markAdditionalDataDirty();
		}
	}
}

package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.block.base.OrientableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.AnalyzerBlockEntity;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.parchment.DiscoveryParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerBlock extends OrientableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	static
	{
		VoxelShape one = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(2, 0, 7, 4, 2, 9), Block.createCuboidShape(7, 0, 2, 9, 2, 4)),
				VoxelShapes.union(Block.createCuboidShape(7, 0, 12, 9, 2, 14), Block.createCuboidShape(12, 0, 7, 14, 2, 9)));
		VoxelShape two = VoxelShapes.union(VoxelShapes.union(Block.createCuboidShape(4, 0, 4, 12, 4, 12), Block.createCuboidShape(2, 4, 2, 4, 8, 14)),
				VoxelShapes.union(Block.createCuboidShape(4, 4, 2, 12, 8, 4), Block.createCuboidShape(12, 4, 2, 14, 8, 14)));
		VoxelShape three = VoxelShapes.union(Block.createCuboidShape(6, 4, 6, 10, 6, 10), Block.createCuboidShape(4, 4, 12, 12, 8, 14));

		shape = VoxelShapes.union(VoxelShapes.union(one, two), three);
	}

	public AnalyzerBlock()
	{
		super(FabricBlockSettings.of(Material.WOOD).strength(2f, 2f).sounds(BlockSoundGroup.WOOD).build());
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof AnalyzerBlockEntity))
		{
			return false;
		}

		return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, 0, (stack) ->
		{
			// All of this is only called on the server-side
			DataHolder dataPlayer = ((DataHolder)player);
			if (stack.getItem() == Items.STICK)
			{
				if (!dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_STICK_KEY))
				{
					ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity) player);
					dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.ANALYZED_STICK_KEY, true);
					dataPlayer.markAdditionalDataDirty();
				}
			} else if (stack.getItem() == Blocks.CRAFTING_TABLE.getItem())
			{
				if (dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_STICK_KEY) && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_CRAFTING_TABLE_KEY))
				{
					ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) player);
					dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.ANALYZED_CRAFTING_TABLE_KEY, true);
					ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.TRANSFIGURATION.getID().toString(), false);
					dataPlayer.markAdditionalDataDirty();
				}
			} else if (stack.getItem() == Blocks.OBSIDIAN.getItem())
			{
				if (dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_SOUL_PENDANT_KEY) && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_OBSIDIAN_KEY))
				{
					ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) player);
					dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.ANALYZED_OBSIDIAN_KEY, true);
					ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.SOUL_COLLECTION.getID().toString(), false);
					dataPlayer.markAdditionalDataDirty();
				}
			} else
			{
				if (dataPlayer.getAdditionalData().getIntArray(ArcaneMagicConstants.GATHER_QUEST_ANALYZED_INDEXES_KEY).length < 4)
				{
					for (int index : dataPlayer.getAdditionalData().getIntArray(ArcaneMagicConstants.GATHER_QUEST_INDEXES_KEY))
					{
						Ingredient ingredient = DiscoveryParchment.GATHER_QUEST_OPTIONS[index];
						if (ingredient.method_8093(stack)) // apply
						{
							int[] analyzedArray = dataPlayer.getAdditionalData().getIntArray(ArcaneMagicConstants.GATHER_QUEST_ANALYZED_INDEXES_KEY);
							List<Integer> analyzed = new ArrayList<>();
							for (int analyzedID : analyzedArray)
							{
								analyzed.add(analyzedID);
							}
							analyzed.add(index);
							dataPlayer.getAdditionalData().putIntArray(ArcaneMagicConstants.GATHER_QUEST_ANALYZED_INDEXES_KEY, analyzed);
							dataPlayer.markAdditionalDataDirty();
							break;
						}
					}

					if (dataPlayer.getAdditionalData().getIntArray(ArcaneMagicConstants.GATHER_QUEST_ANALYZED_INDEXES_KEY).length >= 4)
					{
						ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity)player);
					}
				} else if (!dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.ANALYZED_STICK_KEY))
				{
					for (int index : dataPlayer.getAdditionalData().getIntArray(ArcaneMagicConstants.ANALYSIS_QUEST_INDEXES_KEY))
					{
						if (index != -1)
						{
							Ingredient ingredient = DiscoveryParchment.ANALYSIS_QUEST_OPTIONS[index];
							if (ingredient.method_8093(stack)) // apply
							{
								int[] analyzedArray = dataPlayer.getAdditionalData().getIntArray(ArcaneMagicConstants.ANALYSIS_QUEST_ANALYZED_INDEXES_KEY);
								List<Integer> analyzed = new ArrayList<>();
								for (int analyzedID : analyzedArray)
								{
									analyzed.add(analyzedID);
								}
								analyzed.add(index);
								dataPlayer.getAdditionalData().putIntArray(ArcaneMagicConstants.ANALYSIS_QUEST_ANALYZED_INDEXES_KEY, analyzed);
								dataPlayer.markAdditionalDataDirty();
								return;
							}
						}
					}
				}
			}
		});
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
		return new AnalyzerBlockEntity();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if (!world.isClient && placer instanceof PlayerEntity && !((DataHolder) placer).getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_ANALYZER_KEY))
		{
			ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity)placer);
			((DataHolder) placer).getAdditionalData().putBoolean(ArcaneMagicConstants.PLACED_ANALYZER_KEY, true);
			((DataHolder) placer).markAdditionalDataDirty();
		}
	}
}

package com.raphydaphy.arcanemagic.block;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.base.OrientableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.particle.ParticleSource;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.init.ModSounds;
import com.raphydaphy.arcanemagic.item.ScepterItem;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.recipe.TransfigurationRecipe;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Optional;

public class TransfigurationTableBlock extends OrientableBlockBase implements BlockEntityProvider
{
	private static final VoxelShape shape;

	static
	{
		VoxelShape bottom = Block.createCuboidShape(2, 0, 2, 14, 4, 14);
		VoxelShape middle = Block.createCuboidShape(0, 4, 0, 16, 8, 16);
		VoxelShape top = Block.createCuboidShape(3, 8, 3, 13, 10, 13);

		shape = VoxelShapes.union(VoxelShapes.union(bottom, middle), top);
	}

	public TransfigurationTableBlock()
	{
		super(FabricBlockSettings.of(Material.WOOD).strength(2f, 3f).sounds(BlockSoundGroup.WOOD).build());
	}

	public boolean useScepter(World world, BlockEntity blockEntity, ItemStack scepter, PlayerEntity player, Hand hand)
	{
		CompoundTag tag = scepter.getTag();
		if (tag != null)
		{
			Optional<TransfigurationRecipe> recipe = world.getRecipeManager().get(TransfigurationRecipe.TYPE, (TransfigurationTableBlockEntity) blockEntity, world);

			if (recipe.isPresent())
			{
				if (ArcaneMagicUtils.useSoul(world, scepter, player, recipe.get().getSoul()))
				{
					if (!world.isClient)
					{
						((TransfigurationTableBlockEntity) blockEntity).clearRecipe(world.isReceivingRedstonePower(blockEntity.getPos()));
						ItemEntity result = new ItemEntity(world, blockEntity.getPos().getX() + 0.5, blockEntity.getPos().getY() + 1, blockEntity.getPos().getZ() + 0.5, recipe.get().getOutput());
						result.setVelocity(0, 0, 0);
						world.spawnEntity(result);

						if (player != null)
						{
							DataHolder dataPlayer = (DataHolder) player;
							Item output = recipe.get().getOutput().getItem();
							boolean updated = false;

							if (output.getItem() == ModRegistry.GOLD_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_GOLD_CRYSTAL_KEY))
							{
								updated = true;
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_GOLD_CRYSTAL_KEY, true);
								ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.CRYSTALLIZATION.getID().toString(), false);
								ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.SOUL_STORAGE.getID().toString(), false);
							} else if (output.getItem() == ModRegistry.SOUL_PENDANT && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_SOUL_PENDANT_KEY))
							{
								updated = true;
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_SOUL_PENDANT_KEY, true);
								ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.SOUL_STORAGE.getID().toString(), false);
							} else if (output.getItem() == ModRegistry.COAL_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_COAL_CRYSTAL_KEY))
							{
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_COAL_CRYSTAL_KEY, true);
								if (ArcaneMagicUtils.craftedAllCrystals(dataPlayer))
								{
									updated = true;
									ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.PERFECTION.getID().toString(), false);
								}
							} else if (output.getItem() == ModRegistry.REDSTONE_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_REDSTONE_CRYSTAL_KEY))
							{
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_REDSTONE_CRYSTAL_KEY, true);
								if (ArcaneMagicUtils.craftedAllCrystals(dataPlayer))
								{
									updated = true;
									ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.PERFECTION.getID().toString(), false);
								}
							} else if (output.getItem() == ModRegistry.LAPIS_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_LAPIS_CRYSTAL_KEY))
							{
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_LAPIS_CRYSTAL_KEY, true);
								if (ArcaneMagicUtils.craftedAllCrystals(dataPlayer))
								{
									updated = true;
									ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.PERFECTION.getID().toString(), false);
								}
							} else if (output.getItem() == ModRegistry.DIAMOND_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_DIAMOND_CRYSTAL_KEY))
							{
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_DIAMOND_CRYSTAL_KEY, true);
								if (ArcaneMagicUtils.craftedAllCrystals(dataPlayer))
								{
									updated = true;
									ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.PERFECTION.getID().toString(), false);
								}
							} else if (output.getItem() == ModRegistry.EMERALD_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_EMERALD_CRYSTAL_KEY))
							{
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_EMERALD_CRYSTAL_KEY, true);
								if (ArcaneMagicUtils.craftedAllCrystals(dataPlayer))
								{
									updated = true;
									ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.PERFECTION.getID().toString(), false);
								}
							} else if (output.getItem() == ModRegistry.PURE_CRYSTAL && !dataPlayer.getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_PURE_CRYSTAL_KEY))
							{
								updated = true;
								dataPlayer.getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_PURE_CRYSTAL_KEY, true);
								ArcaneMagicUtils.updateNotebookSection(world, dataPlayer, NotebookSectionRegistry.PERFECTION.getID().toString(), false);
							}

							if (updated)
							{
								ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) player);
								dataPlayer.markAdditionalDataDirty();
							}
						}
					} else
					{
						doParticles(blockEntity.getPos());
					}
					if (player != null)
					{
						player.swingHand(hand);
					}

					world.playSound(player, blockEntity.getPos(), ModSounds.TRANSFIGURATION, SoundCategory.BLOCK, 0.5f, 1);

					return true;
				}
			}
		}
		return false;
	}

	private void doParticles(BlockPos pos)
	{
		float inverseSpread = 300;
		float colorDiviser = 5;
		ParticleRenderer.INSTANCE.addSource(new ParticleSource((age) -> {
			ParticleUtil.spawnGlowParticle(MinecraftClient.getInstance().world, pos.getX() + 0.5f + (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread, pos.getY() + 0.77f, pos.getZ() + 0.5f + (float)ArcaneMagic.RANDOM.nextGaussian() / inverseSpread,
					0, 0.01f, 0, ArcaneMagic.RANDOM.nextFloat() / colorDiviser, ArcaneMagic.RANDOM.nextFloat() / colorDiviser, ArcaneMagic.RANDOM.nextFloat() / colorDiviser, 1, true, 0.3f, 100);
		}, 12));
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (!(blockEntity instanceof TransfigurationTableBlockEntity))
		{
			return false;
		}

		ItemStack held = player.getStackInHand(hand);

		if (held.isEmpty() && hand == Hand.OFF && !player.getMainHandStack().isEmpty())
		{
			return false;
		}

		if (!held.isEmpty() && held.getItem() instanceof ScepterItem)
		{
			return useScepter(world, blockEntity, held, player, hand);
		}

		Vec3d hit = new Vec3d(hitResult.getPos().x - pos.getX(), hitResult.getPos().y - pos.getY(), hitResult.getPos().z - pos.getZ());

		if (hit.x >= 0.203 && hit.x <= 0.801 && hit.y >= 0.5625 && hit.z >= 0.203 && hit.z <= 0.801)
		{
			Direction facing = state.get(FACING);

			double divX = (hit.x - 0.203f);
			double divZ = (hit.z - 0.203f);

			int row = (divX <= 0.2152 ? 2 : divX <= 0.4084 ? 1 : 0);
			int col = (divZ <= 0.2152 ? 2 : divZ <= 0.4084 ? 1 : 0);

			int slot = row + col * 3;

			if (facing == Direction.EAST)
			{
				row = 2 - row;
				slot = row * 3 + col;
			} else if (facing == Direction.SOUTH)
			{
				slot = 8 - slot;
			} else if (facing == Direction.WEST)
			{
				col = 2 - col;
				slot = row * 3 + col;
			}

			return ArcaneMagicUtils.pedestalInteraction(world, player, blockEntity, hand, slot);
		}

		return false;
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

	@Override // Copied from net.minecraft.container.Container#calculateComparatorOutput
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
		return new TransfigurationTableBlockEntity();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
	{
		if (!world.isClient && placer instanceof PlayerEntity && !((DataHolder) placer).getAdditionalData().getBoolean(ArcaneMagicConstants.PLACED_TRANSFIGURATION_TABLE_KEY))
		{
			ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) placer);
			((DataHolder) placer).getAdditionalData().putBoolean(ArcaneMagicConstants.PLACED_TRANSFIGURATION_TABLE_KEY, true);
			ArcaneMagicUtils.updateNotebookSection(world, (DataHolder) placer, NotebookSectionRegistry.TRANSFIGURATION.getID().toString(), false);
			((DataHolder) placer).markAdditionalDataDirty();
		}
	}
}

package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.TransfigurationTableBlock;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.SmelterBlockEntity;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.core.common.LivingEntityHooks;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ScepterItem extends SoulStorageItem
{
	private static final String DRAIN_TARGET = "drain_target";
	public final int maxSoul;

	public ScepterItem(int maxSoul)
	{
		super(new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
		this.maxSoul = maxSoul;
		DispenserBlock.registerBehavior(this, new ScepterDispenserBehavior());
	}

	@Override
	public void onEntityTick(ItemStack scepter, World world, Entity holder, int slot, boolean selected)
	{
		if (world.getTime() % 20 == 0 && !world.isClient && selected && holder instanceof PlayerEntity)
		{
			int scepterSoul = scepter.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);
			CompoundTag scepterTag = scepter.getTag();
			if (scepterSoul < this.maxSoul && scepterTag != null)
			{
				int pendantSlot = ArcaneMagicUtils.findPendant((PlayerEntity) holder);
				if (pendantSlot != -1)
				{
					ItemStack pendant = ((PlayerEntity) holder).inventory.getInvStack(pendantSlot);
					int pendantSoul = pendant.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);
					CompoundTag pendantTag = pendant.getTag();

					if (pendantTag != null)
					{
						if (scepterSoul + pendantSoul <= this.maxSoul)
						{
							// Transfer all soul from the pendant into the scepter
							scepterTag.putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul + pendantSoul);
							pendantTag.putInt(ArcaneMagicConstants.SOUL_KEY, 0);
						} else
						{
							// Fill the scepter and leave the remaining soul in the pendant
							scepterTag.putInt(ArcaneMagicConstants.SOUL_KEY, maxSoul);
							pendantTag.putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul + pendantSoul - this.maxSoul);
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx)
	{
		CompoundTag tag = ctx.getItemStack().getTag();
		if (tag != null)
		{
			Block block = ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock();
			if (block == ModRegistry.CRYSTAL_INFUSER)
			{
				BlockEntity blockEntity = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
				if (blockEntity instanceof CrystalInfuserBlockEntity)
				{
					CrystalInfuserBlockEntity crystalInfuser = (CrystalInfuserBlockEntity) blockEntity;
					if (!crystalInfuser.getInvStack(0).isEmpty() && !crystalInfuser.getInvStack(1).isEmpty() && !crystalInfuser.getInvStack(2).isEmpty() && !((CrystalInfuserBlockEntity) blockEntity).isActive())
					{
						if (ArcaneMagicUtils.useSoul(ctx.getWorld(), ctx.getItemStack(), ctx.getPlayer(), 20))
						{
							if (!ctx.getWorld().isClient)
							{
								((CrystalInfuserBlockEntity) blockEntity).resetCraftingTime();
								((CrystalInfuserBlockEntity) blockEntity).setActive(true);
							}
							ctx.getWorld().playSound(ctx.getPlayer(), ctx.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCK, 1, 1);
							return ActionResult.SUCCESS;
						}
					}
				}
			} else if (block == ModRegistry.SMELTER)
			{
				BlockEntity blockEntity = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
				if (blockEntity instanceof SmelterBlockEntity)
				{
					SmelterBlockEntity smelter = (SmelterBlockEntity) blockEntity;
					if (smelter.startSmelting(true))
					{
						if (ArcaneMagicUtils.useSoul(ctx.getWorld(), ctx.getItemStack(), ctx.getPlayer(), ArcaneMagicConstants.SOUL_PER_SMELT))
						{
							smelter.startSmelting(false);
							ctx.getWorld().playSound(ctx.getPlayer(), ctx.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCK, 1, 1);
							return ActionResult.SUCCESS;
						}
					}
				}
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public UseAction getUseAction(ItemStack itemStack_1)
	{
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack itemStack_1)
	{
		return 32;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		return new TypedActionResult<>(ActionResult.FAIL, player.getStackInHand(hand));
	}

	@Override
	public boolean interactWithEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)
	{
		if (!(target instanceof PlayerEntity) && target.getHealthMaximum() <= player.getHealthMaximum())
		{
			if (player.getItemCooldownManager().isCooldown(stack.getItem()))
			{
				return false;
			}

			player.setCurrentHand(hand);
			player.getStackInHand(hand).getOrCreateTag().putInt(DRAIN_TARGET, target.getEntityId());

			if (!player.world.isClient)
			{
				target.addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 32, 128, true, false));
				target.addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 32, 128, true, false));
			}

			return true;
		}
		return false;
	}

	@Override
	public void onItemStopUsing(ItemStack stack, World world, LivingEntity player, int int_1)
	{
		CompoundTag tag = stack.getTag();
		if (!world.isClient && tag != null)
		{
			Entity drainTarget = player.world.getEntityById(tag.getInt(DRAIN_TARGET));
			if (drainTarget instanceof LivingEntity)
			{
				((LivingEntity) drainTarget).clearPotionEffects();
			}
			tag.remove(DRAIN_TARGET);
		}

		if (player instanceof PlayerEntity)
		{
			((PlayerEntity) player).getItemCooldownManager().set(stack.getItem(), 25);
		}
	}

	@Override
	public void onUsingTick(World world, LivingEntity player, ItemStack stack, int timeLeft)
	{
		if (player instanceof PlayerEntity)
		{
			CompoundTag tag = stack.getTag();
			if (tag != null)
			{
				Entity drainTarget = player.world.getEntityById(tag.getInt(DRAIN_TARGET));
				if (drainTarget instanceof LivingEntity)
				{
					if (world.isClient)
					{
						drainingParticles(world, player, (LivingEntity) drainTarget);
					}
					if (ArcaneMagic.RANDOM.nextInt(10) == 1)
					{
						if (!world.isClient)
						{
							((ServerWorld) world).method_14199(ParticleTypes.DAMAGE_INDICATOR, drainTarget.x, drainTarget.y + (double) (drainTarget.getHeight() * 0.5F), drainTarget.z, 0, 0.1D, 0.0D, 0.1D, 0.2D);
						}
						((LivingEntityHooks) drainTarget).playHurtSound(DamageSource.MAGIC);
					}
				}
			}
		}
	}

	private void drainingParticles(World world, LivingEntity player, LivingEntity target)
	{
		float travelTime = 50;
		float inverseSpread = 7;

		SpawnEggItem egg = SpawnEggItem.forEntity(target.getType());

		if (egg != null)
		{
			int color = egg.getColor(0);

			float red = ((color >> 16) & 0xFF) / 255f;
			float green = ((color >> 8) & 0xFF) / 255f;
			float blue = (color & 0xFF) / 255f;

			for (int i = 0; i < 5; i++)
			{
				ParticleUtil.spawnGlowParticle(world, (float) target.x + (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread, (float) target.y + target.getHeight() - 0.1f + (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread, (float) target.z + (float) ArcaneMagic.RANDOM.nextGaussian() / inverseSpread,
						(float) (player.x - target.x) / travelTime, (float) (player.y + 0.3f - target.y) / travelTime, (float) (player.z - target.z) / travelTime,
						red, green, blue, 0.1f, 0.2f, 100);
			}
		}
	}

	@Override
	public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity livingEntity)
	{
		CompoundTag tag = stack.getTag();
		if (!world.isClient && tag != null)
		{
			Entity mousedEntity = world.getEntityById(tag.getInt(DRAIN_TARGET));
			if (mousedEntity instanceof LivingEntity)
			{
				mousedEntity.damage(ModRegistry.DRAINED_DAMAGE, ((LivingEntity) mousedEntity).getHealthMaximum());
			}
			tag.remove(DRAIN_TARGET);
			ArcaneMagicUtils.addSoul(world, stack, livingEntity instanceof PlayerEntity ? (PlayerEntity) livingEntity : null, ArcaneMagic.RANDOM.nextInt(3) + 4);
		}

		if (livingEntity instanceof PlayerEntity)
		{
			((PlayerEntity) livingEntity).getItemCooldownManager().set(stack.getItem(), 50);
		}
		return stack;
	}

	private class ScepterDispenserBehavior extends ItemDispenserBehavior
	{
		@Override
		protected ItemStack dispenseStack(BlockPointer pointer, ItemStack stack)
		{
			World world = pointer.getWorld();

			BlockPos facingPos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
			BlockState facingBlock = world.getBlockState(facingPos);
			if (facingBlock.getBlock() instanceof TransfigurationTableBlock)
			{
				BlockEntity blockEntity = world.getBlockEntity(facingPos);
				if (blockEntity instanceof TransfigurationTableBlockEntity)
				{
					((TransfigurationTableBlock) facingBlock.getBlock()).useScepter(world, blockEntity, stack, null, null);
				}
			}

			return stack;
		}

		@Override
		protected void playSound(BlockPointer pointer)
		{
		}

		@Override
		protected void spawnParticles(BlockPointer pointer, Direction dir)
		{
			//pointer.getWorld().playEvent(2000, pointer.getBlockPos(), dir.getId());
		}
	}

	@Override
	public void onCrafted(ItemStack stack, World world, PlayerEntity player)
	{
		if (!world.isClient && player != null)
		{
			if (!((DataHolder)player).getAdditionalData().getBoolean(ArcaneMagicConstants.CRAFTED_SCEPTER_KEY))
			{
				ArcaneMagicPacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity) player);
				((DataHolder) player).getAdditionalData().putBoolean(ArcaneMagicConstants.CRAFTED_SCEPTER_KEY, true);
				ArcaneMagicUtils.updateNotebookSection(world, (DataHolder)player, NotebookSectionRegistry.DISCOVERY.getID().toString(), false);
				((DataHolder) player).markAdditionalDataDirty();
			}
		}
	}
}

package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.TransfigurationTableBlock;
import com.raphydaphy.arcanemagic.block.entity.TransfigurationTableBlockEntity;
import com.raphydaphy.arcanemagic.core.LivingEntityMixin;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
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

import java.util.Objects;
import java.util.Random;

public class ScepterItem extends Item
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
			if (scepterSoul < this.maxSoul && scepter.getTag() != null)
			{
				int pendantSlot = ArcaneMagicUtils.findPendant((PlayerEntity)holder);
				if (pendantSlot != -1)
				{
					ItemStack pendant = ((PlayerEntity)holder).inventory.getInvStack(pendantSlot);
					int pendantSoul = pendant.getOrCreateTag().getInt(ArcaneMagicConstants.SOUL_KEY);

					if (scepterSoul + pendantSlot <= this.maxSoul)
					{
						// Transfer all soul from the pendant into the scepter
						scepter.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul + pendantSoul);
						Objects.requireNonNull(pendant.getTag()).putInt(ArcaneMagicConstants.SOUL_KEY, 0);
					} else
					{
						// Fill the scepter and leave the remaining soul in the pendant
						scepter.getTag().putInt(ArcaneMagicConstants.SOUL_KEY, ArcaneMagicConstants.SOUL_PENDANT_MAX_SOUL);
						Objects.requireNonNull(pendant.getTag()).putInt(ArcaneMagicConstants.SOUL_KEY, scepterSoul + pendantSoul - this.maxSoul);
					}
				}
			}
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx)
	{
		if (ctx.getItemStack().getTag() != null && ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.CRAFTING_TABLE)
		{
			if (ArcaneMagicUtils.useSoul(ctx.getWorld(), ctx.getItemStack(), ctx.getPlayer(), 15))
			{
				if (!ctx.getWorld().isClient)
				{
					ctx.getWorld().setBlockState(ctx.getBlockPos(), ModRegistry.TRANSFIGURATION_TABLE.getPlacementState(new ItemPlacementContext(ctx)));
				} else
				{
					Random rand = new Random(System.currentTimeMillis());
					for (int i = 0; i < 30; i++)
					{
						ctx.getWorld().addParticle(ParticleTypes.PORTAL, ctx.getBlockPos().getX() + rand.nextFloat(), ctx.getBlockPos().getY() + rand.nextFloat() / 2d, ctx.getBlockPos().getZ() + rand.nextFloat(), 0, 0, 0);
					}
				}
				ctx.getWorld().playSound(ctx.getPlayer(), ctx.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCK, 1, 1);
				return ActionResult.SUCCESS;
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
		if (!world.isClient && stack.getTag() != null)
		{
			Entity drainTarget = player.world.getEntityById(stack.getTag().getInt(DRAIN_TARGET));
			if (drainTarget instanceof LivingEntity)
			{
				((LivingEntity) drainTarget).clearPotionEffects();
			}
			stack.getTag().remove(DRAIN_TARGET);
		}

		if (player instanceof PlayerEntity)
		{
			((PlayerEntity) player).getItemCooldownManager().set(stack.getItem(), 25);
		}
	}

	@Override
	public void method_7852(World world, LivingEntity player, ItemStack stack, int timeLeft) // usingUpdate
	{
		if (player instanceof PlayerEntity)
		{
			if (stack.getTag() != null)
			{
				Entity drainTarget = player.world.getEntityById(stack.getTag().getInt(DRAIN_TARGET));
				if (drainTarget instanceof LivingEntity)
				{
					Random rand = new Random(System.nanoTime());
					if (rand.nextInt(10) == 1)
					{
						if (!world.isClient)
						{
							((ServerWorld) world).method_14199(ParticleTypes.DAMAGE_INDICATOR, drainTarget.x, drainTarget.y + (double) (drainTarget.getHeight() * 0.5F), drainTarget.z, 0, 0.1D, 0.0D, 0.1D, 0.2D);
						}
						((LivingEntityMixin) drainTarget).playHurtSound(DamageSource.MAGIC);
					}
				}
			}
		}
	}

	@Override
	public ItemStack onItemFinishedUsing(ItemStack stack, World world, LivingEntity livingEntity)
	{
		if (!world.isClient && stack.getTag() != null)
		{
			Entity mousedEntity = world.getEntityById(stack.getTag().getInt(DRAIN_TARGET));
			if (mousedEntity != null)
			{
				mousedEntity.kill();
			}
			stack.getTag().remove(DRAIN_TARGET);
			Random rand = new Random(System.nanoTime());

			ArcaneMagicUtils.addSoul(world, stack, livingEntity instanceof PlayerEntity ? (PlayerEntity)livingEntity : null, rand.nextInt(3) + 4);
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
					((TransfigurationTableBlock)facingBlock.getBlock()).useScepter(world, blockEntity, stack, null, null);
				}
			}

			return stack;
		}

		@Override
		protected void playSound(BlockPointer pointer) { }

		@Override
		protected void spawnParticles(BlockPointer pointer, Direction dir)
		{
			//pointer.getWorld().playEvent(2000, pointer.getBlockPos(), dir.getId());
		}
	}
}

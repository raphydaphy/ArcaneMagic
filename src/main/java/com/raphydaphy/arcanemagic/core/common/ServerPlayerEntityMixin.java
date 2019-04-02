package com.raphydaphy.arcanemagic.core.common;

import com.mojang.authlib.GameProfile;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.Parchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.parchment.DiscoveryParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.packet.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity
{
	public ServerPlayerEntityMixin(World world, GameProfile profile)
	{
		super(world, profile);
	}

	@Shadow
	public abstract boolean isInTeleportationState();

	@Shadow private boolean inTeleportationState;

	@Shadow
	protected abstract void method_18783(ServerWorld serverWorld_1);

	@Shadow public ServerPlayNetworkHandler networkHandler;

	@Shadow @Final public ServerPlayerInteractionManager interactionManager;

	@Shadow private int field_13978;

	@Shadow private float field_13997;

	@Shadow private int field_13979;

	@Inject(at = @At("HEAD"), method = "method_14203")
	private void onPlayerClone(ServerPlayerEntity playerEntity, boolean keepEverything, CallbackInfo info) // copyFrom
	{
		if (((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY))
		{
			((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY, false);
			((PlayerEntity) (Object) this).addChatMessage(new TranslatableTextComponent("message.arcanemagic.parchment_lost").setStyle(new Style().setColor(TextFormat.DARK_PURPLE)), false);
			ArcaneMagicUtils.unlockRecipe((PlayerEntity) (Object) this, "written_parchment");
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "onDeath")
	private void onDeath(DamageSource source, CallbackInfo info)
	{
		if (!((PlayerEntity) (Object) this).world.isClient && !((PlayerEntity) (Object) this).world.getGameRules().getBoolean("keepInventory") && !((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY))
		{
			for (int slot = 0; slot < ((PlayerEntity) (Object) this).inventory.getInvSize(); slot++)
			{
				ItemStack stack = ((PlayerEntity) (Object) this).inventory.getInvStack(slot);
				if (stack.getItem() == ModRegistry.WRITTEN_PARCHMENT)
				{
					Parchment parchment = ParchmentRegistry.getParchment(stack);
					if (parchment instanceof DiscoveryParchment)
					{
						((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY, true);
						((DataHolder) this).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.SEND_PARCHMENT_RECIPE_ON_RESPAWN_KEY, true);
						((DataHolder) this).markAdditionalDataDirty();
						break;
					}
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "changeDimension", cancellable = true)
	private void changeDimension(DimensionType newDim, CallbackInfoReturnable<Entity> info)
	{
		this.inTeleportationState = true;
		DimensionType curDim = this.dimension;
		if (newDim == )
		if (curDim == DimensionType.THE_END && newDim == DimensionType.OVERWORLD)
		{
			this.detach();
			this.getServerWorld().removePlayer(this);
			if (!this.notInAnyWorld)
			{
				this.notInAnyWorld = true;
				this.networkHandler.sendPacket(new GameStateChangeS2CPacket(4, this.seenCredits ? 0.0F : 1.0F));
				this.seenCredits = true;
			}

			return this;
		} else
		{
			ServerWorld serverWorld_1 = this.server.getWorld(curDim);
			this.dimension = newDim;
			ServerWorld serverWorld_2 = this.server.getWorld(newDim);
			LevelProperties levelProperties_1 = this.world.getLevelProperties();
			this.networkHandler.sendPacket(new PlayerRespawnS2CPacket(newDim, levelProperties_1.getGeneratorType(), this.interactionManager.getGameMode()));
			this.networkHandler.sendPacket(new DifficultyS2CPacket(levelProperties_1.getDifficulty(), levelProperties_1.isDifficultyLocked()));
			PlayerManager playerManager_1 = this.server.getPlayerManager();
			playerManager_1.method_14576(this);
			serverWorld_1.removePlayer(this);
			this.removed = false;
			double playerX = this.x;
			double playerY = this.y;
			double playerZ = this.z;
			float pitch = this.pitch;
			float yaw = this.yaw;
			double double_4 = 8.0D;
			float float_3 = yaw;
			serverWorld_1.getProfiler().push("moving");
			if (curDim == DimensionType.OVERWORLD && newDim == DimensionType.THE_END)
			{
				BlockPos blockPos_1 = serverWorld_2.getForcedSpawnPoint();
				playerX = (double) blockPos_1.getX();
				playerY = (double) blockPos_1.getY();
				playerZ = (double) blockPos_1.getZ();
				yaw = 90.0F;
				pitch = 0.0F;
			}

			this.setPositionAndAngles(playerX, playerY, playerZ, yaw, pitch);
			serverWorld_1.getProfiler().pop();
			serverWorld_1.getProfiler().push("placing");
			double minX = Math.min(-2.9999872E7D, serverWorld_2.getWorldBorder().getBoundWest() + 16.0D);
			double minZ = Math.min(-2.9999872E7D, serverWorld_2.getWorldBorder().getBoundNorth() + 16.0D);
			double maxX = Math.min(2.9999872E7D, serverWorld_2.getWorldBorder().getBoundEast() - 16.0D);
			double maxZ = Math.min(2.9999872E7D, serverWorld_2.getWorldBorder().getBoundSouth() - 16.0D);
			playerX = MathHelper.clamp(playerX, minX, maxX);
			playerZ = MathHelper.clamp(playerZ, minZ, maxZ);
			this.setPositionAndAngles(playerX, playerY, playerZ, yaw, pitch);
			if (newDim == DimensionType.THE_END)
			{
				int int_1 = MathHelper.floor(this.x);
				int int_2 = MathHelper.floor(this.y) - 1;
				int int_3 = MathHelper.floor(this.z);

				for (int int_6 = -2; int_6 <= 2; ++int_6)
				{
					for (int int_7 = -2; int_7 <= 2; ++int_7)
					{
						for (int int_8 = -1; int_8 < 3; ++int_8)
						{
							int int_9 = int_1 + int_7;
							int int_10 = int_2 + int_8;
							int int_11 = int_3 - int_6;
							boolean boolean_1 = int_8 < 0;
							serverWorld_2.setBlockState(new BlockPos(int_9, int_10, int_11), boolean_1 ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
						}
					}
				}

				this.setPositionAndAngles((double) int_1, (double) int_2, (double) int_3, yaw, 0.0F);
				this.setVelocity(Vec3d.ZERO);
			} else if (!serverWorld_2.getPortalForcer().method_8653(this, float_3))
			{
				serverWorld_2.getPortalForcer().method_8654(this);
				serverWorld_2.getPortalForcer().method_8653(this, float_3);
			}

			serverWorld_1.getProfiler().pop();
			this.setWorld(serverWorld_2);
			serverWorld_2.method_18211((ServerPlayerEntity) (Object) this);
			this.method_18783(serverWorld_1);
			this.networkHandler.requestTeleport(this.x, this.y, this.z, yaw, pitch);
			this.interactionManager.setWorld(serverWorld_2);
			this.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(this.abilities));
			playerManager_1.method_14606((ServerPlayerEntity) (Object) this, serverWorld_2);
			playerManager_1.method_14594((ServerPlayerEntity) (Object) this);
			for (StatusEffectInstance statusInstance : this.getPotionEffects())
			{
				this.networkHandler.sendPacket(new EntityPotionEffectS2CPacket(this.getEntityId(), statusInstance));
			}
			this.networkHandler.sendPacket(new WorldEventS2CPacket(1032, BlockPos.ORIGIN, 0, false));
			this.field_13978 = -1;
			this.field_13997 = -1.0F;
			this.field_13979 = -1;
			info.setReturnValue(this);
		}
	}
}

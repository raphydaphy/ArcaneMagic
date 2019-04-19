package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import com.raphydaphy.crochet.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class DaggerItem extends SwordItem implements ICrystalEquipment {
    private final float speed;

    public DaggerItem(ToolMaterial material, int damage, float speed) {
        super(material, damage, speed, new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
        this.speed = speed;
    }

    public static UUID getDamageModifier() {
        return Item.MODIFIER_DAMAGE;
    }

    public static UUID getSpeedModifier() {
        return Item.MODIFIER_SWING_SPEED;
    }

    public static int activeDuration(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.containsKey(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY)) {
            ArcaneMagicUtils.ForgeCrystal crystal = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));
            if (crystal == ArcaneMagicUtils.ForgeCrystal.GOLD) {
                return 5 * 20;
            } else if (crystal == ArcaneMagicUtils.ForgeCrystal.COAL) {
                return 20 * 20;
            }
            return 10 * 20;
        }
        return 0;
    }

    public float getSpeed() {
        return this.speed;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        CompoundTag tag = stack.getTag();
        if (player.isSneaking() && tag != null && tag.containsKey(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY)) {
            if (!player.getItemCooldownManager().isCooldown(ModRegistry.IRON_DAGGER) && tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY) <= 0) {
                tag.putBoolean(ArcaneMagicConstants.DAGGER_IS_ACTIVE_KEY, true);
                tag.putInt(ArcaneMagicConstants.DAGGER_TIMER_KEY, activeDuration(stack));
                if (world.isClient) {
                    world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1);
                }
                return new TypedActionResult<>(ActionResult.SUCCESS, stack);
            }
        }
        return new TypedActionResult<>(ActionResult.PASS, stack);
    }

    @Override
    public void onEntityTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            int timer = tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY);
            if (timer > 0) {
                tag.putInt(ArcaneMagicConstants.DAGGER_TIMER_KEY, timer - 1);
                if (entity instanceof PlayerEntity) {
                    if (timer - 1 == 0) {
                        tag.putBoolean(ArcaneMagicConstants.DAGGER_IS_ACTIVE_KEY, false);
                        ((PlayerEntity) entity).getItemCooldownManager().set(stack.getItem(), ArcaneMagicConstants.DAGGER_ACTIVE_COOLDOWN);
                        if (world.isClient) {
                            world.playSound((PlayerEntity) entity, entity.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCrafted(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isClient && player != null) {
            if (!((DataHolder) player).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_DAGGER_KEY)) {
                PacketHandler.sendToClient(new ProgressionUpdateToastPacket(true), (ServerPlayerEntity) player);
                ((DataHolder) player).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.CRAFTED_DAGGER_KEY, true);
                ArcaneMagicUtils.updateNotebookSection(world, (DataHolder) player, NotebookSectionRegistry.ARMOURY.getID().toString(), false);
                ((DataHolder) player).markAdditionalDataDirty();
            }
        }
    }
}

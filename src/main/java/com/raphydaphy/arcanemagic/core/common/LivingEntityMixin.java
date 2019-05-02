package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModEvents;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ICrystalEquipment;
import com.raphydaphy.arcanemagic.network.ProgressionUpdateToastPacket;
import com.raphydaphy.arcanemagic.parchment.DiscoveryParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import com.raphydaphy.crochet.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected int playerHitTimer;

    @Shadow
    protected boolean dead;

    @Shadow public abstract boolean attack(Entity entity_1);

    @Inject(at = @At(value = "HEAD"), method = "dropLoot", cancellable = true)
    private void method_16077(DamageSource source, boolean killedByPlayer, CallbackInfo info) {
        if (source == ModRegistry.DRAINED_DAMAGE || ModEvents.shouldLivingEntityDropLoot(((LivingEntity) (Object) this), source)) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "damage")
    private void damage(DamageSource source, float float_1, CallbackInfoReturnable<Boolean> info) {
        Entity entity = source.getAttacker();
        if (entity instanceof LivingEntity) {
            ItemStack stack = ((LivingEntity) entity).getMainHandStack();
            CompoundTag tag;
            if (stack.getItem() instanceof ICrystalEquipment && (tag = stack.getTag()) != null) {
                ArcaneMagicUtils.ForgeCrystal active = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));
                int timer = tag.getInt(ArcaneMagicConstants.DAGGER_TIMER_KEY);
                if (active == ArcaneMagicUtils.ForgeCrystal.GOLD && timer > 0) {
                    // TODO: Disable hit cooldown.. how?
                    this.playerHitTimer = 0;
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    private void onDeath(DamageSource source, CallbackInfo info) {
        if (((LivingEntity) (Object) this).getType() == EntityType.DROWNED && !this.dead && !((LivingEntity) (Object) this).world.isClient) {
            Entity attacker = source.getAttacker();
            if (attacker instanceof PlayerEntity) {
                int kills = ((DataHolder) attacker).getAdditionalData(ArcaneMagic.DOMAIN).getInt(ArcaneMagicConstants.DROWNED_KILLS_KEY);
                int paper = -1;
                boolean giveParchment = false;

                if (kills < 5) {
                    if (kills < 2) {
                        for (int i = 0; i < ((PlayerEntity) attacker).inventory.getInvSize(); i++) {
                            if (((PlayerEntity) attacker).inventory.getInvStack(i).getItem() == Items.PAPER) {
                                paper = i;
                                break;
                            }
                        }

                        if (kills == 0) {
                            String message = "message.arcanemagic.drowned_first_kill";
                            if (paper != -1) {
                                message = "message.arcanemagic.drowned_paper_first";
                                kills++;
                                giveParchment = true;
                            }
                            ((PlayerEntity) attacker).addChatMessage(new TranslatableTextComponent(message).setStyle(new Style().setColor(TextFormat.DARK_PURPLE)), false);
                        } else if (kills == 1 && paper != -1 && !((DataHolder)attacker).getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.GIVEN_PARCHMENT_KEY)) {
                            giveParchment = true;
                            ((PlayerEntity) attacker).addChatMessage(new TranslatableTextComponent("message.arcanemagic.drowned_paper_second").setStyle(new Style().setColor(TextFormat.DARK_PURPLE)), false);
                        }
                    }

                    if (giveParchment) {
                        ItemStack parchment = new ItemStack(ModRegistry.WRITTEN_PARCHMENT);
                        parchment.getOrCreateTag().putString(ArcaneMagicConstants.PARCHMENT_TYPE_KEY, DiscoveryParchment.NAME);

                        List<Integer> gatherIndexes = new ArrayList<>();

                        while (gatherIndexes.size() < 4) {
                            int index = ArcaneMagic.RANDOM.nextInt(DiscoveryParchment.GATHER_QUEST_OPTIONS.length);
                            if (!gatherIndexes.contains(index)) {
                                gatherIndexes.add(index);
                            }
                        }

                        List<Integer> analysisIndexes = new ArrayList<>();

                        while (analysisIndexes.size() < 3) {
                            int index = ArcaneMagic.RANDOM.nextInt(DiscoveryParchment.ANALYSIS_QUEST_OPTIONS.length);
                            if (!analysisIndexes.contains(index)) {
                                analysisIndexes.add(index);
                            }
                        }

                        analysisIndexes.add(-1);
                        Collections.shuffle(analysisIndexes);

                        ((DataHolder)attacker).getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.GIVEN_PARCHMENT_KEY, true);
                        ((DataHolder) attacker).getAdditionalData(ArcaneMagic.DOMAIN).putIntArray(ArcaneMagicConstants.GATHER_QUEST_INDEXES_KEY, gatherIndexes);
                        ((DataHolder) attacker).getAdditionalData(ArcaneMagic.DOMAIN).putIntArray(ArcaneMagicConstants.ANALYSIS_QUEST_INDEXES_KEY, analysisIndexes);
                        ((DataHolder) attacker).markAdditionalDataDirty();

                        ItemStack paperStack = ((PlayerEntity) attacker).inventory.getInvStack(paper);
                        if (!paperStack.isEmpty()) {
                            paperStack.subtractAmount(1);
                        }
                        if (!((PlayerEntity) attacker).giveItemStack(parchment.copy())) {
                            ((LivingEntity) (Object) this).world.spawnEntity(new ItemEntity(((LivingEntity) (Object) this).world, attacker.x, attacker.y + 0.5, attacker.z, parchment.copy()));
                        }
                    }
                    if (kills != 1 || paper != -1) {
                        if (kills == 4) {
                            PacketHandler.sendToClient(new ProgressionUpdateToastPacket(false), (ServerPlayerEntity) attacker);
                        }
                        ((DataHolder) attacker).getAdditionalData(ArcaneMagic.DOMAIN).putInt(ArcaneMagicConstants.DROWNED_KILLS_KEY, kills + 1);
                        ((DataHolder) attacker).markAdditionalDataDirty();
                    }
                }
            }
        }
    }
}

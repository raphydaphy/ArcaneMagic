package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.ChatFormat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class, priority = 2000)
public abstract class ServerPlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "copyFrom")
    private void onPlayerClone(ServerPlayerEntity playerEntity, boolean keepEverything, CallbackInfo info)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        DataHolder oldDataPlayer = (DataHolder) player;

        if (!player.world.getGameRules().getBoolean("keepInventory")
                && !oldDataPlayer.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY)
                && oldDataPlayer.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.GIVEN_PARCHMENT_KEY)
                && !oldDataPlayer.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_SCEPTER_KEY)) {
            oldDataPlayer.getAdditionalData(ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.DIED_WITH_PARCHMENT_KEY, true);
            oldDataPlayer.markAdditionalDataDirty();
            ((PlayerEntity) (Object) this).addChatMessage(new TranslatableComponent("message.arcanemagic.parchment_lost").setStyle(new Style().setColor(ChatFormat.DARK_PURPLE)), false);
            ArcaneMagicUtils.unlockRecipe((PlayerEntity) (Object) this, "written_parchment");
        }
    }
}

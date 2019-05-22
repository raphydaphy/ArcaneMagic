package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.Parchment;
import com.raphydaphy.arcanemagic.client.screen.ParchmentScreen;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.parchment.DiscoveryParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.crochet.data.PlayerData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ParchmentItem extends Item {
    public final ParchmentType type;

    public ParchmentItem(ParchmentType type) {
        super(new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(type == ParchmentType.BLANK ? 64 : 1));
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == ModRegistry.WRITTEN_PARCHMENT && !player.isSneaking()) {
            Parchment parchment = ParchmentRegistry.getParchment(stack);
            if (parchment != null && (((ParchmentItem) stack.getItem()).type == ParchmentType.ANCIENT) == parchment.isAncient()) {
                if (!world.isClient && !PlayerData.get(player, ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.GIVEN_PARCHMENT_KEY)) {
                    PlayerData.get(player, ArcaneMagic.DOMAIN).putBoolean(ArcaneMagicConstants.GIVEN_PARCHMENT_KEY, true);
                    PlayerData.markDirty(player);
                }
                parchment.onOpened(world, player, hand, stack);
                if (world.isClient) {
                    openGUI(stack, parchment);
                }
                return new TypedActionResult<>(ActionResult.SUCCESS, stack);
            } else {
                if (!world.isClient) {
                    player.setStackInHand(hand, new ItemStack(ModRegistry.PARCHMENT, stack.getAmount()));
                }
                return new TypedActionResult<>(ActionResult.FAIL, stack);
            }
        }

        if (stack.getItem() == ModRegistry.PARCHMENT && player.isSneaking()) {
            if (!world.isClient) {
                if (player.dimension == ModRegistry.VOID_DIM) {
                    player.changeDimension(DimensionType.OVERWORLD);
                } else {
                    player.changeDimension(ModRegistry.VOID_DIM);
                }
            }
            return new TypedActionResult<>(ActionResult.SUCCESS, stack);
        }
        return new TypedActionResult<>(ActionResult.PASS, stack);
    }

    @Environment(EnvType.CLIENT)
    public boolean hasEnchantmentGlint(ItemStack stack) {
        return type == ParchmentType.ANCIENT;
    }

    @Environment(EnvType.CLIENT)
    private void openGUI(ItemStack stack, Parchment parchment) {
        MinecraftClient.getInstance().openScreen(new ParchmentScreen(stack, parchment));
    }

    @Override
    public void onCrafted(ItemStack stack, World world, PlayerEntity player) {
        if (type == ParchmentType.WRITTEN) {
            stack.getOrCreateTag().putString(ArcaneMagicConstants.PARCHMENT_TYPE_KEY, DiscoveryParchment.NAME);
        }
    }

    public enum ParchmentType {
        BLANK, WRITTEN, ANCIENT
    }
}

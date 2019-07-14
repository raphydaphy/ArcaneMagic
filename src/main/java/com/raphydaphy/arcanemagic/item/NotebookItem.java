package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.screen.NotebookScreen;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.notebook.NotebookSectionRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NotebookItem extends Item {
    public NotebookItem() {
        super(new Item.Settings().maxCount(1).group(ArcaneMagic.GROUP));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.containsKey(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY)) {
                tag.putString(ArcaneMagicConstants.NOTEBOOK_SECTION_KEY, NotebookSectionRegistry.CONTENTS.getID().toString());
                tag.putInt(ArcaneMagicConstants.NOTEBOOK_PAGE_KEY, 0);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!player.isSneaking()) {
            if (world.isClient) {
                player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1, 1);
                openGUI(stack);
            }
            return new TypedActionResult<>(ActionResult.SUCCESS, stack);
        }
        return new TypedActionResult<>(ActionResult.PASS, stack);
    }

    @Environment(EnvType.CLIENT)
    private void openGUI(ItemStack stack) {
        MinecraftClient.getInstance().openScreen(new NotebookScreen(stack));
    }
}

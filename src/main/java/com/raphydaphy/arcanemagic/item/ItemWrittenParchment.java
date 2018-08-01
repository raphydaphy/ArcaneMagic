package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.client.gui.GUINotebook;
import com.raphydaphy.arcanemagic.client.gui.GUIParchment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWrittenParchment extends Item
{
    private final boolean ancient;

    public ItemWrittenParchment(boolean ancient)
    {
        super(new Item.Builder().group(ItemGroup.MISC));
        this.ancient = ancient;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return ancient;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
        {
            openGUI(player, stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    private void openGUI(EntityPlayer player, ItemStack stack)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GUIParchment(player, stack));
    }
}

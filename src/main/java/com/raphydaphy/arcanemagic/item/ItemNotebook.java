package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.client.gui.GuiNotebook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemNotebook extends Item
{
    public ItemNotebook()
    {
        super(new Item.Builder().group(ItemGroup.MISC).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote)
        {
            openGUI(player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    void openGUI(EntityPlayer player)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiNotebook(player));
    }
}

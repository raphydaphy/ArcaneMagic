package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.client.gui.GuiParchment;
import com.raphydaphy.arcanemagic.parchment.IParchment;
import com.raphydaphy.arcanemagic.parchment.ParchmentRegistry;
import com.raphydaphy.arcanemagic.parchment.ParchmentWizardHut;
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
    public static final String PARCHMENT_KEY = "key.arcanemagic.parchment_type";

    private final boolean ancient;

    public ItemWrittenParchment(boolean ancient)
    {
        super(new Item.Builder().group(ItemGroup.MISC).maxStackSize(1));
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
        if (!player.isSneaking())
        {

            IParchment parchment = ParchmentRegistry.getParchment(stack);
            if (parchment != null && parchment.isAncient() == ancient)
            {
                if (world.isRemote)
                {
                    openGUI(stack, parchment);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
            player.setHeldItem(hand, new ItemStack(ArcaneMagic.PARCHMENT, stack.getCount()));
            player.openContainer.detectAndSendChanges();
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    private void openGUI(ItemStack stack, IParchment parchment)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiParchment(stack, parchment));
    }
}

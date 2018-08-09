package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.BlockAltar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemTransformer extends Item
{
    public ItemTransformer()
    {
        super(new Item.Builder().group(ItemGroup.MISC));
    }

    // OnItemUse
    public EnumActionResult func_195939_a(ItemUseContext context)
    {
        World world = context.func_195991_k();
        IBlockState state = world.getBlockState(context.func_195995_a());
        if (state.getBlock() == ArcaneMagic.ALTAR && !state.getValue(BlockAltar.HAS_TRANSFORMER))
        {
            if (!world.isRemote)
            {
                context.func_195996_i().shrink(1);
                world.setBlockState(context.func_195995_a(), state.withProperty(BlockAltar.HAS_TRANSFORMER, true));
                EntityPlayer player = context.func_195999_j();
                world.playSound(null, context.func_195995_a(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1, 1);
                if (player != null)
                {
                    player.openContainer.detectAndSendChanges();
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}

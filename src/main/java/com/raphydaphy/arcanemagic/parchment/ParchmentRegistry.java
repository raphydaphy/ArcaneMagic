package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.item.ItemWrittenParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class ParchmentRegistry
{
    private static final List<IParchment> REGISTRY = new ArrayList<>();


    // TODO: Forge complete() event
    static
    {
        REGISTRY.add(new ParchmentDrownedDiscovery());
    }

    public static IParchment getParchment(ItemStack from)
    {
        if (from.getItem() instanceof ItemWrittenParchment)
        {
            System.out.println("1");
            NBTTagCompound tag = from.getTagCompound();
            if (tag != null && tag.hasKey(ArcaneMagicResources.PARCHMENT_KEY))
            {
                System.out.println("2");
                String key = tag.getString(ArcaneMagicResources.PARCHMENT_KEY);
                for (IParchment parchment : REGISTRY)
                {
                    System.out.println("3");
                    if (parchment.getName().equals(key))
                    {
                        System.out.println("4");
                        return parchment;
                    }
                }
            }
        }
        return null;
    }
}

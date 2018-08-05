package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.item.ItemWrittenParchment;
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
        REGISTRY.add(new ParchmentWizardHut());
    }

    public static IParchment getParchment(ItemStack from)
    {
        if (from.getItem() instanceof ItemWrittenParchment)
        {
            NBTTagCompound tag = from.getTagCompound();
            if (tag != null && tag.hasKey(ItemWrittenParchment.PARCHMENT_KEY))
            {
                String key = tag.getString(ItemWrittenParchment.PARCHMENT_KEY);
                for (IParchment parchment : REGISTRY)
                {
                    if (parchment.getName().equals(key))
                    {
                        return parchment;
                    }
                }
            }
        }
        return null;
    }
}

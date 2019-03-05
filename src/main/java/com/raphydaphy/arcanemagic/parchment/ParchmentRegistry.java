package com.raphydaphy.arcanemagic.parchment;

import com.raphydaphy.arcanemagic.api.parchment.IParchment;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.item.ParchmentItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class ParchmentRegistry
{
	private static final List<IParchment> REGISTRY = new ArrayList<>();

	static
	{
		// Add parchments here
		REGISTRY.add(new DiscoveryParchment());
	}

	public static IParchment getParchment(ItemStack from)
	{
		if (from.getItem() instanceof ParchmentItem && ((ParchmentItem) from.getItem()).type != ParchmentItem.ParchmentType.BLANK)
		{
			CompoundTag tag = from.getTag();
			if (tag != null && tag.containsKey(ArcaneMagicConstants.PARCHMENT_TYPE_KEY))
			{
				String key = tag.getString(ArcaneMagicConstants.PARCHMENT_TYPE_KEY);
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

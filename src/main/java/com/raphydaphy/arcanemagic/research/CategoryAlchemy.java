package com.raphydaphy.arcanemagic.research;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.api.research.IThaumonomiconCategory;
import com.raphydaphy.arcanemagic.api.research.IThaumonomiconEntry;
import com.raphydaphy.arcanemagic.api.util.IconTypePair;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.api.util.IconTypePair.IconType;

import net.minecraft.util.ResourceLocation;

public class CategoryAlchemy implements IThaumonomiconCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "thaumcraft.research.category.alchemy";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_crucible.png");
	}

	@Override
	public List<IThaumonomiconEntry> getEntries()
	{
		List<IThaumonomiconEntry> entries = new ArrayList<IThaumonomiconEntry>();
		entries.add(new EntryBasicAlchemy());
		return entries;
	}

	public class EntryBasicAlchemy implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.alchemy.basic_alchemy";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/mic/research/r_crucible.png"), IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 270);
		}
	}
}

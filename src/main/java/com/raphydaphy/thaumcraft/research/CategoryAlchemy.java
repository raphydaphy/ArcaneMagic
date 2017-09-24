package com.raphydaphy.thaumcraft.research;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.api.research.IThaumonomiconCategory;
import com.raphydaphy.thaumcraft.api.research.IThaumonomiconEntry;
import com.raphydaphy.thaumcraft.api.util.IconTypePair;
import com.raphydaphy.thaumcraft.api.util.IconTypePair.IconType;
import com.raphydaphy.thaumcraft.api.util.Pos2;

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

package com.raphydaphy.arcanemagic.notebook;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.util.IconTypePair;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.api.util.IconTypePair.IconType;

import net.minecraft.util.ResourceLocation;

public class CategoryAlchemy implements INotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.alchemy";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(ArcaneMagic.MODID, "textures/misc/notebook/r_crucible.png");
	}

	@Override
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		entries.add(new EntryBasicAlchemy());
		return entries;
	}

	public class EntryBasicAlchemy implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.alchemy.basic_alchemy";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/misc/notebook/r_crucible.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 270);
		}
	}
}

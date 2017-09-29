package com.raphydaphy.arcanemagic.notebook;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

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

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "basic_alchemy");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
		}
	}
	
	@Override
	public Pair<ResourceLocation, Pos2> getBackground()
	{
		return Pair.of(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/notebook_back_tc_eldritch.png"), new Pos2(512, 512));
	}

	private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "category_alchemy");
	
	@Override
	public ResourceLocation getRegistryName() {
		return LOC;
	}
}

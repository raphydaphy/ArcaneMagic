package com.raphydaphy.arcanemagic.notebook;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.util.IconTypePair;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.api.util.IconTypePair.IconType;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryThaumaturgy implements INotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.thaumaturgy";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(ArcaneMagic.MODID, "textures/misc/notebook/r_thaumaturgy.png");
	}

	@Override
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		entries.add(new EntryBasicScepter());
		entries.add(new EntryBasicFoci());
		return entries;
	}

	public class EntryBasicScepter implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.thaumaturgy.basic_scepter";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ItemStack(ModRegistry.SCEPTER), IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 250);
		}
	}

	public class EntryBasicFoci implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.thaumaturgy.basic_foci";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ItemStack(ModRegistry.SHARD), IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(290, 210);
		}
	}
}

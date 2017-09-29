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

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "basic_scepter");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
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
			return new IconTypePair(new ItemStack(ModRegistry.ESSENCE), IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(290, 210);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "basic_foci");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
		}
	}
	
	@Override
	public Pair<ResourceLocation, Pos2> getBackground()
	{
		return Pair.of(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/notebook_back_tc.png"), new Pos2(512, 512));
	}

	private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "category_thaum");
	
	@Override
	public ResourceLocation getRegistryName() {
		return LOC;
	}
}

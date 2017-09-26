package com.raphydaphy.arcanemagic.research;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.api.research.IThaumonomiconCategory;
import com.raphydaphy.arcanemagic.api.research.IThaumonomiconEntry;
import com.raphydaphy.arcanemagic.api.util.IconTypePair;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.api.util.IconTypePair.IconType;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryThaumaturgy implements IThaumonomiconCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "thaumcraft.research.category.thaumaturgy";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_thaumaturgy.png");
	}

	@Override
	public List<IThaumonomiconEntry> getEntries()
	{
		List<IThaumonomiconEntry> entries = new ArrayList<IThaumonomiconEntry>();
		entries.add(new EntryBasicWand());
		entries.add(new EntryBasicFoci());
		return entries;
	}

	public class EntryBasicWand implements IThaumonomiconEntry
	{
		@Override public String getUnlocalizedName() { return "thaumcraft.research.category.thaumaturgy.basic_wand"; }
		@Override public IconTypePair getIcon() { return new IconTypePair(new ItemStack(ModRegistry.WAND), IconType.CIRCLE); }
		@Override public Pos2 getPos() { return new Pos2(260, 250); }
	}
	
	public class EntryBasicFoci implements IThaumonomiconEntry
	{
		@Override public String getUnlocalizedName() { return "thaumcraft.research.category.thaumaturgy.basic_foci"; }
		@Override public IconTypePair getIcon() { return new IconTypePair(new ItemStack(ModRegistry.SHARD), IconType.CIRCLE); }
		@Override public Pos2 getPos() { return new Pos2(290, 210); }
	}
}

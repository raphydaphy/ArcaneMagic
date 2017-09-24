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

public class CategoryBasicInformation implements IThaumonomiconCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "thaumcraft.research.category.basic_info";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(Thaumcraft.MODID, "textures/items/thaumonomicon.png");
	}

	@Override
	public List<IThaumonomiconEntry> getEntries()
	{
		List<IThaumonomiconEntry> entries = new ArrayList<IThaumonomiconEntry>();
		entries.add(new EntryAspects());
		entries.add(new EntryThaumonomicon());
		entries.add(new EntryResearch());
		entries.add(new EntryKnowledgeFragments());
		entries.add(new EntryNodesIntro());
		entries.add(new EntryWarp());
		return entries;
	}

	public class EntryAspects implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.basic_info.aspects";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_aspects.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 250);
		}
	}

	public class EntryThaumonomicon implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.basic_info.thaumonomicon";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/items/thaumonomicon.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(290, 200);
		}
	}

	public class EntryResearch implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.basic_info.research";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/items/scribing_tools.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(320, 250);
		}
	}

	public class EntryKnowledgeFragments implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.basic_info.knowledge_fragments";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/items/knowledge_fragment.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(350, 200);
		}
	}

	public class EntryNodesIntro implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.basic_info.nodes_intro";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_nodes1.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(200, 250);
		}
	}

	public class EntryWarp implements IThaumonomiconEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "thaumcraft.research.category.basic_info.warp";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_warp.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 300);
		}
	}

}

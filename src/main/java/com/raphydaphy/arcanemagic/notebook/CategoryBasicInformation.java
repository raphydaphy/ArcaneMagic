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

public class CategoryBasicInformation implements INotebookCategory
{
	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.basic_info";
	}

	@Override
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(ArcaneMagic.MODID, "textures/items/notebook.png");
	}

	@Override
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		entries.add(new EntryAspects());
		entries.add(new EntryThaumonomicon());
		entries.add(new Entrynotebook());
		entries.add(new EntryKnowledgeFragments());
		entries.add(new EntryNodesIntro());
		entries.add(new EntryWarp());
		return entries;
	}

	public class EntryAspects implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.aspects";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/misc/notebook/r_aspects.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 250);
		}
	}

	public class EntryThaumonomicon implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.thaumonomicon";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/items/notebook.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(290, 200);
		}
	}

	public class Entrynotebook implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.notebook";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/items/scribing_tools.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(320, 250);
		}
	}

	public class EntryKnowledgeFragments implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.knowledge_fragments";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/items/ancient_parchment.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(350, 200);
		}
	}

	public class EntryNodesIntro implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.nodes_intro";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/misc/notebook/r_nodes1.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(200, 250);
		}
	}

	public class EntryWarp implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.warp";
		}

		@Override
		public IconTypePair getIcon()
		{
			return new IconTypePair(new ResourceLocation(ArcaneMagic.MODID, "textures/misc/notebook/r_warp.png"),
					IconType.CIRCLE);
		}

		@Override
		public Pos2 getPos()
		{
			return new Pos2(260, 300);
		}
	}

}

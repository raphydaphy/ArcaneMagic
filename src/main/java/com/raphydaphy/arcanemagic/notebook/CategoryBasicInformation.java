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
		entries.add(new EntryBasicLinguistics());
		entries.add(new EntryElementalParticles());
		entries.add(new EntryNotebook());
		entries.add(new EntryKnowledgeFragments());
		entries.add(new EntryNodesIntro());
		entries.add(new EntryWarp());
		return entries;
	}

	public class EntryBasicLinguistics implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.basic_linguistics";
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
			return new Pos2(265, 150);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "basic_linguistics");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
		}
	}

	public class EntryElementalParticles implements INotebookEntry
	{
		@Override
		public String getUnlocalizedName()
		{
			return "arcanemagic.notebook.category.basic_info.elemental_particles";
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
			return new Pos2(200, 220);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "particles");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
		}
	}

	public class EntryNotebook implements INotebookEntry
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
			return new Pos2(350, 280);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "notebook");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
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
			return new Pos2(350, 220);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "fragments");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
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
			return new Pos2(200, 280);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "basic_nodes");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
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
			return new Pos2(260, 350);
		}

		private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "basic_warp");
		
		@Override
		public ResourceLocation getRegistryName() {
			return LOC;
		}
	}

	@Override
	public Pair<ResourceLocation, Pos2> getBackground()
	{
		return Pair.of(new ResourceLocation(ArcaneMagic.MODID, "textures/gui/notebook_back_core.png"), new Pos2(1920, 1200));
	}

	private final ResourceLocation LOC = new ResourceLocation(ArcaneMagic.MODID, "category_basics");
	
	@Override
	public ResourceLocation getRegistryName() {
		return LOC;
	}

}

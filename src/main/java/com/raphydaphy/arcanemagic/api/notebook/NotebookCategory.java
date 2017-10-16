package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import akka.japi.Pair;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class NotebookCategory extends IForgeRegistryEntry.Impl<NotebookCategory>
{

	public static final CategoryRegistry REGISTRY = new CategoryRegistry();

	public abstract String getUnlocalizedName();

	public abstract List<INotebookEntry> getEntries();
	
	public Pair<String, Integer> getUnlocParchmentInfo()
	{
		return new Pair<String, Integer>("arcanemagic.notebook.category.unknown_realms", 2);
	}

	public String getRequiredTag()
	{
		return "usedNotebook";
	}

	public String getPrerequisiteTag()
	{
		return "usedNotebook";
	}

	public FontRenderer getFontRenderer(GuiScreen notebook)
	{
		return notebook.mc.fontRenderer;
	}

	public abstract ItemStack getIcon();

}

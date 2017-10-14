package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class NotebookCategory extends IForgeRegistryEntry.Impl<NotebookCategory>
{

	public static final CategoryRegistry REGISTRY = new CategoryRegistry();

	public abstract String getUnlocalizedName();

	public abstract List<INotebookEntry> getEntries();

	public String getRequiredTag()
	{
		return "usedNotebook";
	}

	public String getPrerequisiteTag()
	{
		return getRequiredTag();
	}
	
	public FontRenderer getFontRenderer(GuiScreen notebook)
	{
		return notebook.mc.fontRenderer;
	}
	
	public abstract ItemStack getIcon();

}

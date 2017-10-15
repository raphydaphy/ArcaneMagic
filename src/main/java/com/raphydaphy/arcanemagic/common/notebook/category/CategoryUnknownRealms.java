package com.raphydaphy.arcanemagic.common.notebook.category;

import java.util.ArrayList;
import java.util.List;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.notebook.entry.NotebookEntryText;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CategoryUnknownRealms extends NotebookCategory
{

	@Override
	public String getUnlocalizedName()
	{
		return "arcanemagic.notebook.category.unknown_realms";
	}

	@Override
	public List<INotebookEntry> getEntries()
	{
		List<INotebookEntry> entries = new ArrayList<INotebookEntry>();
		for (int i = 0; i < 3; i++)
		{
			entries.add(new NotebookEntryText(getUnlocalizedName() + "." + i, 0x000000,
					Minecraft.getMinecraft().standardGalacticFontRenderer));
		}
		return entries;
	}

	@Override
	public String getRequiredTag()
	{
		return null;
	}

	@Override
	public FontRenderer getFontRenderer(GuiScreen notebook)
	{
		return notebook.mc.standardGalacticFontRenderer;
	}

	@Override
	public ItemStack getIcon()
	{
		return new ItemStack(Blocks.BARRIER);
	}
}

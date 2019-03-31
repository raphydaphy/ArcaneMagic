package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.docs.NotebookElement;
import com.raphydaphy.arcanemagic.api.docs.NotebookSection;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.crochet.data.DataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CrystallizationNotebookSection implements NotebookSection
{
	@Override
	public Identifier getID()
	{
		return new Identifier(ArcaneMagic.DOMAIN, "crystallization");
	}

	@Override
	public boolean isVisibleTo(DataHolder player)
	{
		return player.getAdditionalData(ArcaneMagic.DOMAIN).getBoolean(ArcaneMagicConstants.CRAFTED_GOLD_CRYSTAL_KEY);
	}

	@Override
	public List<NotebookElement> getElements(DataHolder player, int page)
	{
		List<NotebookElement> elements = new ArrayList<>();

		int textPages = BasicNotebookElements.textPages("notebook.arcanemagic.crystallization.0", 2);
		if (page == 0)
		{
			elements.add(new BasicNotebookElements.SmallHeading("notebook.arcanemagic.crystallization.title").withPadding(3));
		} else
		{
			elements.add(new BasicNotebookElements.Padding(3));
		}

		if (page <= textPages)
		{
			elements.addAll(BasicNotebookElements.wrapText("notebook.arcanemagic.crystallization.0", 2, 0, page));
		} else
		{
			elements.add(new BasicNotebookElements.Padding(3));
			if (page == textPages + 1)
			{
				elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.coal_crystal").withPadding(10));
				elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "coal_crystal")).orElse(null)));
			} else if (page == textPages + 2)
			{
				elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.lapis_crystal").withPadding(10));
				elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "lapis_crystal")).orElse(null)));
			} else if (page == textPages + 3)
			{
				elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.redstone_crystal").withPadding(10));
				elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "redstone_crystal")).orElse(null)));
			} else if (page == textPages + 4)
			{
				elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.diamond_crystal").withPadding(10));
				elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "diamond_crystal")).orElse(null)));
			} else if (page == textPages + 5)
			{
				elements.add(new BasicNotebookElements.Paragraph(true, 1, "item.arcanemagic.emerald_crystal").withPadding(10));
				elements.add(new BasicNotebookElements.Recipe(MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(ArcaneMagic.DOMAIN, "emerald_crystal")).orElse(null)));
			}
		}
		return elements;
	}

	@Override
	public int getPageCount(DataHolder player)
	{
		return BasicNotebookElements.textPages("notebook.arcanemagic.crystallization.0", 2) + 5;
	}
}

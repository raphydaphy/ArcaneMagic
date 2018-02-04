package com.raphydaphy.arcanemagic.common.notebook.entry;

import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class NotebookEntryText implements INotebookEntry {
	private String unlocalizedText;
	private int color;

	private FontRenderer renderer;

	public NotebookEntryText(String unlocalizedText, int color) {
		this(unlocalizedText, color, Minecraft.getMinecraft().fontRenderer);
	}

	public NotebookEntryText(String unlocalizedText, int color, FontRenderer renderer) {
		this.unlocalizedText = unlocalizedText;
		this.color = color;
		this.renderer = renderer;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return new ResourceLocation(ArcaneMagic.MODID, "notebook_entry_text");
	}

	@Override
	public void draw(int x, int y, int mouseX, int mouseY, GuiScreen notebook) {
		renderer.drawSplitString(I18n.format(unlocalizedText), x, y, 180, color);
	}

	@Override
	public int getHeight(GuiScreen notebook) {
		return renderer.getWordWrappedHeight(I18n.format(unlocalizedText), 180);
	}

	@Override
	public boolean containsSearchKey(String searchKey) {
		return I18n.format(unlocalizedText).toLowerCase().contains(searchKey);
	}
}

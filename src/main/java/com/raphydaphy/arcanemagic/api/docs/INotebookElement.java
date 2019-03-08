package com.raphydaphy.arcanemagic.api.docs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;

public interface INotebookElement
{
	@Environment(EnvType.CLIENT)
	int draw(Screen screen, int x, int y, int mouseX, int mouseY, int xTop, int yTop);
}

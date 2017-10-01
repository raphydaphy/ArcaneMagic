package com.raphydaphy.arcanemagic.api.notebook;

import java.util.List;

import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class NotebookCategory extends IForgeRegistryEntry.Impl<NotebookCategory> {
	
	public static final CategoryRegistry REGISTRY = new CategoryRegistry();
	
	public abstract String getUnlocalizedName();
	
	public abstract List<INotebookEntry> getEntries();

}

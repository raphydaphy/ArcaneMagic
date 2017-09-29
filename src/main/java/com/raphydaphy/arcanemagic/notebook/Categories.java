package com.raphydaphy.arcanemagic.notebook;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;

public class Categories {
	
	public static final INotebookCategory ALCHEMY = new CategoryAlchemy();
	public static final INotebookCategory BASIC_INFO = new CategoryBasicInformation();
	public static final INotebookCategory THAUM = new CategoryThaumaturgy();
	
	private static boolean done = false;
			
	public static void register() {
		if(done) return;
		done = true;
		ArcaneMagicAPI.registerCategory(ALCHEMY);
		ArcaneMagicAPI.registerCategory(BASIC_INFO);
		ArcaneMagicAPI.registerCategory(THAUM);
	}

}

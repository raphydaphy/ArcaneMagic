package com.raphydaphy.arcanemagic.common.capabilities;

import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;

import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by Xander V on 27/09/2017.
 */
public class Capabilities
{

	public static AnimaStorage.DefaultStorage storage = new AnimaStorage.DefaultStorage();
	public static NotebookInfo.DefaultInfo info = new NotebookInfo.DefaultInfo();

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IAnimaStorage.class, storage, AnimaStorage.class);
		CapabilityManager.INSTANCE.register(INotebookInfo.class, info, NotebookInfo.class);
	}

}

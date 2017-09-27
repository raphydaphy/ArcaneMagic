package com.raphydaphy.arcanemagic.capabilities;

import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by Xander V on 27/09/2017.
 */
public class Capabilities {

    public static EssenceStorage.DefaultStorage storage = new EssenceStorage.DefaultStorage();

    public static void register(){
        CapabilityManager.INSTANCE.register(IEssenceStorage.class, storage, EssenceStorage.class);
    }

}

package com.raphydaphy.arcanemagic.proxy;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy
{
	// i am lonely
	// no-one ever puts events in me
	// my sister, ClientProxy, is nearly full
	// and here i am, forgotten ;-;
	
	@Override
	@SuppressWarnings("deprecation")
	public String translate(String key, Object... args) {
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, args);
	}
}

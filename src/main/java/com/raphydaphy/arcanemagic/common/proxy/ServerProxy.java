package com.raphydaphy.arcanemagic.common.proxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy {

	@Override
	@SuppressWarnings("deprecation")
	public String translate(String key, Object... args) {
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, args);
	}
}

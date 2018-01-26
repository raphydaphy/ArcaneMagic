package com.raphydaphy.arcanemagic.common.proxy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public String translate(String key, Object... args)
	{
		return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, args);
	}
	
	@Override
	public void changeReachDist(EntityLivingBase entity, float additionalDist) {
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP) entity).interactionManager.setBlockReachDistance(Math.max(5, ((EntityPlayerMP) entity).interactionManager.getBlockReachDistance() + additionalDist));
	}
}

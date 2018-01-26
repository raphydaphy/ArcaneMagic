package com.raphydaphy.arcanemagic.common.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ArcaneMagicPlayerController extends PlayerControllerMP {
	private float distanceModifier = 0f;

	public ArcaneMagicPlayerController(Minecraft mcIn, NetHandlerPlayClient netHandler) {
		super(mcIn, netHandler);

		if (!(mc.playerController instanceof ArcaneMagicPlayerController)) {
			init();
		}
	}

	public void init() {
		EntityPlayer player = mc.player;
		boolean isFlying = player.capabilities.isFlying;
		boolean allowFlying = player.capabilities.allowFlying;
		this.setGameType(mc.playerController.getCurrentGameType());
		player.capabilities.isFlying = isFlying;
		player.capabilities.allowFlying = allowFlying;
		mc.playerController = this;
	}

	@Override
	public float getBlockReachDistance() {
		return super.getBlockReachDistance() + distanceModifier;
	}

	public void modifyReach(float by) {
		distanceModifier += by;
	}

	public float getExtraReach() {
		return distanceModifier;
	}
}

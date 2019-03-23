package com.raphydaphy.arcanemagic.cutscene;

import com.mojang.blaze3d.platform.GLX;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.core.client.GameRendererHooks;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.CutsceneFinishPacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Cutscene
{
	private List<Transition> transitionList = new ArrayList<>();
	private float startPitch;
	private float startYaw;
	private int ticks;
	private int duration;
	private int startPerspective;
	private boolean setShader = false;

	Cutscene(PlayerEntity player, int perspective)
	{
		duration = ((DataHolder) player).getAdditionalData().getInt(ArcaneMagicConstants.CUTSCENE_LENGTH);
		startPitch = player.pitch;
		ticks = 0;
		startYaw = player.yaw;
		this.startPerspective = perspective;
	}

	Cutscene withTransition(Transition transition)
	{
		transitionList.add(transition);
		return this;
	}

	@Environment(EnvType.CLIENT)
	void updateClient()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		if (hideHud())
		{
			if (client.options.perspective != 0)
			{
				client.options.perspective = 0;
				client.worldRenderer.method_3292();
				client.gameRenderer.onCameraEntitySet(client.getCameraEntity());


			}
			if (!setShader)
			{
				if (GLX.usePostProcess)
				{
					((GameRendererHooks) client.gameRenderer).useShader(new Identifier(ArcaneMagic.DOMAIN, "shaders/cutscene.json"));
				}
				setShader = true;
			}
		} else
		{
			client.gameRenderer.disableShader();
			setShader = false;

			if (client.options.perspective != startPerspective)
			{
				client.options.perspective = startPerspective;
				client.worldRenderer.method_3292();

				if (client.options.perspective == 0)
				{
					client.gameRenderer.onCameraEntitySet(client.getCameraEntity());
				} else
				{
					client.gameRenderer.onCameraEntitySet(null);
				}
			}
		}

		if (ticks >= duration)
		{
			ArcaneMagicPacketHandler.sendToServer(new CutsceneFinishPacket());
		} else
		{
			ticks++;
		}
	}

	void updateLook()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		ClientPlayerEntity player = client.player;

		float interpCutsceneTime = ArcaneMagicUtils.lerp(ticks, ticks + 1, client.getTickDelta());

		for (Transition transition : transitionList)
		{
			if (transition.active(interpCutsceneTime) && transition.fixedCamera)
			{
				player.yaw = startYaw;
				player.pitch = startPitch;
				return;
			}
		}
		float percent = interpCutsceneTime / (float) duration;
		player.yaw = ArcaneMagicUtils.lerp(0, 60, percent);
		player.pitch = ArcaneMagicUtils.lerp(0, 30, percent);
	}

	@Environment(EnvType.CLIENT)
	void renderTransitions()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		float interpCutsceneTime = ArcaneMagicUtils.lerp(ticks, ticks + 1, client.getTickDelta());

		for (Transition transition : transitionList)
		{
			if (transition.active(interpCutsceneTime))
			{
				transition.render(client, interpCutsceneTime);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	boolean hideHud()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		float interpCutsceneTime = ArcaneMagicUtils.lerp(ticks, ticks + 1, client.getTickDelta());

		for (Transition transition : transitionList)
		{
			if (transition.active(interpCutsceneTime))
			{
				if (transition.showHud())
				{
					return false;
				}
			}
		}

		return true;
	}
}

package com.raphydaphy.arcanemagic.cutscene;

import com.mojang.blaze3d.platform.GLX;
import com.raphydaphy.arcanemagic.core.client.GameRendererHooks;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.network.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.network.CutsceneFinishPacket;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.DataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Cutscene
{
	private List<Transition> transitionList = new ArrayList<>();
	private CutsceneCameraEntity camera;
	private int ticks;
	private int duration;
	private boolean setCamera = false;
	private Identifier shader;
	private Path cameraPath;

	Cutscene(PlayerEntity player, Path cameraPath)
	{
		this.duration = ((DataHolder) player).getAdditionalData().getInt(ArcaneMagicConstants.CUTSCENE_LENGTH);
		this.ticks = 0;
		this.cameraPath = cameraPath.build();
		this.camera = new CutsceneCameraEntity(player.world).withPos(this.cameraPath.getPoint(0));
	}

	Cutscene withDipTo(float length, int red, int green, int blue)
	{
		withTransition(new Transition.DipTo(0, length, red, green, blue).setIntro());
		withTransition(new Transition.DipTo(duration - length, length, red, green, blue).setOutro());
		return this;
	}

	Cutscene withTransition(Transition transition)
	{
		transitionList.add(transition);
		return this;
	}

	Cutscene withShader(Identifier shader)
	{
		this.shader = shader;
		return this;
	}

	@Environment(EnvType.CLIENT)
	void updateClient()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		if (hideHud())
		{
			camera.moveTo(cameraPath.getPoint(ticks / (float)duration));
			camera.update();
			if (!setCamera)
			{
				client.cameraEntity = camera;

				if (this.shader != null)
				{
					client.worldRenderer.method_3292();
					if (GLX.usePostProcess)
					{
						((GameRendererHooks) client.gameRenderer).useShader(this.shader);
					}
				}
				setCamera = true;
			}
		} else
		{
			client.gameRenderer.disableShader();
			setCamera = false;
			client.setCameraEntity(client.player);
			client.worldRenderer.method_3292();
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

		float interpCutsceneTime = ArcaneMagicUtils.lerp(ticks, ticks + 1, client.getTickDelta());

		for (Transition transition : transitionList)
		{
			if (transition.active(interpCutsceneTime) && transition.fixedCamera)
			{
				return;
			}
		}
		float percent = interpCutsceneTime / (float) duration;

		Vector3f direction = cameraPath.getPoint(percent);
		direction.subtract(cameraPath.getPoint(percent >= 0.99f ? 0.9999f : percent + 0.01f));
		float lengthSquared = direction.x() * direction.x() + direction.y() * direction.y() + direction.z() * direction.z();
		if (lengthSquared != 0 && lengthSquared != 1) direction.scale(1 / (float)Math.sqrt(lengthSquared));

		camera.prevYaw = camera.yaw;
		camera.prevPitch = camera.pitch;

		//camera.yaw = (float)Math.atan2(distance.z(), distance.x());
		//camera.pitch = (float)(Math.atan2(Math.sqrt(distance.z() * distance.z() + distance.x() * distance.x()), distance.y()) + Math.PI);

		camera.pitch = (float)Math.toDegrees(Math.asin(direction.y()));
		camera.yaw = (float)Math.toDegrees(Math.atan2(direction.x(), direction.z()));

		System.out.println(direction.y() + ", " + camera.pitch);
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

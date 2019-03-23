package com.raphydaphy.arcanemagic.cutscene;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.client.MinecraftClient;

public abstract class Transition
{
	float startTime;
	float length;
	boolean showHud = false;
	boolean fixedCamera = false;

	Transition(float startTime, float length)
	{
		this.startTime = startTime;
		this.length = length;
	}

	abstract void render(MinecraftClient client, float cutsceneTime);

	public boolean active(float cutsceneTime)
	{
		return cutsceneTime >= startTime && cutsceneTime <= startTime + length;
	}

	boolean showHud()
	{
		return showHud;
	}

	boolean fixedCamera()
	{
		return fixedCamera;
	}

	public static class FadeTo extends Transition
	{
		protected float red;
		protected float green;
		protected float blue;

		FadeTo(float startTime, float length, int red, int green, int blue)
		{
			super(startTime, length);
			this.red = red / 255f;
			this.green = green / 255f;
			this.blue = blue / 255f;
		}

		@Override
		public void render(MinecraftClient client, float cutsceneTime)
		{
			float transitionTime = cutsceneTime - startTime;
			float percent = 1 - transitionTime / length;
			RenderUtils.drawRect(0, 0, client.window.getScaledWidth(), client.window.getScaledHeight(), percent, red, green, blue);
		}
	}

	public static class DipTo extends FadeTo
	{
		private boolean isIntro = false;
		private boolean isOutro = false;

		DipTo(float startTime, float length, int red, int green, int blue)
		{
			super(startTime, length, red, green, blue);
		}

		public DipTo setIntro()
		{
			this.isIntro = true;
			return this;
		}

		public DipTo setOutro()
		{
			this.isOutro = true;
			return this;
		}

		@Override
		public boolean active(float cutsceneTime)
		{
			if (isIntro)
			{
				return cutsceneTime <= startTime + length;
			} else if (isOutro)
			{
				return cutsceneTime >= startTime;
			}
			return cutsceneTime >= startTime && cutsceneTime <= startTime + length;
		}

		@Override
		public void render(MinecraftClient client, float cutsceneTime)
		{
			float transitionTime = cutsceneTime - startTime;
			float halfTime = length / 2f;
			GlStateManager.disableDepthTest();
			if (transitionTime < halfTime)
			{
				showHud = isIntro;
				fixedCamera = isIntro;
				float percent = transitionTime / halfTime;
				RenderUtils.drawRect(0, 0, client.window.getScaledWidth(), client.window.getScaledHeight(), percent, red, green, blue);
			} else
			{
				transitionTime = transitionTime - halfTime;
				showHud = isOutro;
				fixedCamera = isOutro;
				float percent = 1 - transitionTime / halfTime;
				RenderUtils.drawRect(0, 0, client.window.getScaledWidth(), client.window.getScaledHeight(), percent, red, green, blue);
			}
			GlStateManager.enableDepthTest();
		}
	}
}

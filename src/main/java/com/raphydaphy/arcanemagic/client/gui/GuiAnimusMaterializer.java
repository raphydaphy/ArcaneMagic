package com.raphydaphy.arcanemagic.client.gui;

import java.util.Map;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.container.ContainerAnimusMaterializer;
import com.raphydaphy.arcanemagic.common.data.EnumBasicAnimus;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAnimusMaterializer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiAnimusMaterializer extends GuiContainer
{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 181;
	private TileEntityAnimusMaterializer te;
	private static final ResourceLocation background = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/animus_materializer.png");

	public GuiAnimusMaterializer(TileEntityAnimusMaterializer tileEntity, ContainerAnimusMaterializer container)
	{
		super(container);
		te = tileEntity;
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@SideOnly(Side.CLIENT)
	private void drawBar(int x, int y, float r, float g, float b, int essence, float rotation)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.rotate(rotation, 0, 0, 1);
		GlStateManager.color(1, 1, 1);
		GlStateManager.clearColor(1, 1, 1, 1);

		Minecraft.getMinecraft().getTextureManager().bindTexture(background);

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		drawModalRectWithCustomSizedTexture(x, y, 177, 2, 12, 60, 196, 181);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 0.2f);
		drawModalRectWithCustomSizedTexture(x + 2, y + 6, 179, 63, 8, 48, 196, 181);
		drawTexturedModalRect(x + 1, y + 4, 104, 0, 8, 29);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, 1f);
		drawModalRectWithCustomSizedTexture(x + 2, y + 6 + (48 - essence), 179, 63, 8, essence, 196, 181);
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		super.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(background);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 196, 181);
		for (int x = 0; x < 2; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				drawModalRectWithCustomSizedTexture(guiLeft + 13 + (x * 20), guiTop + 20 + (y * 20), 176, 113, 18, 18,
						196, 181);
			}
		}

		Map<Anima, AnimaStack> essenceStored = te.getCapability(IAnimaStorage.CAP, null).getStored();
		int i = 0;
		for (EnumBasicAnimus e : EnumBasicAnimus.values())
		{
			Anima essence = e.getAnima();
			i++;
			AnimaStack stack = essenceStored.containsKey(essence) ? essenceStored.get(essence)
					: new AnimaStack(essence, 0);
			drawBar(guiLeft + 40 + (i * 18), guiTop + 19, essence.getColor().getRed() / 256f,
					essence.getColor().getGreen() / 256f, essence.getColor().getBlue() / 256f,
					(int) Math.floor(stack.getCount() / 20.4f), 0);
		}

		String title = I18n.format("gui.arcanemagic.animus_materializer");
		mc.fontRenderer.drawString(title, (float) (guiLeft + 85 - mc.fontRenderer.getStringWidth(title) / 2),
				guiTop + 7, 4210752, false);
		this.fontRenderer.drawString(I18n.format("container.inventory"), guiLeft + 8, guiTop + 87, 4210752);
	}
}
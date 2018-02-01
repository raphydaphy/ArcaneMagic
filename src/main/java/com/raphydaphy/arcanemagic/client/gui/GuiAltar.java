package com.raphydaphy.arcanemagic.client.gui;

import java.awt.Color;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.client.render.GLHelper;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.container.ContainerInfusionAltar;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityAltar;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiAltar extends GuiContainer
{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 203;
	private TileEntityAltar te;
	private long age;
	private Color rainbow;
	private static final ResourceLocation background = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/writing_desk.png");

	public GuiAltar(TileEntityAltar tileEntity, ContainerInfusionAltar container)
	{
		super(container);
		te = tileEntity;
		xSize = WIDTH;
		ySize = HEIGHT;
		age = 0;
		rainbow = new Color(0, 0, 0);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		super.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		age += 1;
		float frequency = 0.3f;
		double r = Math.sin(frequency * age) * 127 + 128;
		double g = Math.sin(frequency * age + 2) * 127 + 128;
		double b = Math.sin(frequency * age + 4) * 127 + 128;

		rainbow = new Color((int) r, (int) g, (int) b);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		ScaledResolution res = new ScaledResolution(mc);
		mc.getTextureManager().bindTexture(background);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 250, 203);
		fontRenderer.drawString(I18n.format("gui.arcanemagic.altar"),
				(guiLeft + 103 - fontRenderer.getStringWidth(I18n.format("gui.arcanemagic.altar")) / 2), guiTop + 9,
				4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory"), guiLeft + 8, guiTop + 106, 4210752);

		IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (handler != null)
		{
			ItemStack book = handler.getStackInSlot(0);
			ItemStack paper = handler.getStackInSlot(1);

			boolean drawWarning = false;
			String text = "";

			if (book.isEmpty())
			{
				text = I18n.format("gui.arcanemagic.altar.nobook");
				drawWarning = true;
			} else if (paper.isEmpty())
			{
				text = I18n.format("gui.arcanemagic.altar.nopaper");
				drawWarning = true;
			}

			if (drawWarning)
			{
				this.drawHoveringText(text, (guiLeft + 92 - fontRenderer.getStringWidth(text) / 2), guiTop + 68);
			} else
			{
				GLHelper.drawCircle2D(34, guiLeft + 102, guiTop + 60, Color.black, 1, res.getScaleFactor() * 1);

				//GLHelper.drawTriangle2D(59, 45, guiLeft + 126, guiTop + 36, Color.black, res.getScaleFactor());
				//GLHelper.drawTriangle2D(59, -45 - 90, guiLeft + 78, guiTop + 84, Color.black, res.getScaleFactor());

				GlStateManager.pushMatrix();

				// CREATION
				GLHelper.renderFancyBeam2D(guiLeft + 101, guiTop + 60, 45, rainbow, mc.world.getSeed(), age, 0.2f);
				GLHelper.renderFancyBeam2D(guiLeft + 101, guiTop + 60, 106, rainbow, mc.world.getSeed(), age, 0.2f);
				GLHelper.renderFancyBeam2D(guiLeft + 101, guiTop + 60, 166, rainbow, mc.world.getSeed(), age, 0.2f);
				GLHelper.renderFancyBeam2D(guiLeft + 101, guiTop + 60, 226, rainbow, mc.world.getSeed(), age, 0.2f);
				GLHelper.renderFancyBeam2D(guiLeft + 101, guiTop + 60, -17, rainbow, mc.world.getSeed(), age, 0.2f);
				GLHelper.renderFancyBeam2D(guiLeft + 101, guiTop + 60, -78, rainbow, mc.world.getSeed(), age, 0.2f);

				// CHAOS
				GLHelper.renderFancyBeams2D(guiLeft + 92, guiTop + 27, Anima.CHAOS.getColor(), mc.world.getSeed(), age,
						0.18f, 40, 30);

				// DEPTH
				GLHelper.renderFancyBeams2D(guiLeft + 111, guiTop + 92, Anima.DEPTH.getColor(), mc.world.getSeed(),
						age + 2023, 0.18f, 40, 30);

				// HORIZON
				GLHelper.renderFancyBeams2D(guiLeft + 78, guiTop + 83, Anima.HORIZON.getColor(), mc.world.getSeed(),
						age + 1123, 0.18f, 40, 30);

				// INFERNO
				GLHelper.renderFancyBeams2D(guiLeft + 126, guiTop + 36, Anima.INFERNO.getColor(), mc.world.getSeed(),
						age + 4341, 0.18f, 40, 30);

				// OZONE
				GLHelper.renderFancyBeams2D(guiLeft + 135, guiTop + 67, Anima.OZONE.getColor(), mc.world.getSeed(),
						age + 6941, 0.18f, 40, 30);

				// PEACE
				GLHelper.renderFancyBeams2D(guiLeft + 68, guiTop + 51, Anima.PEACE.getColor(), mc.world.getSeed(),
						age + 7812, 0.18f, 40, 30);

				double scale = 0.7;
				GlStateManager.scale(scale, scale, scale);
				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.CREATION.getItemForm(),
						(int) ((guiLeft + 96) * (1 / scale)), (int) ((guiTop + 54) * (1 / scale)));

				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.CHAOS.getItemForm(),
						(int) ((guiLeft + 88) * (1 / scale)), (int) ((guiTop + 21) * (1 / scale)));
				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.DEPTH.getItemForm(),
						(int) ((guiLeft + 106) * (1 / scale)), (int) ((guiTop + 87) * (1 / scale)));

				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.HORIZON.getItemForm(),
						(int) ((guiLeft + 73) * (1 / scale)), (int) ((guiTop + 78) * (1 / scale)));
				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.INFERNO.getItemForm(),
						(int) ((guiLeft + 121) * (1 / scale)), (int) ((guiTop + 30) * (1 / scale)));

				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.OZONE.getItemForm(),
						(int) ((guiLeft + 130) * (1 / scale)), (int) ((guiTop + 62.5) * (1 / scale)));
				mc.getRenderItem().renderItemAndEffectIntoGUI(Anima.PEACE.getItemForm(),
						(int) ((guiLeft + 63) * (1 / scale)), (int) ((guiTop + 46) * (1 / scale)));
				GlStateManager.popMatrix();
			}
		}
	}
}
package com.raphydaphy.arcanemagic.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.container.ContainerWritingDesk;
import com.raphydaphy.arcanemagic.tileentity.TileEntityWritingDesk;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiWritingDesk extends GuiContainer
{
	public static final int WIDTH = 176;
	public static final int HEIGHT = 203;
	private TileEntityWritingDesk te;
	private static final ResourceLocation background = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/writing_desk.png");

	public GuiWritingDesk(TileEntityWritingDesk tileEntity, ContainerWritingDesk container)
	{
		super(container);
		te = tileEntity;
		xSize = WIDTH;
		ySize = HEIGHT;
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
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT, 250, 203);
		fontRenderer.drawString(I18n.format("gui.arcanemagic.writing_desk"),
				(guiLeft + 103 - fontRenderer.getStringWidth(I18n.format("gui.arcanemagic.writing_desk")) / 2),
				guiTop + 9, 4210752);
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
				text = I18n.format("gui.arcanemagic.writing_desk.nobook");
				drawWarning = true;
			} else if (paper.isEmpty())
			{
				text = I18n.format("gui.arcanemagic.writing_desk.nopaper");
				drawWarning = true;
			}

			if (drawWarning)
			{
				this.drawHoveringText(text, (guiLeft + 92 - fontRenderer.getStringWidth(text) / 2), guiTop + 68);
			} else
			{
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();

				GlStateManager.disableTexture2D();
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.shadeModel(7425);
				GL11.glLineWidth(10F);
				Tessellator tes = Tessellator.getInstance();
				BufferBuilder vb = tes.getBuffer();
				vb.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
				double radius = 1;
				Color color = Color.GREEN;
				for (int deg = 0; deg <= 360; deg++)
				{
					vb.pos(guiLeft + 30 + Math.cos(Math.toRadians(deg)) * (radius), guiTop + 30,
							guiTop + 30 + Math.sin(Math.toRadians(deg)) * radius)
							.color(color.getRed(), color.getGreen(), color.getBlue(), 1).endVertex();
				}

				tes.draw();

				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
			}
		}
	}
}
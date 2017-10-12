package com.raphydaphy.arcanemagic.client.gui;

import java.awt.Color;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.container.ContainerWritingDesk;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.tileentity.TileEntityWritingDesk;
import com.raphydaphy.arcanemagic.util.GLHelper;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
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
		ScaledResolution res = new ScaledResolution(mc);
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
				GLHelper.drawCircle2D(34, guiLeft + 102, guiTop + 60, Color.black, 1, res.getScaleFactor() * 1);
				
				GLHelper.drawTriangle2D(59,45, guiLeft + 126, guiTop + 36, Color.black, res.getScaleFactor());
				GLHelper.drawTriangle2D(59,-45 - 90, guiLeft + 78, guiTop + 84, Color.black, res.getScaleFactor());
				
				GlStateManager.pushMatrix();
				
				// CREATION
				GLHelper.renderFancyBeams2D(guiLeft + 101.5, guiTop  + 59.5, Essence.CREATION.getColor(), mc.world.getSeed(), mc.world.getWorldTime() + 5053, 0.2f, 40, 30);
				
				// CHAOS
				GLHelper.renderFancyBeams2D(guiLeft + 92, guiTop  + 27, Essence.CHAOS.getColor(), mc.world.getSeed(), mc.world.getWorldTime(), 0.18f, 40, 30);
				
				// DEPTH
				GLHelper.renderFancyBeams2D(guiLeft + 111, guiTop  + 92, Essence.DEPTH.getColor(), mc.world.getSeed(), mc.world.getWorldTime() + 2023, 0.18f, 40, 30);
				
				// HORIZON
				GLHelper.renderFancyBeams2D(guiLeft + 78, guiTop  + 83, Essence.HORIZON.getColor(), mc.world.getSeed(), mc.world.getWorldTime() + 1123, 0.18f, 40, 30);
				
				// INFERNO
				GLHelper.renderFancyBeams2D(guiLeft + 126, guiTop  + 36, Essence.INFERNO.getColor(), mc.world.getSeed(), mc.world.getWorldTime() + 4341, 0.18f, 40, 30);
				
				// OZONE
				GLHelper.renderFancyBeams2D(guiLeft + 135, guiTop  + 67, Essence.OZONE.getColor(), mc.world.getSeed(), mc.world.getWorldTime() - 2732, 0.18f, 40, 30);
				
				// PEACE
				GLHelper.renderFancyBeams2D(guiLeft + 68, guiTop  + 51, Essence.PEACE.getColor(), mc.world.getSeed(), mc.world.getWorldTime() - 2732, 0.18f, 40, 30);
				
				double scale = 0.7;
				GlStateManager.scale(scale, scale, scale);
				mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.NETHER_STAR), (int)((guiLeft + 96) * (1 / scale)), (int)((guiTop + 54) * (1 / scale)));
				
				mc.getRenderItem().renderItemAndEffectIntoGUI(Essence.CHAOS.getItemForm(), (int)((guiLeft + 88) * (1 / scale)), (int)((guiTop + 21) * (1 / scale)));
				mc.getRenderItem().renderItemAndEffectIntoGUI(Essence.DEPTH.getItemForm(), (int)((guiLeft + 106) * (1 / scale)), (int)((guiTop + 87) * (1 / scale)));
				
				mc.getRenderItem().renderItemAndEffectIntoGUI(Essence.HORIZON.getItemForm(), (int)((guiLeft + 73) * (1 / scale)), (int)((guiTop + 78) * (1 / scale)));
				mc.getRenderItem().renderItemAndEffectIntoGUI(Essence.INFERNO.getItemForm(), (int)((guiLeft + 121) * (1 / scale)), (int)((guiTop + 30) * (1 / scale)));
				
				mc.getRenderItem().renderItemAndEffectIntoGUI(Essence.OZONE.getItemForm(), (int)((guiLeft + 130) * (1 / scale)), (int)((guiTop + 62.5) * (1 / scale)));
				mc.getRenderItem().renderItemAndEffectIntoGUI(Essence.PEACE.getItemForm(), (int)((guiLeft + 63) * (1 / scale)), (int)((guiTop + 46) * (1 / scale)));
				GlStateManager.popMatrix();
			}
		}
	}
}
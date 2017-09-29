package com.raphydaphy.arcanemagic.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.INotebookCategory;
import com.raphydaphy.arcanemagic.api.notebook.INotebookEntry;
import com.raphydaphy.arcanemagic.api.util.Pos2;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNotebookNew extends GuiScreen
{
	public static final int FRAME_WIDTH = 344;
	public static final int FRAME_HEIGHT = 230;
	public static final int FRAME_TEX_HEIGHT = 256;

	public static final String tagUsedNotebook = "usedNotebook";
	public static final String tagPageX = "notebookPageX";
	public static final String tagPageY = "notebookPageY";
	public static final String tagTab = "notebookTab";

	private EntityPlayer player;

	private int lastDragX = 0;
	private int lastDragY = 0;

	private int relMouseX = 0;
	private int relMouseY = 0;

	private static final ResourceLocation frame = new ResourceLocation(ArcaneMagic.MODID, "textures/gui/notebook.png");
	private static final ResourceLocation page = new ResourceLocation(ArcaneMagic.MODID,
			"textures/gui/thaumonomicon_page.png");

	public GuiNotebookNew(EntityPlayer player)
	{
		this.player = player;

		if (player.getEntityData().getBoolean(tagUsedNotebook) == false)
		{
			System.out.println("Player opened Notebook for first time, doing initial setup.");
			// player.getEntityData().setBoolean(tagUsedNotebook, true);
			player.getEntityData().setInteger(tagPageX, 200);
			player.getEntityData().setInteger(tagPageY, 200);
			player.getEntityData().setInteger(tagTab, 0);
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.setGuiSize(mc.displayWidth, mc.displayHeight);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0)
		{
			
		}
	}
}

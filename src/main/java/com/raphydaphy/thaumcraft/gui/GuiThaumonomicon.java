package com.raphydaphy.thaumcraft.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.handler.ThaumcraftSoundHandler;

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
public class GuiThaumonomicon extends GuiScreen
{
	public static final int WIDTH = 300;
    public static final int HEIGHT = 265;
    
    public static final String tagUsedThauminomicon = "usedThauminomicon";
    public static final String tagPageX = "thauminomiconPageX";
    public static final String tagPageY = "thauminomiconPageY";
    public static final String tagTab = "thauminomiconTab";
    
    private EntityPlayer player;
    
    private int lastDragX = 0;
    private int lastDragY = 0;
    
    private int relMouseX = 0;
    private int relMouseY = 0;
    
    private static final ResourceLocation frame = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon.png");
    private static final ResourceLocation page = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon_page.png");
    private static final ResourceLocation back = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon_back.png");
    private static final ResourceLocation back_eldritch = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon_back_eldritch.png");
    
    private static final ResourceLocation r_alchent = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_alchent.png");
    private static final ResourceLocation r_alchman = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_alchman.png");
    private static final ResourceLocation r_alchmult = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_alchmult.png");
    private static final ResourceLocation r_artifice = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_artifice.png");
    private static final ResourceLocation r_aspects = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_aspects.png");
    private static final ResourceLocation r_crucible = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_crucible.png");
    private static final ResourceLocation r_eldritch = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_eldritch.png");
    private static final ResourceLocation r_eldritchmajor = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_eldritchmajor.png");
    private static final ResourceLocation r_eldritchminor = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_eldritchminor.png");
    private static final ResourceLocation r_enchant = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_enchant.png");
    private static final ResourceLocation r_golemancy = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_golemancy.png");
    private static final ResourceLocation r_infernalfurnace = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_infernalfurnace.png");
    private static final ResourceLocation r_mask0 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_mask0.png");
    private static final ResourceLocation r_mask1 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_mask1.png");
    private static final ResourceLocation r_mask2 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_mask2.png");
    private static final ResourceLocation r_nodepreserve = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_nodepreserve.png");
    private static final ResourceLocation r_nodes1 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_nodes1.png");
    private static final ResourceLocation r_nodes2 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_nodes1.png");
    private static final ResourceLocation r_nodetap1 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_nodetap1.png");
    private static final ResourceLocation r_nodetap2 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_nodetap2.png");
    private static final ResourceLocation r_outer = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_outer.png");
    private static final ResourceLocation r_outerrev = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_outerenv.png");
    private static final ResourceLocation r_pech = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_pech.png");
    private static final ResourceLocation r_resdupe = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_resdupe.png");
    private static final ResourceLocation r_researcher1 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_researcher1.png");
    private static final ResourceLocation r_researcher2 = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_researcher2.png");
    private static final ResourceLocation r_runicupg = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_runicupg.png");
    private static final ResourceLocation r_thaumaturgy = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_thaumaturgy.png");
    private static final ResourceLocation r_warp = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_warp.png");
    
    private static final ResourceLocation[] tabs = new ResourceLocation[] { new ResourceLocation(Thaumcraft.MODID, "textures/items/thaumonomicon.png"), r_thaumaturgy, r_crucible, r_artifice, r_golemancy, r_eldritch };
	private static final int totalTabs = 6;
	
    public GuiThaumonomicon(EntityPlayer player)
    {
    	this.player = player;
    	
    	if (player.getEntityData().getBoolean(tagUsedThauminomicon) == false)
    	{
    		System.out.println("Player opened Thauminomicon for first time, doing initial setup.");
    		//player.getEntityData().setBoolean(tagUsedThauminomicon, true);
    		player.getEntityData().setInteger(tagPageX, 0);
    		player.getEntityData().setInteger(tagPageY, 0);
    		player.getEntityData().setInteger(tagTab, 0);
    	}
    }
    
    @Override
    public void initGui()
    {
    	super.initGui();
    	this.setGuiSize(WIDTH, HEIGHT);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	super.drawScreen(mouseX, mouseY, partialTicks);
    	
    	ScaledResolution res = new ScaledResolution(mc);
    	
    	relMouseX = mouseX;
    	relMouseY = mouseY;
    	
    	float screenX = (res.getScaledWidth() / 2) - (255 / 2);
    	float screenY = (res.getScaledHeight() / 2) - (229 / 2);
    	
    	GlStateManager.pushMatrix();
    	GlStateManager.translate((res.getScaledWidth() /2) - (255 / 2), (res.getScaledHeight() / 2) - (229 / 2), 0);
    	
    	if (player.getEntityData().getInteger(tagTab) != 5)
    	{
    		mc.getTextureManager().bindTexture(back);
    	}
    	else
    	{
    		mc.getTextureManager().bindTexture(back_eldritch);
    	}
    	GlStateManager.scale(2, 2, 2);
        drawTexturedModalRect(8, 8.5f, (int)player.getEntityData().getFloat(tagPageX), (int)player.getEntityData().getFloat(tagPageY), 112, 98);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        
    	mc.getTextureManager().bindTexture(frame);
    	
    	
    	for (int tab = 0; tab < totalTabs * 2; tab++)
    	{
    		int thisTab = tab >= totalTabs ? tab - 6 : tab;
    		if (thisTab == player.getEntityData().getInteger(tagTab))
    		{
    			if (tab < totalTabs)
    			{
    				drawTexturedModalRect(-24, tab*23, 152, 232, 24, 24);
    			}
    			else
    			{
    				drawIcon(tabs[thisTab], -19, thisTab*23 + 4);
    			}
    		}
    		else
    		{
    			if (tab < totalTabs)
    			{
    				drawTexturedModalRect(-16, tab * 23, 184, 232, 16, 22);
    			}
    			else
    			{
    				drawIcon(tabs[thisTab], -12, thisTab*23 + 3);
    			}
    		}
    	}
    	
    	drawResearchIcon(r_aspects, "circle", 105, 105);
    	
    	drawResearchIcon(new ResourceLocation(Thaumcraft.MODID, "textures/items/thaumonomicon.png"), "circle", 140, 50);
    	drawResearchIcon(new ResourceLocation(Thaumcraft.MODID, "textures/items/scribing_tools.png"), "circle", 170, 105);
    	
    	mc.getTextureManager().bindTexture(frame);
    	drawTexturedModalRect(0, 0, 0, 0, 255, 229);
    	
    	
    	GlStateManager.popMatrix();
    	
    	GlStateManager.pushMatrix();
    	if (relMouseX >= screenX - 24 && relMouseY >= screenY && relMouseX <= screenX && relMouseY <= screenY + (totalTabs * 23))
    	{
			for (int tab = 0; tab < totalTabs; tab++)
			{
				int tabWidth = 16;
				
				if (tab == player.getEntityData().getInteger(tagTab))
				{
					tabWidth = 24;
				}
        		if (relMouseY >= screenY + (tab * 23) && relMouseY <= screenY + (tab * 23) + 23 && relMouseX >= screenX - tabWidth)
        		{
    				this.fontRenderer.drawString(I18n.format("thaumcraft.research.category." + tab), mouseX + 1, mouseY - 7, 0xFFFFFF);
    				break;
    			}
    		}
    	}
    	
    	if (relMouseX >= screenX + 105 && relMouseY >= screenY + 105 && relMouseX <= screenX + 105 + 22 && relMouseY <= screenY + 105 + 22)
    	{
    		drawHoveringText(ItemStack.EMPTY, Lists.newArrayList("Aspects of Magic", "The building blocks of magic", "Long tooltips get wrapped around to fit nicely if you want them to tell a long story."), Lists.newArrayList(0xFFFFFF, 0x9090ff, 0x7F0E83), mouseX, mouseY, mc.displayWidth, mc.displayHeight, 200, fontRenderer);
    	}
    	
    	GlStateManager.popMatrix();
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    private void drawHoveringText(@Nonnull final ItemStack stack, List<String> textLines, List<Integer> textColors, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font)
    {
    	if (!textLines.isEmpty())
        {
            RenderTooltipEvent.Pre event = new RenderTooltipEvent.Pre(stack, textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }
            mouseX = event.getX();
            mouseY = event.getY();
            screenWidth = event.getScreenWidth();
            screenHeight = event.getScreenHeight();
            maxTextWidth = event.getMaxWidth();
            font = event.getFontRenderer();

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines)
            {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth)
                {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth)
            {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2)
                    {
                        tooltipTextWidth = mouseX - 12 - 8;
                    }
                    else
                    {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
            {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap)
            {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<String>();
                List<Integer> wrappedTextColors = new ArrayList<Integer>();
                for (int i = 0; i < textLines.size(); i++)
                {
                    String textLine = textLines.get(i);
                    int textColor = textColors.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0)
                    {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine)
                    {
                    	int widthMultiplier = 1;
                    	if (i != 0)
                    	{
                    		widthMultiplier = 2;
                    	}
                    	int lineWidth = font.getStringWidth(line) / widthMultiplier;
                        if (lineWidth > wrappedTooltipWidth)
                        {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                        wrappedTextColors.add(textColors.get(i));
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;
                textColors = wrappedTextColors;
                if (mouseX > screenWidth / 2)
                {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                }
                else
                {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1)
            {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 0.5; // gap between title lines and next lines
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight)
            {
                tooltipY = screenHeight - tooltipHeight - 6;
            }

            final int zLevel = 300;
            final int backgroundColor = 0xF0100010;
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            final int borderColorStart = 0x505000FF;
            final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, textLines, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));
            int tooltipTop = tooltipY;

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
            {
                String line = textLines.get(lineNumber);
                int color = textColors.get(lineNumber);
                int sizeMultiplier = 1;
                if (lineNumber != 0)
                {
                	sizeMultiplier = 2;
                	GlStateManager.scale(0.5, 0.5, 0.5);
                }
                font.drawStringWithShadow(line, (float)tooltipX * sizeMultiplier, (float)tooltipY * sizeMultiplier, color);
                GlStateManager.scale(sizeMultiplier, sizeMultiplier, sizeMultiplier);
                if (lineNumber + 1 == titleLinesCount)
                {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, textLines, tooltipX, tooltipTop, font, tooltipTextWidth, tooltipHeight));

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    private void drawIcon(ResourceLocation icon, float x, float y)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(icon);
        GlStateManager.scale(0.065, 0.065, 1);
        drawTexturedModalRect((int)((1 / 0.065) * x), (int)((1 / 0.065) * y), 0, 0,256, 256);
        GlStateManager.scale((1 / 0.625), (1 / 0.625), 1);
        GlStateManager.popMatrix();
    }
    
    public void drawResearchIcon(ResourceLocation icon, String type, int x, int y)
    {
    	mc.getTextureManager().bindTexture(frame);
    	drawTexturedModalRect(x, y, 56, 232, 22, 22);
    	drawIcon(icon, x+2.3f, y+2.3f);
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
    	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    	
    	GlStateManager.pushMatrix();
    	if (clickedMouseButton == 0)
    	{
        	ScaledResolution res = new ScaledResolution(mc);
        	
        	float screenX = (res.getScaledWidth() / 2) - (255 / 2);
        	float screenY = (res.getScaledHeight() / 2) - (229 / 2);
        	
        	if (relMouseX >= screenX + 16 && relMouseY >= screenY + 17 && relMouseX <= screenX + 240 && relMouseY <= screenY + 210)
        	{
	        	float thisDragDistX = (lastDragX - (mouseX - (res.getScaledWidth() / 2) - (255 / 2))) * 0.7f;
	        	float thisDragDistY = (lastDragY - (mouseY - (res.getScaledHeight() / 2) - (229 / 2))) * 0.45f;
	        	
	        	float totalDragDistX = player.getEntityData().getFloat(tagPageX) + thisDragDistX;
	        	float totalDragDistY = player.getEntityData().getFloat(tagPageY) + thisDragDistY;
	        	
	        	if (totalDragDistX >= 0 && totalDragDistX <= 146)
	        	{
		    		player.getEntityData().setFloat(tagPageX, totalDragDistX);
	        	}
	        	
	        	if (totalDragDistY >= 0 && totalDragDistY <= 160)
	        	{
	        		player.getEntityData().setFloat(tagPageY, totalDragDistY);
	        	}
        	}
        	
    		lastDragX = mouseX - (res.getScaledWidth() / 2) - (255 / 2);
    		lastDragY = mouseY - (res.getScaledHeight() / 2) - (229 / 2);
    	}
    	GlStateManager.popMatrix();
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	
    	if (mouseButton == 0)
    	{
    		ScaledResolution res = new ScaledResolution(mc);
    		
    		lastDragX = mouseX - (res.getScaledWidth() / 2) - (255 / 2);
    		lastDragY = mouseY - (res.getScaledHeight() / 2) - (229 / 2);
    		
    		float screenX = (res.getScaledWidth() / 2) - (255 / 2);
        	float screenY = (res.getScaledHeight() / 2) - (229 / 2);
        	
    		if (relMouseX >= screenX - 16 && relMouseY >= screenY && relMouseX <= screenX && relMouseY <= screenY + (totalTabs * 23))
        	{
    			for (int tab = 0; tab < totalTabs; tab++)
    			{
	        		if (relMouseY >= screenY + (tab * 23) && relMouseY <= screenY + (tab * 23) + 23)
	        		{
	        			if (player.getEntityData().getInteger(tagTab) != tab)
	        			{
	        				player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, ThaumcraftSoundHandler.randomCameraClackSound(), SoundCategory.MASTER, 1f, 1f, false);
	        				player.getEntityData().setInteger(tagTab, tab);
	        				break;
	        			}
	        		}
    			}
        	}
    	}
    }
}

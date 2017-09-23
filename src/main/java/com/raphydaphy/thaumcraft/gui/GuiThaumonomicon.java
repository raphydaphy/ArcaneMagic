package com.raphydaphy.thaumcraft.gui;

import java.io.IOException;

import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.handler.ThaumcraftSoundHandler;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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
    private static final ResourceLocation r_aspects = new ResourceLocation(Thaumcraft.MODID, "textures/misc/research/r_aspects_.png");
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
        
    	mc.getTextureManager().bindTexture(frame);
    	drawTexturedModalRect(0, 0, 0, 0, 255, 229);
       
    	if (relMouseX >= screenX - 24 && relMouseY >= screenY && relMouseX <= screenX && relMouseY <= screenY + (totalTabs * 23))
    	{
    		for (int tab = totalTabs - 1; tab > 0-1; tab--)
    		{
    			if (relMouseY >= (tab + 1)*23 && player.getEntityData().getInteger(tagTab) != tab)
    			{
    				this.fontRenderer.drawString(I18n.format("thaumcraft.research.category." + tab), relMouseX, relMouseY, 0xFFFFFF);
    				break;
    			}
    		}
    	}
    	
        GlStateManager.popMatrix();
    }
    
    private void drawIcon(ResourceLocation icon, int x, int y)
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
        	
        	System.out.println(" MX: " + relMouseX + " MY: " + relMouseY + " SX: " + res.getScaledWidth() + " SY: " + res.getScaledHeight() +" F: " + res.getScaleFactor());
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
        	
    		if (relMouseX >= screenX - 24 && relMouseY >= screenY && relMouseX <= screenX && relMouseY <= screenY + (totalTabs * 23))
        	{
        		for (int tab = totalTabs - 1; tab > 0-1; tab--)
        		{
        			if (relMouseY <= (tab + 1)*23 && player.getEntityData().getInteger(tagTab) != tab)
        			{
        				player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, ThaumcraftSoundHandler.randomCameraClackSound(), SoundCategory.MASTER, 1f, 1f, false);
        				player.getEntityData().setInteger(tagTab, tab);
        			}
        		}
        	}
    	}
    }
}

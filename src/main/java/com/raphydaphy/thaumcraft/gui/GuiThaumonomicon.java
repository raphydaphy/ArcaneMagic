package com.raphydaphy.thaumcraft.gui;

import java.io.IOException;

import com.raphydaphy.thaumcraft.Thaumcraft;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
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
    
    private EntityPlayer player;
    
    private int lastDragX = 0;
    private int lastDragY = 0;
    
    private static final ResourceLocation frame = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon.png");
    private static final ResourceLocation page = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon_page.png");
    private static final ResourceLocation back = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon_back.png");
    private static final ResourceLocation back_eldritch = new ResourceLocation(Thaumcraft.MODID, "textures/gui/thaumonomicon_back_eldritch.png");
    
    public GuiThaumonomicon(EntityPlayer player)
    {
    	this.player = player;
    	
    	if (player.getEntityData().getBoolean(tagUsedThauminomicon) == false)
    	{
    		System.out.println("Player opened Thauminomicon for first time, doing initial setup.");
    		//player.getEntityData().setBoolean(tagUsedThauminomicon, true);
    		player.getEntityData().setInteger(tagPageX, 0);
    		player.getEntityData().setInteger(tagPageY, 0);
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
    	GlStateManager.translate((res.getScaledWidth() /2) - (255 / 2), (res.getScaledHeight() / 2) - (229 / 2), 0);
    	
    	mc.getTextureManager().bindTexture(back);
    	GlStateManager.scale(2, 2, 2);
        drawTexturedModalRect(8, 8.5f, (int)player.getEntityData().getFloat(tagPageX), (int)player.getEntityData().getFloat(tagPageY), 112, 98);
        
    	mc.getTextureManager().bindTexture(frame);
    	GlStateManager.scale(0.5f, 0.5f, 0.5f);
        drawTexturedModalRect(0, 0, 0, 0, 255, 229);
        
        
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
    	super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    	
    	if (clickedMouseButton == 0)
    	{
        	ScaledResolution res = new ScaledResolution(mc);
        	
        	float bgStartX = (res.getScaledWidth() / 2) - ((255 + 30) / 2);
        	float bgStartY = (res.getScaledHeight() / 2) - ((229 - 28) / 2);
        	
        	if (mouseX >= bgStartX && mouseY >= bgStartY && mouseX <= bgStartX + 157 && mouseY <= bgStartY + 179)
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
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	
    	if (mouseButton == 0)
    	{
    		ScaledResolution res = new ScaledResolution(mc);
    		
    		lastDragX = mouseX - (res.getScaledWidth() / 2) - (255 / 2);
    		lastDragY = mouseY - (res.getScaledHeight() / 2) - (229 / 2);
    	}
    }
}

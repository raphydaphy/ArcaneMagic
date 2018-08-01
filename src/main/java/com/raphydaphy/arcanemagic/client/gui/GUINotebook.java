package com.raphydaphy.arcanemagic.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

// TODO: sideonly client
public class GUINotebook extends GuiScreen
{
    private EntityPlayer player;

    public GUINotebook(EntityPlayer player)
    {
        this.player = player;
    }

    @Override
    protected void initGui()
    {
        super.initGui();
        System.out.println("init gui");
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_)
    {

    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton))
        {
            if (mouseButton == 0)
            {
                System.out.println("left click");
            }

            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int modifier)
    {
        if (!super.keyPressed(keyCode, p_keyPressed_2_, modifier))
        {
            System.out.println("key pressed " + keyCode + ", " + p_keyPressed_2_ + ", " + modifier);
            return false;
        }
        return true;
    }
}

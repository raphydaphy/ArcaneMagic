package com.raphydaphy.arcanemagic.client.gui;

import com.raphydaphy.arcanemagic.network.PacketAncientParchment;
import com.raphydaphy.arcanemagic.api.IParchment;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
// TODO: sideonly client
public class GuiParchment extends GuiScreen
{
    // x and y size of the parchment texture
    public static final int DIMENSIONS = 64;
    public static final int TEX_HEIGHT = 69;

    public static final int PROGRESS_BAR_LENGTH = 48;
    public static final int FULL_PROGRESS = PROGRESS_BAR_LENGTH - 2;

    public static final float SCALE = 3;
    public static final int SCALED_DIMENSIONS = (int) (DIMENSIONS * SCALE);

    ResourceLocation PARCHMENT = new ResourceLocation(ArcaneMagicResources.MOD_ID, "textures/gui/parchment.png");

    private ItemStack stack;
    private IParchment parchment;

    public GuiParchment(ItemStack stack, IParchment parchment)
    {
        this.stack = stack;
        this.parchment = parchment;
        parchment.setParchmentStack(stack);
    }

    @Override
    protected void initGui()
    {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        mc.getTextureManager().bindTexture(PARCHMENT);

        // The start x and y coords of the notebook on the screen
        int screenX = (mc.mainWindow.getScaledWidth() / 2) - (SCALED_DIMENSIONS / 2);
        int screenY = (mc.mainWindow.getScaledHeight() / 2) - (SCALED_DIMENSIONS / 2);
        
        drawBackground(screenX, screenY);
        
        drawText(mc, parchment.getText(), screenY + 32 * SCALE);

        int percent = (int)((FULL_PROGRESS / 4.0f) * parchment.getPercent());

        if (parchment.getRecipe() != null) drawRecipe(parchment.getRecipe(), screenX, screenY);

        if (parchment.showProgressBar()) drawProgressBar(percent, screenX, screenY);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
    
    private void drawBackground(int screenX, int screenY)
    {
        GuiScreen.drawScaledCustomSizeModalRect(screenX, screenY, 0, 0, DIMENSIONS, DIMENSIONS,
                SCALED_DIMENSIONS, SCALED_DIMENSIONS, DIMENSIONS, TEX_HEIGHT);
    }

    private void drawText(Minecraft mc, String unlocalizedText, float center)
    {
        ArcaneMagicUtils.drawCenteredSplitString(mc.fontRenderer, I18n.format(unlocalizedText), mc.mainWindow.getScaledWidth() / 2f, (int)(center), 160,0);
    }

    private void drawProgressBar(int progress, int screenX, int screenY)
    {
        GuiScreen.drawScaledCustomSizeModalRect(
                (int) (screenX + 8 * SCALE), (int) (screenY + 54 * SCALE), 0, DIMENSIONS,
                PROGRESS_BAR_LENGTH, 5, (int) (PROGRESS_BAR_LENGTH * SCALE), (int) ((5) * SCALE), DIMENSIONS, TEX_HEIGHT);

        if (progress > 0)
        {
            GuiScreen.drawScaledCustomSizeModalRect(
                    (int) (screenX + 9 * SCALE), (int) (screenY + 55 * SCALE), PROGRESS_BAR_LENGTH, DIMENSIONS + 1,
                    1, 3, (int) (progress * SCALE), (int) ((3) * SCALE), DIMENSIONS, TEX_HEIGHT);
        }
    }

    private void drawRecipe(IRecipe recipe, int screenX, int screenY)
    {

        int x = screenX + 31;
        int y = (int) (screenY + 37 * SCALE);
        
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        ItemStack output = recipe.getRecipeOutput();

        RecipeType type = getRecipeType(recipe);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        mc.getTextureManager().bindTexture(PARCHMENT);

        //draw the input
        switch(type) {
            case LARGE_CRAFTING:
                GuiScreen.drawScaledCustomSizeModalRect(x + 20, y - 3, 0, DIMENSIONS, 1, 1, 2, 73, DIMENSIONS,
                        TEX_HEIGHT);
                GuiScreen.drawScaledCustomSizeModalRect(x - 3, y + 20, 0, DIMENSIONS, 1, 1, 73, 2, DIMENSIONS,
                        TEX_HEIGHT);
                GuiScreen.drawScaledCustomSizeModalRect(x + 45, y - 3, 0, DIMENSIONS, 1, 1, 2, 73, DIMENSIONS,
                        TEX_HEIGHT);
                GuiScreen.drawScaledCustomSizeModalRect(x - 3, y + 45, 0, DIMENSIONS, 1, 1, 73, 2, DIMENSIONS,
                        TEX_HEIGHT);

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                RenderHelper.enableGUIStandardItemLighting();

                for (int inputX = 0; inputX < 3; inputX++)
                {
                    for (int inputY = 0; inputY < 3; inputY++)
                    {
                        ItemStack[] ingredient = ingredients.get((3*inputX)+(inputY+1)).getMatchingStacks();
                        if (!ingredient[0].equals(ItemStack.EMPTY))
                        {
                            // Render the recipe component
                            mc.getRenderItem().renderItemAndEffectIntoGUI(ingredient[0], x + (inputX * 25),
                                    y + (inputY * 25));

                        }
                    }
                }
                break;
            default:
                break;
        }

        RenderHelper.disableStandardItemLighting();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        mc.getTextureManager().bindTexture(PARCHMENT);

        // Draw the crafting arrow
        GuiScreen.drawScaledCustomSizeModalRect(x + 78, y + 26, 49,64, 7, 5, (22), (15), DIMENSIONS,
                TEX_HEIGHT);

        // Draw the crafting output box
        GuiScreen.drawScaledCustomSizeModalRect(x + 108, y + 21, 0, DIMENSIONS, 1, 1, 26, 2, DIMENSIONS,
                TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 108, y + 45, 0, DIMENSIONS, 1, 1, 26, 2, DIMENSIONS,
                TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 108, y + 21, 0, DIMENSIONS, 1, 1, 2, 25, DIMENSIONS,
                TEX_HEIGHT);
        GuiScreen.drawScaledCustomSizeModalRect(x + 132, y + 21, 0, DIMENSIONS, 1, 1, 2, 25, DIMENSIONS,
                TEX_HEIGHT);

        // Draw the output item
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(output, x + 113, y + 26);
        RenderHelper.disableStandardItemLighting();


    }

    private RecipeType getRecipeType(IRecipe recipe) {
        if (recipe instanceof FurnaceRecipe) return RecipeType.FURNACE;

//        if (recipe instanceof AnimaRecipe) return RecipeType.ANIMA;

        if (recipe.canFit(2, 2)) return RecipeType.SMALL_CRAFTING;

        if (recipe.canFit(3, 3)) return RecipeType.LARGE_CRAFTING;

        else return RecipeType.UNSUPPORTED;
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        if (parchment.isAncient())
        {
           NetHandlerPlayClient connection = Minecraft.getMinecraft().getConnection();
           if (connection != null)
           {
               connection.sendPacket(new PacketAncientParchment());
           }
        }
    }
}

package com.raphydaphy.arcanemagic.client.render;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityWritingDesk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class WritingDeskTESR extends TileEntitySpecialRenderer<TileEntityWritingDesk>
{
	@Override
	public void render(TileEntityWritingDesk te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableRescaleNormal();
		IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		GlStateManager.translate(0, 0, 0);
		
		renderPapers(cap.getStackInSlot(0), 0, te);

		for (int i = 0; i < 6; i++)
		{
			//renderItems(cap.getStackInSlot(i + 1), i + 1, te);
		}
		GlStateManager.popMatrix();
	}
	
	private void renderItems(ItemStack stack, int slot, TileEntityWritingDesk te)
	{
		if (stack != null && !stack.isEmpty())
		{
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, .62, 0.5);
			GlStateManager.scale(.1f, .1f, .1f);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}

	private void renderPapers(ItemStack stack, int slot, TileEntityWritingDesk te)
	{
		if (stack != null && !stack.isEmpty())
		{
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.75, .8, 0.5);
			GlStateManager.scale(.5f, .5f, .5f);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			Minecraft.getMinecraft().getTextureManager()
					.bindTexture(new ResourceLocation(ArcaneMagic.MODID, "textures/misc/parchment.png"));
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			//GlStateManager.rotate(te.getAge(), 0,1,0);
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos(0, 0, -0.4).tex(0.0D, 1.0D).endVertex();
			bufferbuilder.pos(0, 0, 0.4).tex(1.0D, 1.0D).endVertex();
			bufferbuilder.pos(0, 0.8, 0.4).tex(1.0D, 0.0D).endVertex();
			bufferbuilder.pos(0, 0.8, -0.4).tex(0.0D, 0.0D).endVertex();
			tessellator.draw();

			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.translate(0, -0.59, 0);
			GlStateManager.translate(0, 0, -0.0005);

			for (int side = 0; side < 2; side++)
			{
				if (side == 1)
				{
					GlStateManager.translate(0, 0, 0.001);
					GlStateManager.rotate(180, 0,1,0);
					//GlStateManager.rotate(180,0,0,1);
					
				}
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.scale(0.008, 0.008, 0.008);
	
				GLHelper.drawCenteredSplitString(Minecraft.getMinecraft().fontRenderer,
						"Manipulation Magic", 0, -15, 100,
						0x000000);
	
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
				
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				GlStateManager.scale(0.005, 0.005, 0.005);
				GLHelper.drawCenteredSplitString(Minecraft.getMinecraft().fontRenderer,
						"Manipulating Essence directly would be a powerful ability, if only you could obtain the right items to learn how.\n\nLuckily, your new discoveries should aid you greatly in this process.", 0, 0, 130,
						0x000000);
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
		}
	}
}

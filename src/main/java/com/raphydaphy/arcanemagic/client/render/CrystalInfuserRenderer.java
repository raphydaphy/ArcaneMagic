package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;

public class CrystalInfuserRenderer extends BlockEntityRenderer<CrystalInfuserBlockEntity>
{
	public void render(CrystalInfuserBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		if (entity != null)
		{
			float ticks = ArcaneMagicUtils.lerp(entity.ticks - 1, entity.ticks, partialTicks);

			ItemStack equipment = entity.getInvStack(0);
			ItemStack binder = entity.getInvStack(1);
			ItemStack crystal = entity.getInvStack(2);

			GlStateManager.pushMatrix();
			GuiLighting.enable();
			GlStateManager.enableLighting();
			GlStateManager.disableRescaleNormal();



			// Render Equipment
			if (!equipment.isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translated(renderX + .5, renderY + 1 + Math.sin((Math.PI / 180) * (ticks* 4)) / 15, renderZ + .5);
				GlStateManager.rotated(2 * ticks, 0, 1, 0);
				GlStateManager.scaled(0.8, 0.8, 0.8);
				MinecraftClient.getInstance().getItemRenderer().renderItem(equipment, ModelTransformation.Type.GROUND);

				GlStateManager.popMatrix();
			}

			ticks += 3941;

			// Render Binder
			if (!binder.isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translated(renderX + .5 + Math.cos((Math.PI/180) * (ticks * 2)) / 3, renderY + 1 - Math.sin((Math.PI / 180) * (ticks* 4)) / 8, renderZ + .5 + Math.sin((Math.PI/180) * (ticks * 2)) / 3);
				GlStateManager.rotated(2 * ticks, 0, 1, 0);
				GlStateManager.scaled(0.6, 0.6, 0.6);
				MinecraftClient.getInstance().getItemRenderer().renderItem(binder, ModelTransformation.Type.GROUND);
				GlStateManager.popMatrix();

				ticks -= 3;
				ParticleUtil.spawnGlowParticle(entity.getWorld(),
						(float)(entity.getPos().getX() + .5 + Math.cos((Math.PI/180) * (ticks * 2)) / 3),
						(float)(entity.getPos().getY() + 1 - Math.sin((Math.PI / 180) * (ticks* 4)) / 8),
						(float)(entity.getPos().getZ() + .5 + Math.sin((Math.PI/180) * (ticks * 2)) / 3),
						0, 0, 0, 1, 0, 0, 0.1f, 0.1f, 150);
				ticks += 3;
			}

			ticks += 90;

			// Render Crystal
			if (!crystal.isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translated(renderX + .5 + Math.cos((Math.PI/180) * (ticks * 2)) / 3, renderY + 1 - Math.sin((Math.PI / 180) * ((ticks + 45)* 4)) / 8, renderZ + .5 + Math.sin((Math.PI/180) * (ticks * 2)) / 3);
				GlStateManager.rotated(2 * ticks, 0, 1, 0);
				GlStateManager.scaled(0.6, 0.6, 0.6);
				MinecraftClient.getInstance().getItemRenderer().renderItem(crystal, ModelTransformation.Type.GROUND);
				GlStateManager.popMatrix();

				ticks -= 3;
				ParticleUtil.spawnGlowParticle(entity.getWorld(),
						(float)(entity.getPos().getX()  + .5 + Math.cos((Math.PI/180) * (ticks * 2)) / 3),
						(float)(entity.getPos().getY() + 1 - Math.sin((Math.PI / 180) * ((ticks + 45)* 4)) / 8),
						(float)(entity.getPos().getZ() + .5 + Math.sin((Math.PI/180) * (ticks * 2)) / 3),
						0, 0, 0, 1, 0.5f, 0, 0.1f, 0.1f, 150);
			}

			GlStateManager.popMatrix();

		}
	}
}

package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.client.particle.ParticleUtil;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.sun.javafx.geom.Vec3f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Vec3d;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import paulscode.sound.Vector3D;

public class CrystalInfuserRenderer extends BlockEntityRenderer<CrystalInfuserBlockEntity>
{
	public void render(CrystalInfuserBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		if (entity != null)
		{
			float ticks = ArcaneMagicUtils.lerp(entity.ticksExisted - 1, entity.ticksExisted, partialTicks);

			ItemStack equipment = entity.getInvStack(0);
			ItemStack binder = entity.getInvStack(1);
			ItemStack crystal = entity.getInvStack(2);

			GlStateManager.pushMatrix();
			GuiLighting.enable();
			GlStateManager.enableLighting();
			GlStateManager.disableRescaleNormal();

			boolean using = entity.isActive();
			long craftingTime = entity.getCraftingTime();

			// Render Equipment
			if (!equipment.isEmpty())
			{
				GlStateManager.pushMatrix();

				if (using)
				{
					GlStateManager.translated(renderX + .5, renderY + 1 + Math.sin((Math.PI / 180) * (ticks * 4)) / 15, renderZ + .5);
					GlStateManager.rotated(2 * ticks, 0, 1, 0);
				} else
				{
					GlStateManager.translated(renderX + .5, renderY + .635, renderZ + .5);
					GlStateManager.rotated(90, 1, 0, 0);

					if (equipment.getItem() instanceof ArmorItem && ((ArmorItem)equipment.getItem()).getSlotType() == EquipmentSlot.HEAD)
					{
						GlStateManager.translated(0, -0.07, 0);
					}
					GlStateManager.translated(0, -.08, 0);
				}
				GlStateManager.scaled(0.8, 0.8, 0.8);
				MinecraftClient.getInstance().getItemRenderer().renderItem(equipment, ModelTransformation.Type.GROUND);

				GlStateManager.popMatrix();
			}

			ticks += 3941;

			// Render Binder
			if (!binder.isEmpty())
			{
				GlStateManager.pushMatrix();

				if (using)
				{
					GlStateManager.translated(renderX + .5 + Math.cos((Math.PI/180) * (ticks * 2)) / 3, renderY + 1 - Math.sin((Math.PI / 180) * (ticks* 4)) / 8, renderZ + .5 + Math.sin((Math.PI/180) * (ticks * 2)) / 3);
					GlStateManager.rotated(2 * ticks, 0, 1, 0);
					ParticleUtil.spawnGlowParticle(entity.getWorld(),
							(float)(entity.getPos().getX() + .5 + Math.cos((Math.PI/180) * (ticks * 2)) / 3),
							(float)(entity.getPos().getY() + 1 - Math.sin((Math.PI / 180) * (ticks* 4)) / 8),
							(float)(entity.getPos().getZ() + .5 + Math.sin((Math.PI/180) * (ticks * 2)) / 3),
							0, 0, 0, 1, 0, 0, 0.1f, 0.1f, 150);
				} else
				{
					GlStateManager.translated(renderX + .35, renderY + .635, renderZ + .3);

					if (binder.getItem() == Items.LAPIS_LAZULI)
					{
						GlStateManager.rotated(90, 1, 0, 0);
						GlStateManager.rotated(90, 0, 0, 1);
						if (!equipment.isEmpty())
						{
							GlStateManager.rotated(10, 1, 1, 0);
						}
						GlStateManager.translated(.07, -.1, 0);
					} else if (binder.getItem() == Items.REDSTONE)
					{
						GlStateManager.rotated(90, 1, 0, 0);

						if (!equipment.isEmpty())
						{
							GlStateManager.rotated(-10, 1, 0, 0);
							GlStateManager.rotated(10, 0, 1, 0);
						}

					}
				}
				GlStateManager.scaled(0.6, 0.6, 0.6);
				MinecraftClient.getInstance().getItemRenderer().renderItem(binder, ModelTransformation.Type.GROUND);
				GlStateManager.popMatrix();


			}

			ticks += 90;

			// Render Crystal
			if (!crystal.isEmpty())
			{
				GlStateManager.pushMatrix();

				if (using)
				{
					float inverseRadius = (craftingTime) / 1000f; // 0.5
					inverseRadius = 3;
					System.out.println(inverseRadius);
					Vec3f pos = new Vec3f(
							(float)(0.5 + Math.cos((Math.PI/180) * (ticks * 2)) / inverseRadius),
							(float)(1 - Math.sin((Math.PI / 180) * ((ticks + 45)* 4)) / 8),
							(float)(0.5 + Math.sin((Math.PI/180) * (ticks * 2)) / inverseRadius));
					GlStateManager.translated(renderX + pos.x, renderY + pos.y, renderZ + pos.z);
					GlStateManager.rotated(2 * ticks, 0, 1, 0);
					ParticleUtil.spawnGlowParticle(entity.getWorld(),entity.getPos().getX() + pos.x, entity.getPos().getY() + pos.y, entity.getPos().getZ() + pos.z,
							0, 0, 0, 1, 0.5f, 0, 0.1f, 0.1f, 150);
				} else
				{
					GlStateManager.translated(renderX + .69, renderY + .635, renderZ + .6);
					GlStateManager.rotated(90, 1, 0, 0);
					GlStateManager.rotated(50, 0, 0, 1);

					if (!equipment.isEmpty())
					{
						GlStateManager.translated(0, 0, -.01);
						GlStateManager.rotated(10, 0, -1, 0);
					}
				}

				GlStateManager.scaled(0.6, 0.6, 0.6);
				MinecraftClient.getInstance().getItemRenderer().renderItem(crystal, ModelTransformation.Type.GROUND);
				GlStateManager.popMatrix();


			}

			GlStateManager.popMatrix();

		}
	}
}
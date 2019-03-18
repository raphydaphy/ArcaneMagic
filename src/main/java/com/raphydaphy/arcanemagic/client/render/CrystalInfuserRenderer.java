package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.block.base.OrientableBlockBase;
import com.raphydaphy.arcanemagic.block.entity.CrystalInfuserBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import me.shedaniel.rei.client.ClientHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

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

			float craftingTime = entity.getCraftingTime();
			boolean active = entity.isActive() && craftingTime > 10;

			if (active && craftingTime > 8000 && craftingTime > 8150)
			{
				//ParticleUtil.spawnGlowParticle(entity.getWorld(),entity.getPos().getX() + .5f, entity.getPos().getY() + 1.1f, entity.getPos().getZ() + .5f,0, 0, 0, 1, 1, 1, 0.1f, 0,0.5f, 100);
			} else
			{

				GlStateManager.pushMatrix();
				GuiLighting.enable();
				GlStateManager.enableLighting();
				GlStateManager.disableRescaleNormal();

				craftingTime /= 2f;

				float scale = 0.5f;
				if (active && craftingTime > 7500)
				{
					scale = scale - ((craftingTime - 7500) / 500f) * scale;
				}

				GlStateManager.translated(renderX, renderY, renderZ);
				if (!active)
				{
					Direction dir = getWorld().getBlockState(entity.getPos()).get(OrientableBlockBase.FACING);
					RenderUtils.rotateTo(dir);
				}

				// Render Equipment
				if (!equipment.isEmpty())
				{
					GlStateManager.pushMatrix();

					if (active)
					{
						GlStateManager.translated(.5, 1 + Math.sin((Math.PI / 180) * (ticks * 4)) / 15, .5);
						GlStateManager.rotated(2 * ticks, 0, 1, 0);
					} else
					{
						GlStateManager.translated(.5, .635, .5);

						GlStateManager.rotated(90, 1, 0, 0);
						if (equipment.getItem() instanceof ArmorItem && ((ArmorItem) equipment.getItem()).getSlotType() == EquipmentSlot.HEAD)
						{
							GlStateManager.translated(0, -0.07, 0);
						}
						GlStateManager.translated(0, -.08, 0);
					}
					GlStateManager.scaled(0.8, 0.8, 0.8);
					MinecraftClient.getInstance().getItemRenderer().renderItem(equipment, ModelTransformation.Type.GROUND);

					GlStateManager.popMatrix();
				}

				ticks += 400;

				scale = 0.6f;
				if (active && craftingTime > 3750)
				{
					scale = scale - ((craftingTime - 3750) / 500f) * scale;
				}

				// Render Binder
				if (!binder.isEmpty())
				{
					GlStateManager.pushMatrix();

					if (active)
					{
						float inverseRadius = (craftingTime) / 1000f + 3;
						float posX = (float) (.5 + Math.cos((Math.PI / 180) * (ticks * 2)) / inverseRadius);
						float posY = (float) (1 - Math.sin((Math.PI / 180) * (ticks * 4)) / 8);
						float posZ = (float) (.5 + Math.sin((Math.PI / 180) * (ticks * 2)) / inverseRadius);
						GlStateManager.translated(posX, posY, posZ);
						GlStateManager.rotated(2 * ticks, 0, 1, 0);
					} else
					{
						GlStateManager.translated(.35, .635, .3);
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

					GlStateManager.scaled(scale, scale, scale);
					MinecraftClient.getInstance().getItemRenderer().renderItem(binder, ModelTransformation.Type.GROUND);
					GlStateManager.popMatrix();


				}

				ticks += 90;

				// Render Crystal
				if (!crystal.isEmpty())
				{
					GlStateManager.pushMatrix();

					if (active)
					{
						float inverseRadius = (craftingTime) / 1000f + 3;
						float posX = (float) (0.5 + Math.cos((Math.PI / 180) * (ticks * 2)) / inverseRadius);
						float posY = (float) (1 - Math.sin((Math.PI / 180) * ((ticks + 45) * 4)) / 8);
						float posZ = (float) (0.5 + Math.sin((Math.PI / 180) * (ticks * 2)) / inverseRadius);
						GlStateManager.translated(posX, posY, posZ);
						GlStateManager.rotated(2 * ticks, 0, 1, 0);
					} else
					{
						GlStateManager.translated(.69, .635, .6);
						GlStateManager.rotated(90, 1, 0, 0);
						GlStateManager.rotated(50, 0, 0, 1);

						if (!equipment.isEmpty())
						{
							GlStateManager.translated(0, 0, -.01);
							GlStateManager.rotated(10, 0, -1, 0);
						}
					}

					GlStateManager.scaled(scale, scale, scale);
					MinecraftClient.getInstance().getItemRenderer().renderItem(crystal, ModelTransformation.Type.GROUND);
					GlStateManager.popMatrix();
				}

				GlStateManager.popMatrix();
			}

		}
	}
}

package com.raphydaphy.arcanemagic.item;

import com.google.common.collect.Multimap;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DaggerItem extends SwordItem implements ICrystalEquipment
{
	private final float speed;

	public DaggerItem(ToolMaterial material, int damage, float speed)
	{
		super(material, damage, speed, new Item.Settings().itemGroup(ArcaneMagic.GROUP).stackSize(1));
		this.speed = speed;
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public static UUID getDamageModifier()
	{
		return Item.MODIFIER_DAMAGE;
	}

	public static UUID getSpeedModifier()
	{
		return Item.MODIFIER_SWING_SPEED;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void buildTooltip(ItemStack stack, World world, List<TextComponent> lines, TooltipContext ctx)
	{
		if (stack.getTag() != null)
		{
			boolean changed = false;
			ArcaneMagicUtils.ForgeCrystal passive = null;
			ArcaneMagicUtils.ForgeCrystal active = null;

			if (stack.getTag().containsKey(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY))
			{
				changed = true;
				active = ArcaneMagicUtils.ForgeCrystal.getFromID(stack.getTag().getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				lines.add(new StringTextComponent("Active Crystal: " + stack.getTag().getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY)));
			}
			if (stack.getTag().containsKey(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY))
			{
				changed = true;
				passive = ArcaneMagicUtils.ForgeCrystal.getFromID(stack.getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));
				lines.add(new StringTextComponent("Passive Crystal: " + stack.getTag().getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY)));
			}

			if (changed)
			{
				PlayerEntity player = MinecraftClient.getInstance().player;
				Multimap<String, EntityAttributeModifier> modifiers = stack.getAttributeModifiers(EquipmentSlot.HAND_MAIN);
				if (!modifiers.isEmpty())
				{
					lines.add(new StringTextComponent(""));
					lines.add(new TranslatableTextComponent("item.modifiers.mainhand").setStyle(new Style().setColor(TextFormat.GRAY)));
					for (Map.Entry<String, EntityAttributeModifier> modifier : modifiers.entries())
					{

						EntityAttributeModifier entityAttributeModifier_1 = modifier.getValue();
						double double_1 = entityAttributeModifier_1.getAmount();
						boolean boolean_1 = false;
						if (player != null)
						{
							if (entityAttributeModifier_1.getId() == Item.MODIFIER_DAMAGE)
							{
								System.out.println("damageee boy");
								double_1 += player.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getBaseValue();
								double_1 += (double) EnchantmentHelper.getAttackDamage(stack, EntityGroup.DEFAULT);
								boolean_1 = true;
							} else if (entityAttributeModifier_1.getId() == Item.MODIFIER_SWING_SPEED)
							{
								double_1 += player.getAttributeInstance(EntityAttributes.ATTACK_SPEED).getBaseValue();
								boolean_1 = true;
							}
						}

						double double_3;
						if (entityAttributeModifier_1.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier_1.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
						{
							double_3 = double_1;
						} else
						{
							double_3 = double_1 * 100.0D;
						}

						if (boolean_1)
						{
							lines.add((new StringTextComponent(" ")).append(new TranslatableTextComponent("attribute.modifier.equals." + entityAttributeModifier_1.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(double_3), new TranslatableTextComponent("attribute.name." + modifier.getKey()))).applyFormat(TextFormat.DARK_GREEN));
						} else if (double_1 > 0.0D)
						{
							lines.add((new TranslatableTextComponent("attribute.modifier.plus." + entityAttributeModifier_1.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(double_3), new TranslatableTextComponent("attribute.name." + modifier.getKey()))).applyFormat(TextFormat.BLUE));
						} else if (double_1 < 0.0D)
						{
							double_3 *= -1.0D;
							lines.add((new TranslatableTextComponent("attribute.modifier.take." + entityAttributeModifier_1.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(double_3), new TranslatableTextComponent("attribute.name." + modifier.getKey()))).applyFormat(TextFormat.RED));
						}
					}

				}

				//lines.add(new TranslatableTextComponent("attribute.name.generic.attackDamage").setStyle(new Style().setColor(TextFormat.DARK_GREEN)));
			}
		}
	}
}

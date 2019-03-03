package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
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
		CompoundTag tag = stack.getTag();
		if (tag != null)
		{
			if (tag.containsKey(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY))
			{
				lines.add(new StringTextComponent("Active Crystal: " + tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY)));
			}
			if (tag.containsKey(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY))
			{
				lines.add(new StringTextComponent("Passive Crystal: " + tag.getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY)));
			}

		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		CompoundTag tag = stack.getTag();
		if (player.isSneaking() && tag != null && tag.containsKey(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY))
		{
			if (tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY) <= 0)
			{
				int time = 10 * 20;
				ArcaneMagicUtils.ForgeCrystal crystal = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				if (crystal == ArcaneMagicUtils.ForgeCrystal.GOLD)
				{
					time = 3 * 20;
				}
				tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, time);
				if (world.isClient)
				{
					world.playSound(player, player.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYER, 1, 1);
				}
				return new TypedActionResult<>(ActionResult.SUCCESS, stack);
			}
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public void onEntityTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		CompoundTag tag = stack.getTag();
		if (tag != null)
		{
			int timer = tag.getInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY);
			if (timer > 0)
			{
				tag.putInt(ArcaneMagicConstants.ACTIVE_TIMER_KEY, timer - 1);
				if (entity instanceof PlayerEntity)
				{
					if (!world.isClient)
					{
						((PlayerEntity) entity).addChatMessage(new StringTextComponent((timer / 20) + "s Remaining"), true);
					} else if (timer - 1 == 0)
					{
						world.playSound((PlayerEntity) entity, entity.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYER, 1, 1);
					}
				}
			}
		}
	}
}

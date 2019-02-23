package com.raphydaphy.arcanemagic.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial
{
	EMERALD_CRYSTAL("emerald_crystal", 20, new int[]{2, 5, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1F, () ->
	{
		return Ingredient.ofItems(ModRegistry.EMERALD_CRYSTAL);
	});

	private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final Lazy<Ingredient> repairIngredientSupplier;

	ModArmorMaterials(String name, int durabilityMultiplier, int[] protection, int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairIngredient)
	{
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protection;
		this.enchantability = enchantability;
		this.equipSound = sound;
		this.toughness = toughness;
		this.repairIngredientSupplier = new Lazy<>(repairIngredient);
	}

	public int getDurability(EquipmentSlot equipmentSlot_1)
	{
		return BASE_DURABILITY[equipmentSlot_1.getEntitySlotId()] * this.durabilityMultiplier;
	}

	public int getProtectionAmount(EquipmentSlot equipmentSlot_1)
	{
		return this.protectionAmounts[equipmentSlot_1.getEntitySlotId()];
	}

	public int getEnchantability()
	{
		return this.enchantability;
	}

	public SoundEvent getEquipSound()
	{
		return this.equipSound;
	}

	public Ingredient getRepairIngredient()
	{
		return this.repairIngredientSupplier.get();
	}

	@Environment(EnvType.CLIENT)
	public String getName()
	{
		return this.name;
	}

	public float getToughness()
	{
		return this.toughness;
	}
}

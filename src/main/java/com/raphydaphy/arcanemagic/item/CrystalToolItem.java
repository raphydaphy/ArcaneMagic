package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class CrystalToolItem extends ToolItem implements ICrystalEquipment
{
	public CrystalToolItem(ToolMaterial material)
	{
		super(material, new Item.Settings().stackSize(1).itemGroup(ArcaneMagic.GROUP));
	}

	private enum ModToolMaterials implements ToolMaterial
	{
		EMERALD_CRYSTAL(2, 500, 5.0F, 2.5F, 15, () -> {
			return Ingredient.ofItems(ModRegistry.EMERALD_CRYSTAL);
		});

		private final int miningLevel;
		private final int durability;
		private final float blockBreakSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final Lazy<Ingredient> repairIngredient;

		ModToolMaterials(int miningLevel, int durability, float blockBreakSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.durability = durability;
			this.blockBreakSpeed = blockBreakSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairIngredient = new Lazy<>(repairIngredient);
		}

		public int getDurability() {
			return this.durability;
		}

		public float getBlockBreakingSpeed() {
			return this.blockBreakSpeed;
		}

		public float getAttackDamage() {
			return this.attackDamage;
		}

		public int getMiningLevel() {
			return this.miningLevel;
		}

		public int getEnchantability() {
			return this.enchantability;
		}

		public Ingredient getRepairIngredient() {
			return (Ingredient)this.repairIngredient.get();
		}
	}

}


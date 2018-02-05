package com.raphydaphy.arcanemagic.common.item;

import com.google.common.collect.Multimap;
import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.client.render.ModelArcaneDagger;
import com.raphydaphy.arcanemagic.common.util.IHasRecipe;
import com.raphydaphy.arcanemagic.common.util.RecipeHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArcaneDagger extends ItemBase implements IHasRecipe {
	private final float attackDamage = 2.5f;
	private final float attackSpeed = 0.5f;

	public ItemArcaneDagger() {
		super("arcane_dagger", TextFormatting.GRAY);
		this.setMaxDamage(128);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		if ((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
			stack.damageItem(2, entityLiving);
		}

		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return blockIn.getBlock() == Blocks.WEB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initModels(ModelRegistryEvent e) {
		super.initModels(e);
		ModelLoaderRegistry.registerLoader(ModelArcaneDagger.LoaderArcaneDagger.instance);
	}

	@Override
	public int getItemEnchantability() {
		return 33;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		@SuppressWarnings("deprecation")
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) this.attackSpeed, 0));
		}

		return multimap;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (isSelected) {

		}
	}

	@Override
	public void initRecipes(Register<IRecipe> e) {
		RecipeHelper.addElementalShaped(this, Anima.CREATION, 30, null, null, "gemDiamond", null, Items.IRON_SWORD,
				null, "stickWood", null, null);
	}
}

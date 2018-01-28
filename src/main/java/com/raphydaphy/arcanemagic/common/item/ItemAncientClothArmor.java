package com.raphydaphy.arcanemagic.common.item;

import java.util.Locale;

import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAncientClothArmor extends ItemArmor implements IHasModel
{
	public static final ArmorMaterial ArmorMaterialAncientCloth = EnumHelper.addArmorMaterial((ArcaneMagic.MODID+"_armorAncientCloth").toUpperCase(Locale.ROOT), ArcaneMagic.MODID+":armor_ancient_cloth", /*durability*/ 60, /*what are reduction amounts?!??!*/ new int[]{6, 9, 9, 4}, /*enchantability*/ 30, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);
	public static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
	
	public ItemAncientClothArmor(String name, int armorType) {
		super(ArmorMaterialAncientCloth, armorType, VALID_EQUIPMENT_SLOTS[armorType]);
		setRegistryName(name);
		setUnlocalizedName(ArcaneMagic.MODID + "." + name);
		setCreativeTab(ArcaneMagic.creativeTab);
		ModRegistry.ITEMS.add(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack)
	{
		return TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName(stack) + ".name").trim();
	}
	
	@Override
	public void initModels(ModelRegistryEvent e)
	{
		IHasModel.sMRL("items", this, 0, "item=" + getRegistryName().getResourcePath());
	}
	
	@Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack){
        return itemToRepair.getItem().equals((Items.LEATHER));
    }
}

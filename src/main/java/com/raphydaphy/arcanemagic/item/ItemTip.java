package com.raphydaphy.arcanemagic.item;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPartItem;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.data.IPropertyEnum;
import com.raphydaphy.arcanemagic.item.ItemTip.Type;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;

/**
 * Created by Xander V on 29/09/2017.
 */
public class ItemTip extends ItemEnum<Type> implements ScepterPartItem
{
	public ItemTip()
	{
		super("sceptre_tip", Type.values());
		ScepterRegistry.registerAll(Type.IRON, Type.GOLD, Type.THAUMIUM);
	}

	@Override
	public ScepterPart getPartFromItemStack(ItemStack in)
	{
		Preconditions.checkArgument(in.getItem() == this, in.getItem().getRegistryName().toString());
		return ItemTip.Type.values()[in.getMetadata()];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.arcanemagic.tip." + values[stack.getMetadata()].getName();
	}
	
	@Override
	public void initModels(ModelRegistryEvent ev) {
		for (Type t : values)
			IHasModel.sMRL("items", this, t.ordinal(), "item=tip_" + t.getName());
	}

	public enum Type implements ScepterPart, IPropertyEnum
	{
		IRON, GOLD, THAUMIUM,;

		private final String UNLOC_NAME;
		private final ResourceLocation TEXTURE;
		private final ResourceLocation REGNAME;

		Type()
		{
			String name = getName();
			UNLOC_NAME = ArcaneMagic.MODID + ".tip." + name;
			REGNAME = new ResourceLocation(ArcaneMagic.MODID, name);
			TEXTURE = new ResourceLocation(ArcaneMagic.MODID, "items/scepter/tip_" + name);
		}

		@Override
		public String getUnlocalizedName()
		{
			return "part." + UNLOC_NAME;
		}

		@Override
		public ResourceLocation getTexture()
		{
			return TEXTURE;
		}

		@Override
		public ResourceLocation getRegistryName()
		{
			return REGNAME;
		}

		@Override
		public PartCategory getType()
		{
			return PartCategory.TIP;
		}
	}
}

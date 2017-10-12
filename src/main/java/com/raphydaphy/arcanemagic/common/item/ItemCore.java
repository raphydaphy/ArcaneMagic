package com.raphydaphy.arcanemagic.common.item;

import com.google.common.base.Preconditions;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPartItem;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.client.IHasModel;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.data.IPropertyEnum;
import com.raphydaphy.arcanemagic.common.item.ItemCore.Type;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;

/**
 * Created by Xander V on 29/09/2017.
 */

public class ItemCore extends ItemEnum<Type> implements ScepterPartItem
{
	public ItemCore()
	{
		super("sceptre_core", Type.values());
		ScepterRegistry.registerAll(Type.WOOD, Type.GREATWOOD, Type.SILVERWOOD);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item.arcanemagic.core." + values[stack.getMetadata()].getName();
	}

	@Override
	public void initModels(ModelRegistryEvent ev)
	{
		for (Type t : values)
			IHasModel.sMRL("items", this, t.ordinal(), "item=core_" + t.getName());
	}

	@Override
	public ScepterPart getPartFromItemStack(ItemStack in)
	{
		Preconditions.checkArgument(in.getItem() == this, in.getItem().getRegistryName().toString());
		return ItemCore.Type.values()[in.getMetadata()];
	}

	public enum Type implements ScepterPart, IPropertyEnum
	{
		WOOD, SILVERWOOD, GREATWOOD,;

		private final String UNLOC_NAME;
		private final ResourceLocation TEXTURE;
		private final ResourceLocation REGNAME;

		Type()
		{
			String name = getName();
			UNLOC_NAME = ArcaneMagic.MODID + ".core." + name;
			REGNAME = new ResourceLocation(ArcaneMagic.MODID, name);
			TEXTURE = new ResourceLocation(ArcaneMagic.MODID, "items/scepter/core_" + name);
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
			return PartCategory.CORE;
		}
	}
}

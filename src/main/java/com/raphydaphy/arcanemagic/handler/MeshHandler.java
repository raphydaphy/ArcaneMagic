package com.raphydaphy.arcanemagic.handler;

import com.raphydaphy.arcanemagic.item.ItemBase;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class MeshHandler implements ItemMeshDefinition
{
	private static MeshHandler instance;

	public MeshHandler()
	{
	}

	public static MeshHandler instance()
	{
		if (instance == null)
		{
			instance = new MeshHandler();
		}

		return instance;
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBase)
		{
			return ((ItemBase)stack.getItem()).getModelLocation(stack);
		}

		return null;
	}
}
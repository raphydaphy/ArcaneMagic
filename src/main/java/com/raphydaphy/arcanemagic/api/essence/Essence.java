package com.raphydaphy.arcanemagic.api.essence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ModRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Essence extends IForgeRegistryEntry.Impl<Essence>
{

	public static final IForgeRegistry<Essence> REGISTRY = new EssenceRegistry();

	public static final Essence INFERNO = new Essence("inferno", ArcaneMagic.MODID + ".inferno", 0xa5500b);
	public static final Essence DEPTH = new Essence("depth", ArcaneMagic.MODID + ".depth", 0x187ea3);
	public static final Essence OZONE = new Essence("ozone", ArcaneMagic.MODID + ".ozone", 0xeaeaea);
	public static final Essence HORIZON = new Essence("horizon", ArcaneMagic.MODID + ".horizon", 0x066018);
	public static final Essence PEACE = new Essence("peace", ArcaneMagic.MODID + ".peace", 0x303a1f);
	public static final Essence CHAOS = new Essence("chaos", ArcaneMagic.MODID + ".chaos", 0x85b72f);

	public static class EssenceSubscriber
	{

		@SubscribeEvent
		public void onEssenceRegister(Register<Essence> event)
		{
			event.getRegistry().registerAll(INFERNO, DEPTH, OZONE, HORIZON, PEACE, CHAOS);
			INFERNO.setItemForm(new ItemStack(ModRegistry.ESSENCE));
			DEPTH.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 1));
			OZONE.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 2));
			HORIZON.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 3));
			PEACE.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 4));
			CHAOS.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 5));
		}
	}

	private final int color;
	private String unlocName;
	protected ItemStack itemForm = ItemStack.EMPTY;

	private Essence(String name, String unlocName, int color)
	{
		setRegistryName(ArcaneMagic.MODID, name);
		setUnlocalizedName(unlocName);
		this.color = color;
	}

	public Essence(String unlocName, int color)
	{
		setUnlocalizedName(unlocName);
		this.color = color;
	}

	public Essence(int color)
	{
		this.color = color;
	}

	public Essence setUnlocalizedName(String unloc)
	{
		this.unlocName = unloc;
		return this;
	}

	public Essence setItemForm(ItemStack stack)
	{
		if (itemForm.isEmpty())
			itemForm = stack;
		return this;
	}

	public ItemStack getItemForm()
	{
		return itemForm.copy();
	}

	public String getUnlocalizedName()
	{
		return "essence." + unlocName;
	}

	public String getTranslationName()
	{
		return getUnlocalizedName() + ".name";
	}

	public int getColor()
	{
		return color;
	}

	public String toString()
	{
		return this.getClass().getSimpleName() + ":" + getRegistryName();
	}

	private static final String E = "essence_tag";

	@Nullable
	public static List<EssenceStack> readFromNBT(NBTTagCompound tag)
	{
		if (!tag.hasKey(E))
			return null;
		List<EssenceStack> ret = new ArrayList<>();
		NBTTagCompound essTag = tag.getCompoundTag(E);
		for (String s : essTag.getKeySet())
		{
			ret.add(new EssenceStack(REGISTRY.getValue(new ResourceLocation(s)), essTag.getInteger(s)));
		}
		return ret;

	}
	
	public static Map<Essence, EssenceStack> buildMapFromNBT(NBTTagCompound tag){
		List<EssenceStack> stacks = readFromNBT(tag);
		Map<Essence, EssenceStack> map = new HashMap<>();
		for(EssenceStack stack : stacks) 
			map.put(stack.getEssence(), stack);
		return map;
	}

	public static NBTTagCompound writeToNBT(NBTTagCompound tag, EssenceStack... essences)
	{
		NBTTagCompound essTag = tag.getCompoundTag(E);
		for (EssenceStack e : essences)
		{
			essTag.setInteger(e.getEssence().getRegistryName().toString(), e.getCount());
		}
		return tag;
	}

}
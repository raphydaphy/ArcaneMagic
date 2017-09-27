package com.raphydaphy.arcanemagic.api.essence;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class EssenceStack implements INBTSerializable<NBTTagCompound>
{

	public static final EssenceStack EMPTY = new EssenceStack(null, 0);

	private Essence essence;
	private int count;

	public EssenceStack(Essence e, int size)
	{
		essence = e;
		count = size;
	}

	public EssenceStack(NBTTagCompound tag){
		essence = null;//dummy value
		count = 0;
		this.deserializeNBT(tag);
	}

	@Override
	public String toString() {
		return String.format(count + " x " + essence);
	}

	public int getCount()
	{
		return count;
	}

	public Essence getEssence()
	{
		return essence;
	}

	public EssenceStack setCount(int count)
	{
		this.count = count;
		return this;
	}

	public EssenceStack grow(int grow)
	{
		count += grow;
		return this;
	}

	public EssenceStack shrink(int shrink)
	{
		count -= shrink;
		return this;
	}

	public boolean isEmpty()
	{
		return essence == null || count <= 0;
	}

	public EssenceStack copy()
	{
		return new EssenceStack(essence, count);
	}

	public ImmutableEssenceStack toImmutable()
	{
		return new ImmutableEssenceStack(this);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("type", essence.getRegistryName().toString());
		tag.setInteger("count", count);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt != null && nbt.hasKey("type") && Essence.REGISTRY.containsKey(new ResourceLocation(nbt.getString("type"))) && nbt.hasKey("count")) {
			this.essence = Essence.REGISTRY.getValue(new ResourceLocation(nbt.getString("type")));
			this.count = nbt.getInteger("count");
		}
	}

	public static class ImmutableEssenceStack extends EssenceStack
	{

		public ImmutableEssenceStack(EssenceStack e)
		{
			super(e.getEssence(), e.getCount());
		}

		public EssenceStack setCount(int count)
		{
			throw new UnsupportedOperationException("This essence stack is immutable!");
		}

		public EssenceStack grow(int grow)
		{
			throw new UnsupportedOperationException("This essence stack is immutable!");
		}

		public EssenceStack shrink(int shrink)
		{
			throw new UnsupportedOperationException("This essence stack is immutable!");
		}

	}
}
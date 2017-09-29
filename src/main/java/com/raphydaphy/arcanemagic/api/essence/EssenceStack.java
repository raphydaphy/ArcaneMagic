package com.raphydaphy.arcanemagic.api.essence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class EssenceStack
{
	private static final String E = "essence_tag";
	public static final EssenceStack EMPTY = new EssenceStack(null, 0);

	private Essence essence;
	private int count;

	public EssenceStack(Essence e, int size)
	{
		essence = e;
		count = size;
	}

	@Override
	public String toString()
	{
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
	
	public static NBTTagCompound resetEssence(NBTTagCompound tag)
	{
		List<EssenceStack> stacks = readFromNBT(tag);
		for (EssenceStack curStack : stacks)
		{
			curStack.setCount(1);
		}
		return tag;
	}

	public static void writeDefaultEssence(Object tileOrItem)
	{
		Preconditions.checkArgument(tileOrItem instanceof TileEntity || tileOrItem instanceof ItemStack);
		if (tileOrItem instanceof TileEntity)
		{
			TileEntity tile = (TileEntity) tileOrItem;
			NBTTagCompound tag = tile.getTileData();
			writeDefaultEssence(tag);
			tile.markDirty();

		} else if (tileOrItem instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) tileOrItem;
			NBTTagCompound tag = stack.getTagCompound();
			writeDefaultEssence(tag);
			stack.setTagCompound(tag);
		}
	}

	public static NBTTagCompound writeDefaultEssence(NBTTagCompound tag)
	{
		if (tag == null)
			tag = new NBTTagCompound();
		Collection<EssenceStack> col = new ArrayList<>();
		for (Essence e : Essence.REGISTRY)
			col.add(new EssenceStack(e, 0));
		return writeToNBT(tag, col);
	}
	
	public static void writeToBuf(ByteBuf to, EssenceStack stack)
	{
		if (stack.isEmpty())
        {
			to.writeByte(-1);
            to.writeShort(-1);
        }
        else
        {
        	to.writeByte(stack.getCount());
        	to.writeShort(Essence.REGISTRY.getValues().indexOf(stack.getEssence()));
        }
	}
	
	public static EssenceStack readFromBuf(ByteBuf from)
	{
		int count = from.readByte();
		int id = from.readShort();
        if (count < 0)
        {
            return EssenceStack.EMPTY;
        }
        else
        {
            
            EssenceStack stack = new EssenceStack(Essence.getEssenceByID(id), count);
            
            return stack;
        }
	}
	
	@Nonnull
	public static List<EssenceStack> readFromNBT(NBTTagCompound tag)
	{
		if (tag != null)
		{
			List<EssenceStack> ret = new ArrayList<>();
			NBTTagCompound essTag = tag.getCompoundTag(E);
			for (String s : essTag.getKeySet())
				ret.add(new EssenceStack(Essence.REGISTRY.getValue(new ResourceLocation(s)), essTag.getInteger(s)));

			return ret;
		} else
		{
			return readFromNBT(writeDefaultEssence(new NBTTagCompound()));
		}
	}

	public static Map<Essence, EssenceStack> buildMapFromNBT(NBTTagCompound tag)
	{
		List<EssenceStack> stacks = readFromNBT(tag);
		Map<Essence, EssenceStack> map = new HashMap<>();
		for (EssenceStack stack : stacks)
			map.put(stack.getEssence(), stack);

		return map;
	}

	public static NBTTagCompound writeToNBT(NBTTagCompound tag, EssenceStack... essences)
	{
		NBTTagCompound essTag = tag.getCompoundTag(E);
		for (EssenceStack e : essences)
		{

			if (e.getCount() < 0)
				continue;

			e.grow(essTag.getInteger(e.getEssence().getRegistryName().toString()));
			essTag.setInteger(e.getEssence().getRegistryName().toString(), e.getCount());
		}
		tag.setTag(E, essTag);
		return tag;
	}

	public static NBTTagCompound writeToNBT(NBTTagCompound tag, Collection<EssenceStack> essences)
	{
		return writeToNBT(tag, essences.toArray(new EssenceStack[essences.size()]));
	}
}
package com.raphydaphy.arcanemagic.api.anima;

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

public class AnimaStack {
	private static final String E = "anima_tag";
	public static final AnimaStack EMPTY = new AnimaStack(null, 0);

	private Anima anima;
	private int count;

	public AnimaStack(Anima e, int size) {
		anima = e;
		count = size;
	}

	@Override
	public String toString() {
		return String.format(count + " x " + anima);
	}

	public int getCount() {
		return count;
	}

	public Anima getAnima() {
		return anima;
	}

	public AnimaStack setCount(int count) {
		this.count = count;
		return this;
	}

	public AnimaStack grow(int grow) {
		count += grow;
		return this;
	}

	public AnimaStack shrink(int shrink) {
		count -= shrink;
		return this;
	}

	public boolean isEmpty() {
		return anima == null || count <= 0;
	}

	public AnimaStack copy() {
		return new AnimaStack(anima, count);
	}

	public ImmutableAnimaStack toImmutable() {
		return new ImmutableAnimaStack(this);
	}

	public static class ImmutableAnimaStack extends AnimaStack {

		public ImmutableAnimaStack(AnimaStack e) {
			super(e.getAnima(), e.getCount());
		}

		@Override
		public AnimaStack setCount(int count) {
			throw new UnsupportedOperationException("This anima stack is immutable!");
		}

		@Override
		public AnimaStack grow(int grow) {
			throw new UnsupportedOperationException("This anima stack is immutable!");
		}

		@Override
		public AnimaStack shrink(int shrink) {
			throw new UnsupportedOperationException("This anima stack is immutable!");
		}

	}

	public static NBTTagCompound resetAnima(NBTTagCompound tag) {
		List<AnimaStack> stacks = readFromNBT(tag);
		for (AnimaStack curStack : stacks) {
			curStack.setCount(1);
		}
		return tag;
	}

	public static void writeDefaultAnimus(Object tileOrItem) {
		Preconditions.checkArgument(tileOrItem instanceof TileEntity || tileOrItem instanceof ItemStack);
		if (tileOrItem instanceof TileEntity) {
			TileEntity tile = (TileEntity) tileOrItem;
			NBTTagCompound tag = tile.getTileData();
			writeDefaultAnimus(tag);
			tile.markDirty();

		} else if (tileOrItem instanceof ItemStack) {
			ItemStack stack = (ItemStack) tileOrItem;
			NBTTagCompound tag = stack.getTagCompound();
			writeDefaultAnimus(tag);
			stack.setTagCompound(tag);
		}
	}

	public static NBTTagCompound writeDefaultAnimus(NBTTagCompound tag) {
		if (tag == null)
			tag = new NBTTagCompound();
		Collection<AnimaStack> col = new ArrayList<>();
		for (Anima e : Anima.REGISTRY)
			col.add(new AnimaStack(e, 0));
		return writeToNBT(tag, col);
	}

	public static void writeToBuf(ByteBuf to, AnimaStack stack) {
		if (stack.isEmpty()) {
			to.writeByte(-1);
			to.writeShort(-1);
		} else {
			to.writeByte(stack.getCount());
			to.writeShort(Anima.REGISTRY.getValues().indexOf(stack.getAnima()));
		}
	}

	public static AnimaStack readFromBuf(ByteBuf from) {
		int count = from.readByte();
		int id = from.readShort();
		if (count < 0) {
			return AnimaStack.EMPTY;
		} else {

			AnimaStack stack = new AnimaStack(Anima.getAnimaByID(id), count);

			return stack;
		}
	}

	@Nonnull
	public static List<AnimaStack> readFromNBT(NBTTagCompound tag) {
		if (tag != null) {
			List<AnimaStack> ret = new ArrayList<>();
			NBTTagCompound essTag = tag.getCompoundTag(E);
			for (String s : essTag.getKeySet())
				ret.add(new AnimaStack(Anima.REGISTRY.getValue(new ResourceLocation(s)), essTag.getInteger(s)));

			return ret;
		} else {
			return readFromNBT(writeDefaultAnimus(new NBTTagCompound()));
		}
	}

	public static Map<Anima, AnimaStack> buildMapFromNBT(NBTTagCompound tag) {
		List<AnimaStack> stacks = readFromNBT(tag);
		Map<Anima, AnimaStack> map = new HashMap<>();
		for (AnimaStack stack : stacks)
			map.put(stack.getAnima(), stack);

		return map;
	}

	/*
	 * Saves all these anima stacks to a subcompound in the passed in tag.
	 * Duplicates will use the last passed stack. This method overrides all
	 * existing data, save wisely.
	 */
	public static NBTTagCompound writeToNBT(NBTTagCompound tag, AnimaStack... animus) {
		NBTTagCompound essTag = tag.getCompoundTag(E);
		for (AnimaStack e : animus) {

			if (e.getCount() < 0)
				essTag.setInteger(e.getAnima().getRegistryName().toString(), 0);
			else
				essTag.setInteger(e.getAnima().getRegistryName().toString(), e.getCount());
		}
		tag.setTag(E, essTag);
		return tag;
	}

	public static NBTTagCompound writeToNBT(NBTTagCompound tag, Collection<AnimaStack> animus) {
		return writeToNBT(tag, animus.toArray(new AnimaStack[animus.size()]));
	}
}
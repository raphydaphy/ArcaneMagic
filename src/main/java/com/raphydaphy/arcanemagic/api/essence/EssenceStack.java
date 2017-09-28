package com.raphydaphy.arcanemagic.api.essence;

public class EssenceStack
{

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
}
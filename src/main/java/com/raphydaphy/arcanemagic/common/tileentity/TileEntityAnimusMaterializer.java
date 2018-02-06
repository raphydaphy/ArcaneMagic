package com.raphydaphy.arcanemagic.common.tileentity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaCrystal;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAnimusMaterializer extends TileEntityAnimaStorage implements ITickable
{
	public static int SIZE = 6;

	private Anima curForming = null;
	private int curFormingTimer = 0;

	public List<AnimaStreamPoint> streamPoints = new ArrayList<AnimaStreamPoint>();

	public TileEntityAnimusMaterializer()
	{
		super(1000);

	}

	private boolean canForm(AnimaStack formStack)
	{

		boolean shouldContinue = false;
		for (int curItemStack = 0; curItemStack < SIZE; curItemStack++)
		{
			if (this.itemStackHandler.getStackInSlot(curItemStack).isEmpty())
			{
				shouldContinue = true;
				break;
			} else if (this.itemStackHandler.insertItem(curItemStack, curForming.getItemForm(), true).isEmpty())
			{
				shouldContinue = true;
				break;
			}
		}

		if (shouldContinue)
		{
			AnimaStack couldTakeThis = animaStorage.take(new AnimaStack(formStack.getAnima(), 10), false);
			if (couldTakeThis != null && !couldTakeThis.isEmpty())
			{
				shouldContinue = false;
			}
		}
		return shouldContinue;
	}

	@Override
	public void update()
	{
		if (!world.isRemote)
		{
			List<AnimaStreamPoint> newStreamPoints = new ArrayList<AnimaStreamPoint>();
			for (AnimaStreamPoint point : streamPoints)
			{
				if (point.update())
				{
					newStreamPoints.add(point);
				}
			}
			streamPoints = newStreamPoints;
			for (AnimaStack formStack : this.animaStorage.getStored().values())
			{
				if (formStack != null && !formStack.isEmpty())
				{
					if (this.curForming != null)
					{
						// we are already forming this essence
						if (formStack.getAnima().equals(this.curForming))
						{
							// keep on going! u can get ther ;-)
							if (this.curFormingTimer <= 100)
							{
								curFormingTimer++;
								if (!canForm(formStack))
								{
									curForming = null;
									curFormingTimer = 0;
									this.markDirty();
								}
							}
							// i said u could :D
							else
							{
								for (int curItemStack = 0; curItemStack < SIZE; curItemStack++)
								{
									boolean doTheThing = false;

									if (this.itemStackHandler.getStackInSlot(curItemStack).isEmpty())
									{
										AnimaStack couldTake = animaStorage
												.take(new AnimaStack(formStack.getAnima(), 100), false);
										couldTake = null;
										if (couldTake == null || couldTake.isEmpty())
										{
											this.itemStackHandler.setStackInSlot(curItemStack,
													curForming.getItemForm());
											doTheThing = true;
										}
									} else if (this.itemStackHandler
											.insertItem(curItemStack, curForming.getItemForm(), true).isEmpty())
									{
										AnimaStack couldTake = animaStorage
												.take(new AnimaStack(formStack.getAnima(), 100), false);
										couldTake = null;
										if (couldTake == null || couldTake.isEmpty())
										{
											this.itemStackHandler.insertItem(curItemStack, curForming.getItemForm(),
													false);
											doTheThing = true;
										}
									}

									if (doTheThing)
									{
										world.playSound(pos.getX(), pos.getY(), pos.getZ(),
												ArcaneMagicSoundHandler.randomMagicSound(), SoundCategory.BLOCKS, 1.0F,
												1.0F, false);
										// essenceStorage.take(new
										// EssenceStack(formStack.getEssence(),
										// 100), false);
										this.markDirty();
										this.curForming = null;
										this.curFormingTimer = 0;
										break;
									}
								}
							}

							break;
						}
					} else if (formStack.getCount() >= 1000)
					{
						this.curForming = formStack.getAnima();
						this.curFormingTimer = 0;
						break;
					}
				}
			}
		}
		for (int x = this.pos.getX() - 8; x < this.pos.getX() + 8; x++)
		{
			for (int y = this.pos.getY() - 3; y < this.pos.getY() + 3; y++)
			{
				for (int z = this.pos.getZ() - 8; z < this.pos.getZ() + 8; z++)
				{
					BlockPos here = new BlockPos(x, y, z);

					if (world.getBlockState(here).getBlock().equals(ModRegistry.ANIMA_CONJURER))
					{
						TileEntityAnimaConjurer te = (TileEntityAnimaConjurer) world.getTileEntity(here);

						if (te != null)
						{
							if (world.isRemote)
							{
								ArcaneMagic.proxy.magicParticle(Color.GREEN, this.getPos(), here);
							} else
							{

								streamPoints.add(new AnimaStreamPoint(world,
										new AnimaStack(Anima.getFromBiome(world.getBiome(here)), 1), here,
										this.getPos()));

								Map<Anima, AnimaStack> storedEssenceConcentrator = te
										.getCapability(IAnimaStorage.CAP, null).getStored();

								Anima useType = null;
								for (AnimaStack transferStack : storedEssenceConcentrator.values())
								{
									if (transferStack.getCount() > 0 && !world.isRemote)
									{
										useType = transferStack.getAnima();
										this.markDirty();
									}
								}
								if (useType != null && world.rand.nextInt(3) == 1)
								{
									if (!world.isRemote)
									{
										// actually send essence, not just
										// particles
										if (Anima.sendAnima(world,
												new AnimaStack(Anima.getFromBiome(world.getBiome(here)), 1),
												new Vec3d(x + 0.5, y + 0.6, z + 0.5),
												new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), false,
												false))
										{

										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE)
	{
		@Override
		public void onContentsChanged(int slot)
		{
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileEntityAnimusMaterializer.this.markDirty();
		}

		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
		{
			if (!(stack.getItem() instanceof IAnimaCrystal))
			{
				return stack;
			}
			return super.insertItem(slot, stack, simulate);
		}
	};

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("items"))
		{
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		if (compound.hasKey("curForming"))
		{
			curForming = Anima.getAnimaByID(compound.getInteger("curForming"));
		}
		curFormingTimer = compound.getInteger("curFormingTimer");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		if (curForming != null)
		{
			compound.setInteger("curForming", Anima.REGISTRY.getValues().indexOf(this.curForming));
		}
		compound.setInteger("curFormingTimer", this.curFormingTimer);
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}

	public class AnimaStreamPoint
	{
		private final BlockPos source;
		private final BlockPos destination;
		private World world;
		private int ticksExisted;
		private AnimaStack stack;

		public AnimaStreamPoint(World world, AnimaStack stack, BlockPos source, BlockPos destination)
		{
			ticksExisted = 0;
			this.source = source;
			this.destination = destination;
			this.world = world;
			this.stack = stack;
		}

		public boolean update()
		{
			ticksExisted++;

			if (ticksExisted >= 111)
			{
				TileEntity destUnchecked = world.getTileEntity(destination);

				if (destUnchecked != null && destUnchecked instanceof TileEntityAnimaStorage)
				{
					TileEntityAnimaStorage dest = (TileEntityAnimaStorage) destUnchecked;

					dest.animaStorage.store(stack, false);
					dest.markDirty();

				}
				return false;
			}
			return true;
		}
	}
}

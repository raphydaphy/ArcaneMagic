package com.raphydaphy.arcanemagic.common.tileentity;

import java.util.Map;

import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.essence.IEssenceStorage;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityCrystallizer extends TileEntityEssenceStorage implements ITickable
{
	public static int SIZE = 6;

	private Essence curForming = null;
	private int curFormingTimer = 0;

	public TileEntityCrystallizer()
	{
		super(1000);

	}

	private boolean canForm(EssenceStack formStack)
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
			EssenceStack couldTakeThis = essenceStorage.take(new EssenceStack(formStack.getEssence(), 10), false);
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
		if (world.isRemote)
		{
			return;
		}
		for (EssenceStack formStack : this.essenceStorage.getStored().values())
		{
			if (formStack != null && !formStack.isEmpty())
			{
				if (this.curForming != null)
				{
					// we are already forming this essence
					if (formStack.getEssence().equals(this.curForming))
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
									EssenceStack couldTake = essenceStorage
											.take(new EssenceStack(formStack.getEssence(), 100), false);
									couldTake = null;
									if (couldTake == null || couldTake.isEmpty())
									{
										this.itemStackHandler.setStackInSlot(curItemStack, curForming.getItemForm());
										doTheThing = true;
									}
								} else if (this.itemStackHandler
										.insertItem(curItemStack, curForming.getItemForm(), true).isEmpty())
								{
									EssenceStack couldTake = essenceStorage
											.take(new EssenceStack(formStack.getEssence(), 100), false);
									couldTake = null;
									if (couldTake == null || couldTake.isEmpty())
									{
										this.itemStackHandler.insertItem(curItemStack, curForming.getItemForm(), false);
										doTheThing = true;
									}
								}

								if (doTheThing)
								{
									world.playSound(pos.getX(), pos.getY(), pos.getZ(),
											ArcaneMagicSoundHandler.randomScepterSound(), SoundCategory.BLOCKS, 1.0F,
											1.0F, false);
									//essenceStorage.take(new EssenceStack(formStack.getEssence(), 100), false);
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
					this.curForming = formStack.getEssence();
					this.curFormingTimer = 0;
					break;
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

					if (world.getBlockState(here).getBlock().equals(ModRegistry.ESSENCE_CONCENTRATOR)
							&& world.getBlockState(here.add(0, -1, 0)).getBlock().equals(Blocks.IRON_BLOCK)
							&& world.getBlockState(here.add(0, -2, 0)).getBlock().equals(Blocks.IRON_BLOCK))
					{
						TileEntityEssenceConcentrator te = (TileEntityEssenceConcentrator) world.getTileEntity(here);

						if (te != null)
						{
							Map<Essence, EssenceStack> storedEssenceConcentrator = te
									.getCapability(IEssenceStorage.CAP, null).getStored();

							Essence useType = null;
							for (EssenceStack transferStack : storedEssenceConcentrator.values())
							{
								if (transferStack.getCount() > 0 && !world.isRemote)
								{
									useType = transferStack.getEssence();
									this.markDirty();
								}
							}
							if (useType != null && world.rand.nextInt(3) == 1)
							{
								if (!world.isRemote)
								{
									// actually send essence, not just particles
									if (Essence.sendEssence(world,
											new EssenceStack(Essence.getFromBiome(world.getBiome(here)), 1),
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

	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileEntityCrystallizer.this.markDirty();
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
			curForming = Essence.getEssenceByID(compound.getInteger("curForming"));
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
			compound.setInteger("curForming", Essence.REGISTRY.getValues().indexOf(this.curForming));
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
			// only extract with automation
			if (facing == null || facing == EnumFacing.DOWN)
			{
				return true;
			}
			return false;
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
}

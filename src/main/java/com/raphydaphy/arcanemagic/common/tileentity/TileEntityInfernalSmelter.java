package com.raphydaphy.arcanemagic.common.tileentity;

import java.awt.Color;
import java.util.Map;

import javax.annotation.Nonnull;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.api.anima.IAnimaCrystal;
import com.raphydaphy.arcanemagic.api.anima.IAnimaStorage;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInfernalSmelter extends TileEntityAnimaStorage implements ITickable
{
	public static int SIZE = 7;
	public static int ORE = 0;
	// crystals go in every slot above 0

	private int progress = 0;
	public int frameAge = 0;

	public TileEntityInfernalSmelter()
	{
		super(10000);
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if (TileEntityInfernalSmelter.this.world != null && TileEntityInfernalSmelter.this.pos != null)
		{
			IBlockState state = TileEntityInfernalSmelter.this.world.getBlockState(TileEntityInfernalSmelter.this.pos);
			TileEntityInfernalSmelter.this.world.markAndNotifyBlock(TileEntityInfernalSmelter.this.pos,
					TileEntityInfernalSmelter.this.world.getChunkFromBlockCoords(TileEntityInfernalSmelter.this.pos),
					state, state, 1 | 2);
		}
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

	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileEntityInfernalSmelter.this.markDirty();
		}

		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack)
		{
			return 1;
		}

		@Override
		@Nonnull
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
		{
			if (slot == ORE && ArcaneMagicAPI.getAnimaFromStack(stack) == null)
			{
				return stack;

			} else if (slot != ORE && !stack.isEmpty() && !(stack.getItem() instanceof IAnimaCrystal))
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
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		return compound;
	}

	public int getProgress()
	{
		return progress;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void update()
	{

		IItemHandler cap = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		if (!cap.getStackInSlot(ORE).isEmpty())
		{
			for (int x = this.pos.getX() - 8; x < this.pos.getX() + 8; x++)
			{
				for (int y = this.pos.getY() - 3; y < this.pos.getY() + 3; y++)
				{
					for (int z = this.pos.getZ() - 8; z < this.pos.getZ() + 8; z++)
					{
						BlockPos here = new BlockPos(x, y, z);
						if (world.getBlockState(here).getBlock().equals(ModRegistry.ELEMENTAL_BURNER))
						{

							TileEntityElementalBurner te = (TileEntityElementalBurner) world.getTileEntity(here);

							if (te != null)
							{
								if (world.isRemote)
								{

									Color color = Anima.getFromBiome(world.getBiome(
											new BlockPos(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ())))
											.getColor();
									color = Color.ORANGE;
									ArcaneMagic.proxy.magicParticle(color, this.getPos().add(0, 1, 0), here);
								} else
								{
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

										if (Anima.sendAnima(world, new AnimaStack(useType, 1),
												new Vec3d(x + 0.5, y + 0.5, z + 0.5),
												new Vec3d(pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5), false,
												true))
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
		if (!world.isRemote)
		{
			markDirty();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos, pos.add(1, 2, 1));
	}
}

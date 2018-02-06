package com.raphydaphy.arcanemagic.common.tileentity;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.api.anima.AnimaStack;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TileEntityAnimaConjurer extends TileEntityAnimaStorage implements ITickable
{
	private ItemStack stack = ItemStack.EMPTY;

	private int frameAge = 0;

	public TileEntityAnimaConjurer()
	{
		super(100);
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
		markDirty();

		if (world != null)
		{
			IBlockState state = world.getBlockState(this.pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if (TileEntityAnimaConjurer.this.world != null && TileEntityAnimaConjurer.this.pos != null)
		{
			IBlockState state = TileEntityAnimaConjurer.this.world.getBlockState(TileEntityAnimaConjurer.this.pos);
			TileEntityAnimaConjurer.this.world.markAndNotifyBlock(TileEntityAnimaConjurer.this.pos,
					TileEntityAnimaConjurer.this.world.getChunkFromBlockCoords(TileEntityAnimaConjurer.this.pos), state,
					state, 1 | 2);
		}
	}

	public int getFrameAge()
	{
		return frameAge;
	}

	public int increaseFrameAge()
	{
		frameAge++;

		return frameAge;
	}

	public int setFrameAge(int newAge)
	{
		frameAge = newAge;

		return frameAge;
	}

	@Override
	public void update()
	{
		if (world.isRemote)
		{
			return;
		}

		//if (this.getStack().getItem() == ModRegistry.ANCIENT_PARCHMENT) {
		for (int x = pos.getX() - 10; x < pos.getX() + 10; x++)
		{
			for (int y = pos.getY() - 5; y < pos.getY() + 5; y++)
			{
				for (int z = pos.getZ() - 10; z < pos.getZ() + 10; z++)
				{
					if (world.rand.nextInt(2000) == 1)
					{
						BlockPos here = new BlockPos(x, y, z);
						if (world.getBlockState(here).getBlock().equals(Blocks.BEDROCK))
						{
							Anima.sendAnima(world, new AnimaStack(Anima.getFromBiome(world.getBiome(here)), 1),
									new Vec3d(x + 0.5, y + 0.5, z + 0.5),
									new Vec3d(pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5), false, true);

						}
					}
				}
			}

		}
		//}

		markDirty();
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(pos, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if (compound.hasKey("item"))
		{
			stack = new ItemStack(compound.getCompoundTag("item"));
		} else
		{
			stack = ItemStack.EMPTY;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (!stack.isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			stack.writeToNBT(tagCompound);
			compound.setTag("item", tagCompound);
		}
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
}

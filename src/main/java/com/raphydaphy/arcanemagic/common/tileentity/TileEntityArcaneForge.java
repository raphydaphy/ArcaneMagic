package com.raphydaphy.arcanemagic.common.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityArcaneForge extends TileEntity
{
	private ItemStack weapon = ItemStack.EMPTY;
	private ItemStack[] gems = { ItemStack.EMPTY, ItemStack.EMPTY };
	private int[] insertDepth = { 4, 4 };

	public ItemStack getWeapon()
	{
		return weapon;
	}

	public void setWeapon(ItemStack stack)
	{
		this.weapon = stack;
		markDirty();

		if (world != null)
		{
			IBlockState state = world.getBlockState(this.pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	public int getDepth(int gem)
	{
		return insertDepth[gem];
	}

	public void setDepth(int depth, int gem)
	{
		insertDepth[gem] = depth;

		markDirty();

		if (world != null)
		{
			IBlockState state = world.getBlockState(this.pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	public void setGem(ItemStack gem, int num)
	{
		this.gems[num] = gem;

		markDirty();

		if (world != null)
		{
			IBlockState state = world.getBlockState(this.pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	public ItemStack getGem(int num)
	{
		return gems[num];
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		if (TileEntityArcaneForge.this.world != null && TileEntityArcaneForge.this.pos != null)
		{
			IBlockState state = TileEntityArcaneForge.this.world.getBlockState(TileEntityArcaneForge.this.pos);
			TileEntityArcaneForge.this.world.markAndNotifyBlock(TileEntityArcaneForge.this.pos,
					TileEntityArcaneForge.this.world.getChunkFromBlockCoords(TileEntityArcaneForge.this.pos), state,
					state, 1 | 2);
		}
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
		if (compound.hasKey("weapon"))
		{
			weapon = new ItemStack(compound.getCompoundTag("weapon"));
		} else
		{
			weapon = ItemStack.EMPTY;
		}
		if (compound.hasKey("gem0"))
		{
			gems[0] = new ItemStack(compound.getCompoundTag("gem0"));
		} else
		{
			gems[0] = ItemStack.EMPTY;
		}
		if (compound.hasKey("gem1"))
		{
			gems[1] = new ItemStack(compound.getCompoundTag("gem1"));
		} else
		{
			gems[1] = ItemStack.EMPTY;
		}
		insertDepth = compound.getIntArray("insertDepth");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (!weapon.isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			weapon.writeToNBT(tagCompound);
			compound.setTag("weapon", tagCompound);
		}
		if (!gems[0].isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			gems[0].writeToNBT(tagCompound);
			compound.setTag("gem0", tagCompound);
		}
		if (!gems[1].isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			gems[1].writeToNBT(tagCompound);
			compound.setTag("gem1", tagCompound);
		}
		compound.setIntArray("insertDepth", insertDepth);
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos, pos.add(2, 2, 2));
	}
}

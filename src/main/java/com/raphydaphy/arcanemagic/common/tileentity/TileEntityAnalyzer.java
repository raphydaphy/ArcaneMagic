package com.raphydaphy.arcanemagic.common.tileentity;

import java.util.ArrayList;
import java.util.UUID;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.capabilities.NotebookInfo;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.network.PacketNotebookToast;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityAnalyzer extends TileEntity implements ITickable
{
	private ItemStack stack = ItemStack.EMPTY;
	private int age = 0;

	private UUID stackOwner = null;

	public TileEntityAnalyzer()
	{

	}

	public ItemStack getStack()
	{
		return stack;
	}

	public void setPlayer(EntityPlayer player)
	{
		if (player != null)
		{
			this.stackOwner = player.getUniqueID();
		}
		else
		{
			this.stackOwner = null;
		}
		markDirty();
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
		this.age = 0;
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
		if (TileEntityAnalyzer.this.world != null && TileEntityAnalyzer.this.pos != null)
		{
			IBlockState state = TileEntityAnalyzer.this.world.getBlockState(TileEntityAnalyzer.this.pos);
			TileEntityAnalyzer.this.world.markAndNotifyBlock(TileEntityAnalyzer.this.pos,
					TileEntityAnalyzer.this.world.getChunkFromBlockCoords(TileEntityAnalyzer.this.pos), state, state,
					1 | 2);
		}
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
		age = compound.getInteger("age");

		if (compound.hasKey("stackOwner"))
		{
			stackOwner = compound.getUniqueId("stackOwner");
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
		compound.setInteger("age", age);
		if (stackOwner != null)
		{
			compound.setUniqueId("stackOwner", stackOwner);
		}
		return compound;
	}

	public int getAge()
	{
		return age;
	}

	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		// shadows told me to put 150 so i did
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public void update()
	{
		age++;
		
		if (!world.isRemote && age == 50 && getStack() != null && !getStack().isEmpty() && stackOwner != null)
		{
			analyze(world.getPlayerEntityByUUID(stackOwner));
		}
		this.markDirty();
	}

	public void analyze(EntityPlayer player)
	{
		if (player.world.isRemote)
		{
			return;
		}
		if (getStack() != null && !getStack().isEmpty())
		{
			NotebookInfo info = player.getCapability(NotebookInfo.CAP, null);

			if (info != null && info.getUsed())
			{

				for (NotebookCategory unlockableCat : ArcaneMagicAPI.getFromAnalysis(getStack().copy(),
						new ArrayList<>()))
				{
					if (unlockableCat != null)
					{
						if (!info.isUnlocked(unlockableCat.getRequiredTag()))
						{
							if (info.isUnlocked(unlockableCat.getPrerequisiteTag()))
							{
								info.setUnlocked(unlockableCat.getRequiredTag());

								ArcaneMagicPacketHandler.INSTANCE.sendTo(new PacketNotebookToast(unlockableCat),
										(EntityPlayerMP) player);
							}
						}
					}
				}
			}
		}
	}
}

package com.raphydaphy.arcanemagic.common.tileentity;

import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;

public class TileEntityAnalyzer extends TileEntity implements ITickable
{
	private ItemStack[] stacks = {ItemStack.EMPTY, ItemStack.EMPTY};
	
	private int age = 0;
	private boolean hasValidStack = false;

	private UUID stackOwner = null;

	public TileEntityAnalyzer()
	{

	}

	public ItemStack[] getStacks()
	{
		return stacks;
	}

	public void setPlayer(EntityPlayer player)
	{
		if (player != null)
		{
			this.stackOwner = player.getUniqueID();
		} else
		{
			this.stackOwner = null;
		}
		markDirty();
	}

	public void setStack(int stack, ItemStack item)
	{
		this.stacks[stack] = item;
		
		if (stack == 0)
		{
			this.age = 0;
	
			if (item != null && !item.isEmpty()
					&& ArcaneMagicAPI.getFromAnalysis(getStacks()[0].copy(), new ArrayList<>()).size() > 0)
			{
				hasValidStack = true;
			} else
			{
				hasValidStack = false;
			}
		}

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
		stacks = new ItemStack[2];
		if (compound.hasKey("analyzingStack"))
		{
			stacks[0] = new ItemStack(compound.getCompoundTag("analyzingStack"));
		} 
		if (compound.hasKey("parchmentStack"))
		{
			stacks[1] = new ItemStack(compound.getCompoundTag("parchmentStack"));
		}
		age = compound.getInteger("age");

		if (compound.hasKey("stackOwner"))
		{
			stackOwner = compound.getUniqueId("stackOwner");
		}

		hasValidStack = compound.getBoolean("hasValidStack");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (!stacks[0].isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			stacks[0].writeToNBT(tagCompound);
			compound.setTag("analyzingStack", tagCompound);
		}
		if (!stacks[1].isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			stacks[1].writeToNBT(tagCompound);
			compound.setTag("parchmentStack", tagCompound);
		}
		compound.setInteger("age", age);
		if (stackOwner != null)
		{
			compound.setUniqueId("stackOwner", stackOwner);
		}

		compound.setBoolean("hasValidStack", hasValidStack);
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

		if (world.rand.nextInt(3) == 1 && hasValidStack)
		{
			world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 5),
					pos.getY() + 0.7, pos.getZ() + 0.4 + (world.rand.nextFloat() / 5), 0, -0.5, 0);

		}
		this.markDirty();
	}

	public List<NotebookCategory> analyze(EntityPlayer player)
	{
		if (player.world.isRemote)
		{
			return new ArrayList<>();
		}
		if (getStacks()[0] != null && !getStacks()[0].isEmpty())
		{
			NotebookInfo info = player.getCapability(NotebookInfo.CAP, null);

			if (info != null && info.getUsed())
			{
				List<NotebookCategory> unlockable = ArcaneMagicAPI.getFromAnalysis(getStacks()[0].copy(),
						new ArrayList<>());
				for (NotebookCategory unlockableCat : unlockable)
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
				return unlockable;
			}
		}
		return new ArrayList<>();
	}
}

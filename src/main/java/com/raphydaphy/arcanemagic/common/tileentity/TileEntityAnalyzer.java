package com.raphydaphy.arcanemagic.common.tileentity;

import java.util.List;
import java.util.UUID;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.api.essence.Essence;
import com.raphydaphy.arcanemagic.api.essence.EssenceStack;
import com.raphydaphy.arcanemagic.api.notebook.INotebookInfo;
import com.raphydaphy.arcanemagic.api.notebook.NotebookCategory;
import com.raphydaphy.arcanemagic.common.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.item.ItemParchment;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAnalyzer extends TileEntityEssenceStorage implements ITickable
{
	// TODO: make this a nonnulllist
	private ItemStack[] stacks = { ItemStack.EMPTY, ItemStack.EMPTY };

	private int progress = 0;
	private boolean hasValidStack = false;

	private UUID stackOwner = null;

	public TileEntityAnalyzer()
	{
		super(200);
	}

	public ItemStack getStack(int stack)
	{
		if (stacks[stack] == null)
		{
			return ItemStack.EMPTY;
		}
		return stacks[stack];
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
		if (item == null)
		{
			item = ItemStack.EMPTY;
		}
		this.stacks[stack] = item;

		if (stack == 0)
		{
			this.progress = 0;

			evaulateStack();
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
			setStack(0, new ItemStack(compound.getCompoundTag("analyzingStack")));
		}
		if (compound.hasKey("parchmentStack"))
		{
			setStack(1, new ItemStack(compound.getCompoundTag("parchmentStack")));
		}
		progress = compound.getInteger("age");

		if (compound.hasKey("stackOwner"))
		{
			stackOwner = compound.getUniqueId("stackOwner");
		}

		hasValidStack = compound.getBoolean("hasValidStack");
	}

	public void evaulateStack()
	{
		hasValidStack = false;

		if (stackOwner != null)
		{
			EntityPlayer player = world.getPlayerEntityByUUID(stackOwner);
			if (player != null)
			{
				List<NotebookCategory> unlockable = ArcaneMagicAPI.getAnalyzer().getAnalysisResults(getStack(0));
				if (getStack(0) != null && !getStack(0).isEmpty() && unlockable.size() > 0)
				{
					INotebookInfo info = player.getCapability(INotebookInfo.CAP, null);
					if (info != null)
					{
						for (NotebookCategory c : unlockable)
						{
							if (info.isUnlocked(c.getPrerequisiteTag()) && !info.isUnlocked(c.getRequiredTag()))
							{
								hasValidStack = true;
								break;
							}
						}
					}

				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (!getStack(0).isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			getStack(0).writeToNBT(tagCompound);
			compound.setTag("analyzingStack", tagCompound);
		}
		if (!getStack(1).isEmpty())
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			getStack(1).writeToNBT(tagCompound);
			compound.setTag("parchmentStack", tagCompound);
		}
		compound.setInteger("progress", progress);
		if (stackOwner != null)
		{
			compound.setUniqueId("stackOwner", stackOwner);
		}

		compound.setBoolean("hasValidStack", hasValidStack);
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
		progress++;

		if (hasValidStack)
		{
			if (!getStack(1).isEmpty() && !world.isRemote)
			{
				if ((essenceStorage.getTotalStored() >= 200) && stackOwner != null)
				{

					// just reached 200
					if (progress > 0)
					{
						progress = -200;
					}
					if (progress == -10)
					{
						List<NotebookCategory> unlockable = ArcaneMagicAPI.getAnalyzer()
								.getAnalysisResults(getStack(0));
						if (!unlockable.isEmpty())
						{
							if (stackOwner != null)
							{
								EntityPlayer player = world.getPlayerEntityByUUID(stackOwner);
								if (player != null)
								{
									INotebookInfo info = player.getCapability(INotebookInfo.CAP, null);
									if (info != null)
									{
										for (NotebookCategory cat : unlockable)
										{
											if (info.isUnlocked(cat.getPrerequisiteTag())
													&& !info.isUnlocked(cat.getRequiredTag()))
											{
												ItemStack writtenParchment = new ItemStack(
														ModRegistry.WRITTEN_PARCHMENT, 1);
												if (!writtenParchment.hasTagCompound())
												{
													writtenParchment.setTagCompound(new NBTTagCompound());
												}

												writtenParchment.getTagCompound().setString(ItemParchment.TITLE,
														cat.getUnlocParchmentInfo().first());
												writtenParchment.getTagCompound().setString(ItemParchment.CATEGORY,
														cat.getUnlocalizedName());
												writtenParchment.getTagCompound().setInteger(ItemParchment.PARAGRAPHS,
														cat.getUnlocParchmentInfo().second());

												EntityItemFancy parchmentEntity = new EntityItemFancy(world,
														pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5,
														writtenParchment);
												parchmentEntity.motionX = 0;
												parchmentEntity.motionY = 0;
												parchmentEntity.motionZ = 0;
												world.spawnEntity(parchmentEntity);
												break;
											}
										}
									}
								}
							}

							world.playSound(null, pos, ArcaneMagicSoundHandler.elemental_crafting_success,
									SoundCategory.BLOCKS, 1, 1);
							for (EssenceStack e : essenceStorage.getStored().values())
								essenceStorage.take(e, false);

							setStack(1, ItemStack.EMPTY);

							progress = 0;
						}
					}
				}
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
									// Send some essence to the parchment for ink
									Essence.sendEssence(world,
											new EssenceStack(Essence.getFromBiome(world.getBiome(here)), 1),
											new Vec3d(x + 0.5, y + 0.5, z + 0.5),
											new Vec3d(pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5),
											new Vec3d(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5), false,
											true);

								}
							}
						}
					}
				}
			} else if (world.isRemote && world.rand.nextInt(3) == 1)
			{
				world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.4 + (world.rand.nextFloat() / 5),
						pos.getY() + 0.5, pos.getZ() + 0.4 + (world.rand.nextFloat() / 5), 0, -0.5, 0);
			}

			if (world.getTotalWorldTime() % 50 == 0)
			{
				evaulateStack();
			}

		} else if (!getStack(1).isEmpty() && !world.isRemote)
		{
			EntityItem blankParchment = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5,
					getStack(1).copy());
			blankParchment.motionX = 0;
			blankParchment.motionY = 0;
			blankParchment.motionZ = 0;
			world.spawnEntity(blankParchment);
			setStack(1, ItemStack.EMPTY);

			for (EssenceStack e : essenceStorage.getStored().values())
			{
				essenceStorage.take(e, false);
			}

		}
		this.markDirty();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(pos, pos.add(1, 2, 1));
	}
}

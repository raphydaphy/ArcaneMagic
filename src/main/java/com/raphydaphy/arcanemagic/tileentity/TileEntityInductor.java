package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.anima.IAnimaInductible;
import com.raphydaphy.arcanemagic.anima.IAnimaReceiver;
import com.raphydaphy.arcanemagic.block.BlockInductor;
import com.raphydaphy.arcanemagic.network.PacketDeathParticles;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityInductor extends TileEntityAnimaStorage implements IAnimaInductible, ITickable
{
    private static final int CAPACITY = 10000;
    private static final String MODE_KEY = "block_mode";
    private static final String LINK_KEY = "link_pos";

    private boolean blockMode = false;
    private BlockPos link = null;

    public TileEntityInductor()
    {
        super(ArcaneMagic.INDUCTOR_TE);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        if (tag.hasKey(MODE_KEY))
        {
            blockMode = tag.getBoolean(MODE_KEY);
        }
        if (tag.hasKey(LINK_KEY))
        {
            link = BlockPos.fromLong(tag.getLong(LINK_KEY));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean(MODE_KEY, blockMode);
        if (link != null)
        {
            tag.setLong(LINK_KEY, link.toLong());
        }
        return tag;
    }

    public boolean setMode(boolean blockMode)
    {
        if (!world.isRemote)
        {
            this.blockMode = blockMode;
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockInductor.BLOCK_MODE, blockMode));
            markDirty();
        }
        return this.blockMode;
    }

    public boolean getMode()
    {
        return blockMode;
    }

    public void setLinkPos(BlockPos pos)
    {
        if (!world.isRemote)
        {
            link = pos;
            if (!blockMode)
            {
                blockMode = true;
                world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockInductor.BLOCK_MODE, true));
            }
            markDirty();
        }
    }

    @Override
    public boolean takeAnima(int anima)
    {
        if (getCurrentAnima() >= anima)
        {
            this.anima -= anima;
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            if (blockMode)
            {
                if (link != null)
                {

                } else
                {
                    blockMode = false;
                    world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockInductor.BLOCK_MODE, false));
                    markDirty();
                    System.err.println("[Inductor] Block mode was enabled but no block was linked");
                }
            } else
            {
                if (anima < CAPACITY)
                {
                    // TODO: frequency based on anima in chunk
                    if (world.getTotalWorldTime() % 50 == 0)
                    {
                        extractAnimaAir();
                    }
                }
            }
            if (world.getTotalWorldTime() % 50 == 0)
            {
                System.out.println("Pos: " + link);
            }
        }
    }

    private void extractAnimaAir()
    {
        if (world.getMinecraftServer() != null)
        {
            for (EntityPlayerMP player : world.getMinecraftServer().getPlayerList().getPlayers())
            {
                System.out.println("player ");
                player.connection.sendPacket(new PacketDeathParticles(pos.getX() + 0.5f, pos.getY() - 1.5f, pos.getZ() + 0.5f, 5, 5, pos));
            }
        }
    }
}

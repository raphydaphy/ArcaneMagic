package com.raphydaphy.arcanemagic.tileentity;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.anima.IAnimaInductible;
import com.raphydaphy.arcanemagic.anima.IAnimaReceiver;
import com.raphydaphy.arcanemagic.block.BlockInductor;
import com.raphydaphy.arcanemagic.network.PacketAnimaParticles;
import com.raphydaphy.arcanemagic.network.PacketDeathParticles;
import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import it.unimi.dsi.fastutil.ints.IntIndirectHeaps;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

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
        // TODO: frequency based on anima in chunk
        if (!world.isRemote && world.getTotalWorldTime() % 50 == 0)
        {
            System.out.println("Pos: " + link + " Anima: " + anima);
            if (blockMode)
            {
                if (link != null)
                {
                    TileEntity at = world.getTileEntity(link);
                    if (at instanceof IAnimaInductible)
                    {
                        IAnimaInductible inductible = (IAnimaInductible) at;
                        if (inductible.takeAnima(10))
                        {
                            extractAnimaBlock();
                            if (this.anima + 10 <= CAPACITY)
                            {
                                this.anima += 10;
                            } else
                            {
                                this.anima = CAPACITY;
                            }
                            markDirty();
                        }
                    } else
                    {
                        setBlockMode(false);
                    }
                } else
                {
                    setBlockMode(false);
                    System.err.println("[Inductor] Block mode was enabled but no block was linked");
                }
            } else
            {
                if (anima < CAPACITY)
                {
                    extractAnimaAir();
                    this.anima += 1;
                    markDirty();
                }
            }
        } else if (world.isRemote && clientParticleTick > 0)
        {
            clientParticleTick--;

            ArcaneMagicUtils.magicParticle(new Color(world.getBiome(pos).getWaterColor()), pos, clientParticleLink);


            if (clientParticleTick == 0)
            {
                clientParticleLink = null;
            }
        }
    }

    private void setBlockMode(boolean blockMode)
    {
        if (!world.isRemote && this.blockMode != blockMode)
        {
            this.blockMode = blockMode;
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockInductor.BLOCK_MODE, blockMode));
            markDirty();
        }
    }

    private void extractAnimaAir()
    {
        if (world.getMinecraftServer() != null)
        {
            for (EntityPlayerMP player : world.getMinecraftServer().getPlayerList().getPlayers())
            {
                player.connection.sendPacket(new PacketDeathParticles(pos.getX() + 0.5f, pos.getY() - 1.5f, pos.getZ() + 0.5f, 5, 5, pos));
            }
        }
    }

    private BlockPos clientParticleLink;
    private int clientParticleTick = 0;

    public void animaParticlePacket(BlockPos link)
    {
        this.clientParticleLink = link;
        this.clientParticleTick = 50;
    }

    private void extractAnimaBlock()
    {
        if (world.getMinecraftServer() != null)
        {
            for (EntityPlayerMP player : world.getMinecraftServer().getPlayerList().getPlayers())
            {
                player.connection.sendPacket(new PacketAnimaParticles(link, pos));
            }
        }
    }
}

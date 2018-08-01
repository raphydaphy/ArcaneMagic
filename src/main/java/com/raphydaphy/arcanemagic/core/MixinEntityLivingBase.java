package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.anima.AnimaReceiveMethod;
import com.raphydaphy.arcanemagic.client.particle.ParticleAnimaDeath;
import com.raphydaphy.arcanemagic.tileentity.TileEntityAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityDrowned;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPhantom;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({EntityDrowned.class, EntityEnderman.class, EntityBlaze.class, EntityPhantom.class})
public abstract class MixinEntityLivingBase extends EntityLivingBase
{
    protected MixinEntityLivingBase(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    public void onDeath(DamageSource src)
    {
        super.onDeath(src);
        if (!world.isRemote)
        {
            BlockPos altar = findAltar();

            if (altar != null)
            {
                TileEntity te = world.getTileEntity(altar);
                if (te instanceof TileEntityAltar)
                {
                    ((TileEntityAltar)te).receiveAnima(world.rand.nextInt(100) + 150, AnimaReceiveMethod.SPECIAL);
                }
            }
        }
    }

    private BlockPos findAltar()
    {
        for (int x = (int)this.posX - 1; x <= (int)this.posX + 1; x++)
        {
            for (int y = (int)this.posY - 2; y <= (int)this.posY; y++)
            {
                for (int z = (int)this.posZ - 1; z <= (int)this.posZ + 1; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() == ArcaneMagic.ALTAR)
                    {
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTime;
        if (this.deathTime == 20)
        {
            int count;
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                count = this.getExperiencePoints(this.attackingPlayer);

                while(count > 0)
                {
                    int lvt_2_1_ = EntityXPOrb.getXPSplit(count);
                    count -= lvt_2_1_;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, lvt_2_1_));
                }
            }

            this.setDead();

            if (world.isRemote)
            {
                for(count = 0; count < 40; ++count)
                {
                    double speedX = -this.rand.nextGaussian() * 0.02D;
                    double speedY = -this.rand.nextGaussian() * 0.02D;
                    double speedZ = -this.rand.nextGaussian() * 0.02D;
                    deathParticles(this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, speedX, speedY, speedZ);
                }
            }
        }

    }

    private void deathParticles(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleAnimaDeath(world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn));
    }
}

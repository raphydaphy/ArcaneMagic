package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityDrowned;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPhantom;
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
        BlockPos altar = findAltar();

        if (altar != null)
        {
            System.out.println("soul dust go");
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
}

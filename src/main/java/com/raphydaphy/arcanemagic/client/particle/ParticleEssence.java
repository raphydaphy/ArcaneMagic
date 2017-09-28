package com.raphydaphy.arcanemagic.client.particle;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.essence.Essence;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleEssence extends Particle
{
	private final float flameScale;
	private final Vec3d travelPos;

	public ParticleEssence(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, int color, Vec3d travelPos)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.motionX = this.motionX * 0.009999999776482582D + xSpeedIn;
		this.motionY = this.motionY * 0.009999999776482582D + ySpeedIn;
		this.motionZ = this.motionZ * 0.009999999776482582D + zSpeedIn;
		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.flameScale = this.particleScale;
		this.setColor(0xFFFFFF);
		this.particleAlpha = 1f;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.travelPos = travelPos;
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(new ResourceLocation(ArcaneMagic.MODID, "misc/essence/essence" + (rand.nextInt(14) + 1)).toString());
		this.setParticleTexture(sprite);
	}
	
	public void setColor(int p_187146_1_)
    {
        float f = (float)((p_187146_1_ & 16711680) >> 16) / 255.0F;
        float f1 = (float)((p_187146_1_ & 65280) >> 8) / 255.0F;
        float f2 = (float)((p_187146_1_ & 255) >> 0) / 255.0F;
        float f3 = 1.0F;
        this.setRBGColorF(f * 1.0F, f1 * 1.0F, f2 * 1.0F);
    }

	@Override
	public int getFXLayer()
	{
		return 1;
	}

	@Override
	public void move(double x, double y, double z)
	{
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}
	
	private void renderParticleSpecial(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = (float)this.particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float)this.particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleTexture != null)
        {
        	// how do i change this to be whhat i want!
            f = this.particleTexture.getMinU();
            f1 = this.particleTexture.getMaxU();
            f2 = this.particleTexture.getMinV();
            f3 = this.particleTexture.getMaxV();
        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {new Vec3d((double)(-rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double)(-rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(rotationYZ * f4 - rotationXZ * f4))};

        if (this.particleAngle != 0.0F)
        {
            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.x;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.y;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.z;
            Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

            for (int l = 0; l < 4; ++l)
            {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double)(2.0F * f9)));
            }
        }

        float r = particleRed;
        float g = particleGreen;
        float b = particleBlue;
        
        //GlStateManager.color(r, g, b, 1);
        buffer.pos((double)f5 + avec3d[0].x, (double)f6 + avec3d[0].y, (double)f7 + avec3d[0].z).tex((double)f1, (double)f3).color(r, g, b, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + avec3d[1].x, (double)f6 + avec3d[1].y, (double)f7 + avec3d[1].z).tex((double)f1, (double)f2).color(r, g, b, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + avec3d[2].x, (double)f6 + avec3d[2].y, (double)f7 + avec3d[2].z).tex((double)f, (double)f2).color(r, g, b, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + avec3d[3].x, (double)f6 + avec3d[3].y, (double)f7 + avec3d[3].z).tex((double)f, (double)f3).color(r, g, b, this.particleAlpha).lightmap(j, k).endVertex();
    }

	@Override
	public int getBrightnessForRender(float p_189214_1_)
	{
		return 100;
	}

	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (travelPos.x <= this.posX + 0.1 && travelPos.x >= this.posX - 0.1 && travelPos.y <= this.posY + 0.1
				&& travelPos.y >= this.posY - 0.1 && travelPos.z <= this.posZ + 0.1 && travelPos.z >= this.posZ - 0.1)
		{
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX = (travelPos.x - this.posX) / (7 + rand.nextDouble());
		this.motionY = (travelPos.y - this.posY) / (7 + rand.nextDouble());
		this.motionZ = (travelPos.z - this.posZ) / (7 + rand.nextDouble());

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}
}

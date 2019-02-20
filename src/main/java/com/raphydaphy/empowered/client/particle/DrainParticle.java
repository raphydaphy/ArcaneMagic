package com.raphydaphy.empowered.client.particle;

import net.minecraft.class_3999;
import net.minecraft.class_4002;
import net.minecraft.class_4003;
import net.minecraft.world.World;

public class DrainParticle extends class_4003
{
	protected final class_4002 field_17866;
	private final float field_3881;
	private float field_3879 = 0.91F;
	private float targetColorRed;
	private float targetColorGreen;
	private float targetColorBlue;
	private boolean changesColor;

	protected DrainParticle(World world_1, double double_1, double double_2, double double_3, class_4002 class_4002_1, float float_1) {
		super(world_1, double_1, double_2, double_3);
		this.field_17866 = class_4002_1;
		this.field_3881 = float_1;
	}

	// AnimatedParticle
	public void setColor(int int_1) {
		float float_1 = (float)((int_1 & 16711680) >> 16) / 255.0F;
		float float_2 = (float)((int_1 & '\uff00') >> 8) / 255.0F;
		float float_3 = (float)((int_1 & 255) >> 0) / 255.0F;
		float float_4 = 1.0F;
		this.setColor(float_1 * 1.0F, float_2 * 1.0F, float_3 * 1.0F);
	}

	// AnimatedParticle
	public void setTargetColor(int int_1) {
		this.targetColorRed = (float)((int_1 & 16711680) >> 16) / 255.0F;
		this.targetColorGreen = (float)((int_1 & '\uff00') >> 8) / 255.0F;
		this.targetColorBlue = (float)((int_1 & 255) >> 0) / 255.0F;
		this.changesColor = true;
	}

	// AnimatedParticle
	public class_3999 method_18122() {
		return class_3999.field_17829;
	}

	// AnimatedParticle
	@Override
	public void update() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.method_18142(this.field_17866);
			if (this.age > this.maxAge / 2) {
				this.setColorAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
				if (this.changesColor) {
					this.colorRed += (this.targetColorRed - this.colorRed) * 0.2F;
					this.colorGreen += (this.targetColorGreen - this.colorGreen) * 0.2F;
					this.colorBlue += (this.targetColorBlue - this.colorBlue) * 0.2F;
				}
			}

			this.velocityY += (double)this.field_3881;
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.velocityX *= (double)this.field_3879;
			this.velocityY *= (double)this.field_3879;
			this.velocityZ *= (double)this.field_3879;
			if (this.onGround) {
				this.velocityX *= 0.699999988079071D;
				this.velocityZ *= 0.699999988079071D;
			}

		}
	}

	public int getColorMultiplier(float float_1) {
		return 15728880;
	}

	protected void method_3091(float float_1) {
		this.field_3879 = float_1;
	}
}

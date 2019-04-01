package com.raphydaphy.arcanemagic.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin
{
	@Shadow @Final private MinecraftClient client;

	@Shadow @Final private TextureManager textureManager;

	@Shadow private ClientWorld world;

	@Shadow private boolean vertexBufferObjectsEnabled;

	@Shadow private GlBuffer field_4102;

	@Shadow private int field_4067;

	@Shadow private int field_4117;

	@Shadow private GlBuffer field_4087;

	@Shadow @Final private static Identifier SUN_TEX;

	@Shadow @Final private static Identifier MOON_PHASES_TEX;

	@Shadow @Final private VertexFormat field_4100;

	private GlBuffer soulDimensionStarBuffer;
	private int soulDimensionStarDisplayList;

	@Inject(at = @At("HEAD"), method = "renderSky", cancellable = true)
	private void renderSky(float tickDelta, CallbackInfo info)
	{
		if (client.world.dimension.getType() == ModRegistry.SOUL_DIMENSION)
		{
			renderSoulDimensionSky(tickDelta);
			info.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "setupStarRendering")
	private void setupSoulDimensionStarRendering(CallbackInfo info)
	{
		Tessellator tessellator_1 = Tessellator.getInstance();
		BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
		if (this.soulDimensionStarBuffer != null)
		{
			this.soulDimensionStarBuffer.delete();
		}

		if (this.soulDimensionStarDisplayList >= 0)
		{
			GlAllocationUtils.deleteSingletonList(this.soulDimensionStarDisplayList);
			this.soulDimensionStarDisplayList = -1;
		}

		if (this.vertexBufferObjectsEnabled)
		{
			this.soulDimensionStarBuffer = new GlBuffer(this.field_4100);
			this.renderSoulDimensionStars(bufferBuilder_1);
			bufferBuilder_1.end();
			bufferBuilder_1.clear();
			this.soulDimensionStarBuffer.set(bufferBuilder_1.getByteBuffer());
		} else
		{
			this.soulDimensionStarDisplayList = GlAllocationUtils.genLists(1);
			GlStateManager.pushMatrix();
			GlStateManager.newList(this.soulDimensionStarDisplayList, 4864);
			this.renderSoulDimensionStars(bufferBuilder_1);
			tessellator_1.draw();
			GlStateManager.endList();
			GlStateManager.popMatrix();
		}

	}

	private void renderSoulDimensionStars(BufferBuilder bufferBuilder_1)
	{
		Random random_1 = new Random(10842L);
		bufferBuilder_1.begin(7, VertexFormats.POSITION);

		for (int int_1 = 0; int_1 < 1500; ++int_1)
		{
			double double_1 = (double) (random_1.nextFloat() * 2.0F - 1.0F);
			double double_2 = (double) (random_1.nextFloat() * 2.0F - 1.0F);
			double double_3 = (double) (random_1.nextFloat() * 2.0F - 1.0F);
			double double_4 = (double) (0.15F + random_1.nextFloat() * 0.1F);
			double double_5 = double_1 * double_1 + double_2 * double_2 + double_3 * double_3;
			if (double_5 < 1.0D && double_5 > 0.01D)
			{
				double_5 = 1.0D / Math.sqrt(double_5);
				double_1 *= double_5;
				double_2 *= double_5;
				double_3 *= double_5;
				double mainX = double_1 * 100.0D;
				double mainY = double_2 * 100.0D;
				double mainZ = double_3 * 100.0D;
				double double_9 = Math.atan2(double_1, double_3);
				double double_10 = Math.sin(double_9);
				double double_11 = Math.cos(double_9);
				double double_12 = Math.atan2(Math.sqrt(double_1 * double_1 + double_3 * double_3), double_2);
				double double_13 = Math.sin(double_12);
				double double_14 = Math.cos(double_12);
				double double_15 = random_1.nextDouble() * 3.141592653589793D * 2.0D;
				double double_16 = Math.sin(double_15);
				double double_17 = Math.cos(double_15);

				for (int int_2 = 0; int_2 < 4; ++int_2)
				{
					double double_18 = 0.0D;
					double double_19 = (double) ((int_2 & 2) - 1) * double_4;
					double double_20 = (double) ((int_2 + 1 & 2) - 1) * double_4;
					double double_21 = 0.0D;
					double double_22 = double_19 * double_17 - double_20 * double_16;
					double double_23 = double_20 * double_17 + double_19 * double_16;
					double addY = double_22 * double_13 + 0.0D * double_14;
					double double_26 = 0.0D * double_13 - double_22 * double_14;
					double addX = double_26 * double_10 - double_23 * double_11;
					double addZ = double_23 * double_10 + double_26 * double_11;
					bufferBuilder_1.vertex(mainX + addX, mainY + addY - 200, mainZ + addZ).next();
				}
			}
		}
	}

	private void renderSoulDimensionSky(float tickDelta)
	{
		GlStateManager.disableTexture();
		Vec3d vec3d_1 = this.world.getSkyColor(this.client.gameRenderer.getCamera().getBlockPos(), tickDelta);
		float float_2 = (float) vec3d_1.x;
		float float_3 = (float) vec3d_1.y;
		float float_4 = (float) vec3d_1.z;
		GlStateManager.color3f(float_2, float_3, float_4);
		Tessellator tessellator_1 = Tessellator.getInstance();
		BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color3f(float_2, float_3, float_4);
		if (this.vertexBufferObjectsEnabled)
		{
			this.field_4087.bind();
			GlStateManager.enableClientState(32884);
			GlStateManager.vertexPointer(3, 5126, 12, 0);
			this.field_4087.draw(7);
			GlBuffer.unbind();
			GlStateManager.disableClientState(32884);
		} else
		{
			GlStateManager.callList(this.field_4117);
		}
		GlStateManager.disableFog();
		GlStateManager.disableAlphaTest();
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GuiLighting.disable();
		float[] floats_1 = this.world.dimension.getBackgroundColor(this.world.getSkyAngle(tickDelta), tickDelta);
		float float_11 = 0;
		int int_2;
		float float_8, float_9, float_10, float_12;
		if (floats_1 != null)
		{
			GlStateManager.disableTexture();
			GlStateManager.shadeModel(7425);
			GlStateManager.pushMatrix();
			GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(MathHelper.sin(this.world.method_8442(tickDelta)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
			float_11 = floats_1[0];
			float_12 = floats_1[1];
			float float_7 = floats_1[2];
			bufferBuilder_1.begin(6, VertexFormats.POSITION_COLOR);
			bufferBuilder_1.vertex(0.0D, 100.0D, 0.0D).color(float_11, float_12, float_7, floats_1[3]).next();
			for (int_2 = 0; int_2 <= 16; ++int_2)
			{
				float_8 = (float) int_2 * 6.2831855F / 16.0F;
				float_9 = MathHelper.sin(float_8);
				float_10 = MathHelper.cos(float_8);
				bufferBuilder_1.vertex((double) (float_9 * 120.0F), (double) (float_10 * 120.0F), (double) (-float_10 * 40.0F * floats_1[3])).color(floats_1[0], floats_1[1], floats_1[2], 0.0F).next();
			}

			tessellator_1.draw();
			GlStateManager.popMatrix();
			GlStateManager.shadeModel(7424);
		}

		GlStateManager.enableTexture();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();


		//  START SUN AND MOON
		float_11 = 1.0F - this.world.getRainGradient(tickDelta);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, float_11);
		GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(this.world.getSkyAngle(tickDelta) * 360.0F, 1.0F, 0.0F, 0.0F);
		float_12 = 30.0F;
		this.textureManager.bindTexture(SUN_TEX);
		bufferBuilder_1.begin(7, VertexFormats.POSITION_UV);
		bufferBuilder_1.vertex((double) (-float_12), 100.0D, (double) (-float_12)).texture(0.0D, 0.0D).next();
		bufferBuilder_1.vertex((double) float_12, 100.0D, (double) (-float_12)).texture(1.0D, 0.0D).next();
		bufferBuilder_1.vertex((double) float_12, 100.0D, (double) float_12).texture(1.0D, 1.0D).next();
		bufferBuilder_1.vertex((double) (-float_12), 100.0D, (double) float_12).texture(0.0D, 1.0D).next();
		tessellator_1.draw();
		float_12 = 20.0F;
		this.textureManager.bindTexture(MOON_PHASES_TEX);
		int int_3 = this.world.getMoonPhase();
		int int_4 = int_3 % 4;
		int_2 = int_3 / 4 % 2;
		float_8 = (float) (int_4 + 0) / 4.0F;
		float_9 = (float) (int_2 + 0) / 2.0F;
		float_10 = (float) (int_4 + 1) / 4.0F;
		float float_16 = (float) (int_2 + 1) / 2.0F;
		bufferBuilder_1.begin(7, VertexFormats.POSITION_UV);
		bufferBuilder_1.vertex((double) (-float_12), -100.0D, (double) float_12).texture((double) float_10, (double) float_16).next();
		bufferBuilder_1.vertex((double) float_12, -100.0D, (double) float_12).texture((double) float_8, (double) float_16).next();
		bufferBuilder_1.vertex((double) float_12, -100.0D, (double) (-float_12)).texture((double) float_8, (double) float_9).next();
		bufferBuilder_1.vertex((double) (-float_12), -100.0D, (double) (-float_12)).texture((double) float_10, (double) float_9).next();
		tessellator_1.draw();

		// END SUN AND MOON
		GlStateManager.disableTexture();
		float float_17 = this.world.getStarsBrightness(tickDelta) * float_11;
		if (float_17 > 0.0F)
		{
			GlStateManager.color4f(float_17, float_17, float_17, float_17);
			if (this.vertexBufferObjectsEnabled)
			{
				this.soulDimensionStarBuffer.bind();
				GlStateManager.enableClientState(32884);
				GlStateManager.vertexPointer(3, 5126, 12, 0);
				this.soulDimensionStarBuffer.draw(7);
				GlBuffer.unbind();
				GlStateManager.disableClientState(32884);
			} else
			{
				GlStateManager.callList(this.soulDimensionStarDisplayList);
			}
		}

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.disableTexture();
		GlStateManager.color3f(0.0F, 0.0F, 0.0F);
		double double_1 = this.client.player.getCameraPosVec(tickDelta).y - this.world.getHorizonHeight();
		if (double_1 < 0.0D)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0.0F, 12.0F, 0.0F);
			if (this.vertexBufferObjectsEnabled)
			{
				this.field_4102.bind();
				GlStateManager.enableClientState(32884);
				GlStateManager.vertexPointer(3, 5126, 12, 0);
				this.field_4102.draw(7);
				GlBuffer.unbind();
				GlStateManager.disableClientState(32884);
			} else
			{
				GlStateManager.callList(this.field_4067);
			}

			GlStateManager.popMatrix();
		}

		if (this.world.dimension.method_12449())
		{
			GlStateManager.color3f(float_2 * 0.2F + 0.04F, float_3 * 0.2F + 0.04F, float_4 * 0.6F + 0.1F);
		} else
		{
			GlStateManager.color3f(float_2, float_3, float_4);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translatef(0.0F, -((float) (double_1 - 16.0D)), 0.0F);
		GlStateManager.callList(this.field_4067);
		GlStateManager.popMatrix();
		GlStateManager.enableTexture();
		GlStateManager.depthMask(true);
	}
}

package com.raphydaphy.arcanemagic.client.particle;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class FountainRenderer {

	public FountainRenderer() {

	}

	public void render(List<ParticleFountain> particles) {
		prepare();

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		for (ParticleFountain particle : particles) {
			GlStateManager.translate(particle.getPosition().x, particle.getPosition().y, particle.getPosition().z);
			
			vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);

			vb.pos(-0.5, -0.5, 0).endVertex();
			vb.pos(0.5, -0.5, 0).endVertex();
			vb.pos(0.5, 0.5, 0).endVertex();

			vb.pos(0.5, 0.5, 0).endVertex();
			vb.pos(-0.5, 0.5, 0).endVertex();
			vb.pos(-0.5, -0.5, 0).endVertex();

			tes.draw();
			GlStateManager.translate(-particle.getPosition().x, -particle.getPosition().y, -particle.getPosition().z);
		}
	}

	private void prepare() {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.depthMask(false);
	}
}

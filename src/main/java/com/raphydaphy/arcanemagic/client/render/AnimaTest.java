package com.raphydaphy.arcanemagic.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class AnimaTest {
	int MAX_PARTICLES = 250;
	AnimaParticle[] particles = new AnimaParticle[MAX_PARTICLES];
	public AnimaTest()
	{
		for (int particle = 0; particle < MAX_PARTICLES; particle++)
		{
			particles[particle] = new AnimaParticle();
		}
	}
	
	
	boolean rainbow = true; // Toggle rainbow effect
	double slowdown = -1.0; // Slow Down Particles
	int xspeed; // Base X Speed (To Allow Keyboard Direction Of Tail)
	int yspeed; // Base Y Speed (To Allow Keyboard Direction Of Tail)
	double zoom = -10.0; // Used To Zoom Out
	int col = 0; // Current Color Selection
	int delay; // Rainbow Effect Delay

	int quadList;

	float[][] colors = { { 1.0f, 0.5f, 0.5f }, { 1.0f, 0.75f, 0.5f }, { 1.0f, 1.0f, 0.5f }, { 0.75f, 1.0f, 0.5f },
			{ 0.5f, 1.0f, 0.5f }, { 0.5f, 1.0f, 0.75f }, { 0.5f, 1.0f, 1.0f }, { 0.5f, 0.75f, 1.0f },
			{ 0.5f, 0.5f, 1.0f }, { 0.75f, 0.5f, 1.0f }, { 1.0f, 0.5f, 1.0f }, { 1.0f, 0.5f, 0.75f } };

	

	public void initFX() {

		for (int i = 0; i < MAX_PARTICLES; i++) {
			int clr = (i + 1) / (MAX_PARTICLES / 12);
			double xi = ((Math.random() * 50) - 26) * 10;
			double yi = ((Math.random() * 50) - 25) * 10;
			double zi = yi;

			ResetParticle(i, clr, xi, yi, zi);
		}
	}

	public void draw() {

		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		
		// Enable smooth shading
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		// Depth buffer setup
		GlStateManager.clearDepth(1.0);
		// Enables Depth Testing
		GlStateManager.disableDepth();
		// Enable Blending
		GlStateManager.enableBlend();
		// Type Of Blending To Perform
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		// Really Nice Perspective Calculations
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		// Really Nice Point Smoothing
		GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_NICEST);
		// Enable Texture Mapping
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// Select Our Texture
		// glBindTexture( GL_TEXTURE_2D, myTexture )
		// Reset all the particles
		int i;
		double x, y, z;
		double xi, yi, zi;

		GlStateManager.loadIdentity();

		// Modify each of the particles
		for (i = 0; i < MAX_PARTICLES; i++) {
			if (particles[i].active) {
				x = particles[i].x; // Grab Our Particle X Position
				y = particles[i].y; // Grab Our Particle Y Position
				z = particles[i].z + zoom; // Particle Z Position + Zoom

				// Draw The Particle Using Our RGB Values,
				// Fade The Particle Based On It's Life
				GlStateManager.color(particles[i].r, particles[i].g, particles[i].b, particles[i].life);
				makeQuad();
				GlStateManager.loadIdentity();
				GlStateManager.translate(x, y, z);
				GlStateManager.callList(quadList);

				particles[i].x += particles[i].xi / slowdown * 1000; // Move on
																		// X
																		// Axis
																		// by X
																		// Speed
				particles[i].y += particles[i].yi / slowdown * 1000; // Move on
																		// Y
																		// Axis
																		// by Y
																		// Speed
				particles[i].z += particles[i].zi / slowdown * 1000; // Move on
																		// Z
																		// Axis
																		// by Z
																		// Speed

				particles[i].xi += particles[i].xg; // Take X Axis Pull into
													// account
				particles[i].yi += particles[i].yg; // Take Y Axis Pull into
													// account
				particles[i].zi += particles[i].zg; // Take Z Axis Pull into
													// account

				particles[i].life -= particles[i].fade; // Reduce life by Fade

				if (particles[i].life <= 0) {
					xi = xspeed + ((Math.random() * 60) - 32);
					yi = yspeed + ((Math.random() * 60) - 30);
					zi = ((Math.random() * 60) - 30);

					ResetParticle(i, col, xi, yi, zi);
				}
				Tessellator.getInstance().draw();
			}
		}
		
		
		
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	public void ResetParticle(int particle, int col, double xi, double yi, double zi) {
		particles[particle].active = true;
		particles[particle].life = 1;
		particles[particle].fade = (Math.random() * 100) / 1000 + 0.003;

		particles[particle].r = colors[col * 3][0];
		particles[particle].g = colors[col * 3][1];
		particles[particle].b = colors[col * 3][2];

		particles[particle].x = 0;
		particles[particle].y = 0;
		particles[particle].z = 0;

		particles[particle].xi = xi;
		particles[particle].yi = yi;
		particles[particle].zi = zi;

		particles[particle].xg = 0;
		particles[particle].yg = 0;
		particles[particle].zg = 0;
	}

	public void makeQuad() {
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		quadList = GlStateManager.glGenLists(1);
		GlStateManager.glNewList(quadList, GL11.GL_COMPILE);

		vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		// Top Right
		//vb.pos(0.5, 0.5, -10).tex(1, 1).endVertex();
		vb.pos(0.5, 0.5, -10).color(colors[1][0], colors[1][1], colors[1][2], 1).endVertex();
		// Top Left
		//vb.pos(-0.5, 0.5, -10).tex(0, 1).endVertex();
		vb.pos(-0.5, 0.5, -10).color(colors[1][0], colors[1][1], colors[1][2], 1).endVertex();
		// Bottom Right
		//vb.pos(0.5, -0.5, -10).tex(1, 0).endVertex();
		vb.pos(0.5, -0.5, -10).color(colors[1][0], colors[1][1], colors[1][2], 1).endVertex();
		// Bottom Left
		//vb.pos(-0.5, -0.5, -10).tex(0, 0).endVertex();
		vb.pos(-0.5,- 0.5, -10).color(colors[1][0], colors[1][1], colors[1][2], 1).endVertex();
		//vb.finishDrawing();
		GlStateManager.glEndList();
	}

	private class AnimaParticle {
		boolean active;
		float life;
		double fade;

		float r; // Red Value
		float g; // Green Value
		float b; // Blue Value

		double x; // X Position
		double y; // Y Position
		double z; // Z Position

		double xi; // X Direction
		double yi; // Y Direction
		double zi; // Z Direction

		double xg; // X Gravity
		double yg; // Y Gravity
		double zg; // Z Gravity
	}
}

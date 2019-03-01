package com.raphydaphy.arcanemagic.client.particle;

// Derived from Elucent's particle system in Embers
// Source (no longer available)
public interface ArcaneMagicParticle
{
	boolean alive();

	boolean isAdditive();

	boolean renderThroughBlocks();
}

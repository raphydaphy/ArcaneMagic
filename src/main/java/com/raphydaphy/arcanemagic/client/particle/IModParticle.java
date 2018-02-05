package com.raphydaphy.arcanemagic.client.particle;

public interface IModParticle {
	boolean alive();

	boolean isAdditive();
	
	boolean shouldRemove();
	
	void setToRemove();
}

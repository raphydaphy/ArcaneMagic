package com.raphydaphy.arcanemagic.common.anima;

import java.awt.Color;
import java.util.Random;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;

public final class AnimaCreation extends Anima {

	private static final Random RAND = new Random();

	public AnimaCreation() {
		super(ArcaneMagic.MODID + ".creation", Color.BLACK);
		setRegistryName(ArcaneMagic.MODID, "creation");
	}

	private static int getRandomColor(Random rand) {
		int r = rand.nextInt(256);
		int g = rand.nextInt(256);
		int b = rand.nextInt(256);
		int value = ((255 & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
		return value;
	}

	@Override
	public int getColorInt() {
		return getRandomColor(RAND);
	}

	@Override
	public Color getColor() {
		return new Color(getRandomColor(RAND));
	}

}

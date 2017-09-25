package com.raphydaphy.thaumcraft.data;

public enum EnumPrimal implements IPropertyEnum {
	AER(0xffff7e),
	AQUA(0x46b6d3),
	IGNIS(0xff5a01),
	TERRA(0x56c000),
	ORDO(0xff5a01),
	PERDITIO(0x404040);
	
	final int color;
	
	EnumPrimal(int color){
		this.color = color;
	}
	
	public int getColorMultiplier() {
		return color;
	}
}

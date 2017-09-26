package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.api.ThaumcraftAPI;
import com.raphydaphy.arcanemagic.wand.WandCap;
import com.raphydaphy.arcanemagic.wand.WandRod;

public class VanillaThaumcraftParts
{
	public static WandRod rod_wood = new WandRod("wood");
	public static WandRod rod_greatwood = new WandRod("greatwood");
	public static WandRod rod_silverwood = new WandRod("silverwood");

	public static WandCap cap_iron = new WandCap("iron");
	public static WandCap cap_gold = new WandCap("iron");
	public static WandCap cap_thaumium = new WandCap("thaumium");

	public static void registerWands()
	{
		ThaumcraftAPI.registerWandPart(rod_wood);
		ThaumcraftAPI.registerWandPart(rod_greatwood);
		ThaumcraftAPI.registerWandPart(rod_silverwood);

		ThaumcraftAPI.registerWandPart(cap_iron);
		ThaumcraftAPI.registerWandPart(cap_gold);
		ThaumcraftAPI.registerWandPart(cap_thaumium);
	}
}

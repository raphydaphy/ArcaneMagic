package com.raphydaphy.arcanemagic.init;

import com.raphydaphy.arcanemagic.api.ArcaneMagicAPI;
import com.raphydaphy.arcanemagic.scepter.ScepterTip;
import com.raphydaphy.arcanemagic.scepter.ScepterCore;

public class ScepterRegistry
{
	public static ScepterCore core_wood = new ScepterCore("wood");
	public static ScepterCore core_greatwood = new ScepterCore("greatwood");
	public static ScepterCore core_silverwood = new ScepterCore("silverwood");

	public static ScepterTip tip_iron = new ScepterTip("iron");
	public static ScepterTip tip_gold = new ScepterTip("iron");
	public static ScepterTip tip_thaumium = new ScepterTip("thaumium");

	public static void registerScepters()
	{
		ArcaneMagicAPI.registerScepterPart(core_wood);
		ArcaneMagicAPI.registerScepterPart(core_greatwood);
		ArcaneMagicAPI.registerScepterPart(core_silverwood);

		ArcaneMagicAPI.registerScepterPart(tip_iron);
		ArcaneMagicAPI.registerScepterPart(tip_gold);
		ArcaneMagicAPI.registerScepterPart(tip_thaumium);
	}
}

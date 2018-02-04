package com.raphydaphy.arcanemagic.common.data;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public interface IPropertyEnum extends IStringSerializable {

	@Override
	public default String getName() {
		return ((Enum<?>) this).name().toLowerCase(Locale.ROOT);
	}

}

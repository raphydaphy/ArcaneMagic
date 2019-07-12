package com.raphydaphy.arcanemagic.util;

import com.google.common.collect.ImmutableList;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArcaneMagicMixins implements IMixinConfigPlugin {
	public static final String PACKAGE = "com.raphydaphy.arcanemagic.core.client";

	//Optifine isn't kind to some of AM's visual mixins.
	public static final List<String> OPTIFINE_DISABLE = ImmutableList.of("ChunkRendererMixin");

	@Override
	public void onLoad(String mixinPackage) {
		if (!PACKAGE.equals(mixinPackage)) throw new IllegalArgumentException(mixinPackage);
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		for (String name : OPTIFINE_DISABLE) {
			if (mixinClassName.equals(PACKAGE+"."+name)) {
				boolean isPresent = FabricLoader.getInstance().isModLoaded("optifabric");
				if (isPresent) ArcaneMagic.getLogger().warn("[ArcaneMagic] OptiFabric is loaded. Some visuals may be affected!");
				return !isPresent;
			}
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}

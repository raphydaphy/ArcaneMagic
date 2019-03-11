package com.raphydaphy.arcanemagic.client.model;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class ArcaneModelLoader
{
	public static final ArcaneModelLoader INSTANCE = new ArcaneModelLoader();

	public static void registerModel(Identifier identifier, Function<ModelLoader, UnbakedModel> loader)
	{
		INSTANCE.doRegisterModel(identifier, loader);
	}

	private static final Logger LOGGER = LogManager.getLogger("ArcaneModelLoader");
	private static final Function<ModelLoader, UnbakedModel> NULL_SUPPLIER = (unused) -> null;

	private Map<Identifier, Function<ModelLoader, UnbakedModel>> loaders = new HashMap<>();

	private ArcaneModelLoader()
	{
	}

	private void doRegisterModel(Identifier identifier, Function<ModelLoader, UnbakedModel> loader)
	{
		if (loaders.containsKey(identifier))
		{
			LOGGER.warn("Identifier {} already registered!", identifier);
		}
		loaders.put(identifier, loader);
	}

	@Nullable
	public UnbakedModel tryLoad(Identifier modelIdentifier, ModelLoader vanillaLoader)
	{
		return loaders.getOrDefault(modelIdentifier, NULL_SUPPLIER).apply(vanillaLoader);
	}

}

package com.raphydaphy.arcanemagic.client.model;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class CustomJsonUnbakedModel extends JsonUnbakedModel {
	public CustomJsonUnbakedModel(Identifier parent, JsonUnbakedModel template, Map<String, String> newTextures, Function<Identifier, UnbakedModel> loader){
		super(parent, template.getElements(), newTextures, template.useAmbientOcclusion(), template.hasDepthInGui(), template.getTransformations(), Collections.emptyList());
		getTextureDependencies(loader, new HashSet<>());//resolve parent
	}
}

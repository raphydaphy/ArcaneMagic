package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.render.IronDaggerModel;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
	@Shadow protected abstract void putModel(Identifier identifier_1, UnbakedModel unbakedModel_1);

	@Inject(method = "loadModel", at=@At("HEAD"), cancellable = true)
	private void loadDaggerModel(Identifier modelId, CallbackInfo callbackInfo){
		if (modelId instanceof ModelIdentifier){
			ModelIdentifier modelIdentifier = (ModelIdentifier)modelId;
			if (ModRegistry.IRON_DAGGER_IDENTIFIER.equals(modelId) && modelIdentifier.getVariant().equals("inventory")){
				this.putModel(modelId, IronDaggerModel.MODEL);
				callbackInfo.cancel();
			}
		}
	}
}

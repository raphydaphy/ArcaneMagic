package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.model.ArcaneModelLoader;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void putModel(Identifier id, UnbakedModel unbakedModel);

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void loadDaggerModel(Identifier modelId, CallbackInfo callbackInfo) {
        UnbakedModel model = ArcaneModelLoader.INSTANCE.tryLoad(modelId, (ModelLoader) (Object) this);
        if (model != null) {
            this.putModel(modelId, model);
            callbackInfo.cancel();
        }
    }
}

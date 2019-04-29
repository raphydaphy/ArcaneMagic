package com.raphydaphy.arcanemagic.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils.ForgeCrystal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
@Environment(EnvType.CLIENT)
public class IronDaggerModel implements UnbakedModel
{
    private static final Identifier BASE = new Identifier(ArcaneMagic.DOMAIN, "item/iron_dagger");
    private static final Identifier BASE_MODEL = new Identifier(ArcaneMagic.DOMAIN, "item/iron_dagger");

    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    private UnbakedModel baseModel;

    public IronDaggerModel()
    {
    }

    @Override
    public Collection<Identifier> getModelDependencies()
    {
        return ImmutableList.of(BASE_MODEL);
    }

    @Override
    public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<String> var2)
    {
        ImmutableSet.Builder<Identifier> builder = ImmutableSet.builder();

        builder.add(BASE);
        baseModel = unbakedModelGetter.apply(BASE_MODEL);
        builder.addAll(baseModel.getTextureDependencies(unbakedModelGetter, var2)); //important to get it to resolve its parent -.-

        for (ForgeCrystal crystal : ForgeCrystal.values())
        {
            if (crystal != ForgeCrystal.EMPTY)
            {
                builder.add(crystal.hilt);
                builder.add(crystal.pommel);
            }
        }

        return builder.build();
    }

    @Override
    public BakedModel bake(ModelLoader loader, Function<Identifier, Sprite> bakedTextureGetter, ModelBakeSettings rotationContainer)
    {
        Map<CacheKey, BakedModel> variants = new HashMap<>();
        for (ForgeCrystal hilt : ForgeCrystal.values())
        {
            for (ForgeCrystal pommel : ForgeCrystal.values())
            {
                Map<String, String> newTextures = new HashMap<>();
                newTextures.put("layer0", BASE.toString());
                if (hilt != ForgeCrystal.EMPTY) newTextures.put("layer1", hilt.hilt.toString());
                if (pommel != ForgeCrystal.EMPTY) newTextures.put(hilt == ForgeCrystal.EMPTY ? "layer1" : "layer2", pommel.pommel.toString());
                CustomJsonUnbakedModel baseCopy = new CustomJsonUnbakedModel(BASE_MODEL, (JsonUnbakedModel) baseModel, newTextures, loader::getOrLoadModel);
                variants.put(new CacheKey(hilt, pommel), doBake(baseCopy, loader, bakedTextureGetter, rotationContainer));
            }
        }

        return new IronDaggerBakedModel(doBake(baseModel, loader, bakedTextureGetter, rotationContainer), variants);
    }

    // copied from net.minecraft.client.render.model.ModelLoader.bake because the generated models don't have ids
    private BakedModel doBake(UnbakedModel unbakedModel_1, ModelLoader loader, Function<Identifier, Sprite> bakedTextureGetter, ModelBakeSettings rotationContainer)
    {
        if (unbakedModel_1 instanceof JsonUnbakedModel)
        {
            JsonUnbakedModel jsonUnbakedModel_1 = (JsonUnbakedModel) unbakedModel_1;
            if (jsonUnbakedModel_1.getRootModel() == ModelLoader.GENERATION_MARKER)
            {
                return ITEM_MODEL_GENERATOR.create(bakedTextureGetter, jsonUnbakedModel_1).bake(loader, jsonUnbakedModel_1, bakedTextureGetter, rotationContainer);
            }
        }
        return unbakedModel_1.bake(loader, bakedTextureGetter, rotationContainer);
    }

    private static final class IronDaggerOverrideHandler extends ModelItemPropertyOverrideList
    {

        private final Map<CacheKey, BakedModel> cache;

        public IronDaggerOverrideHandler(Map<CacheKey, BakedModel> cache)
        {
            super(null, null, (u) -> null, Collections.emptyList());
            this.cache = cache;
        }

        @Override
        public BakedModel apply(BakedModel originalModel, ItemStack stack, World world, LivingEntity entity)
        {
            ArcaneMagicUtils.ForgeCrystal hilt = null;
            ArcaneMagicUtils.ForgeCrystal pommel = null;
            CacheKey key;

            CompoundTag tag = stack.getTag();
            if (tag != null)
            {
                hilt = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_ACTIVE_CRYSTAL_KEY));
                pommel = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.DAGGER_PASSIVE_CRYSTAL_KEY));
            }

            if (hilt == null || pommel == null)
            {
                return originalModel;
            }
            key = new CacheKey(hilt, pommel);

            if (cache.containsKey(key))
            {
                return cache.get(key);
            }

            return originalModel;
        }
    }

    protected static class IronDaggerBakedModel implements BakedModel
    {
        private final BakedModel base;
        private final IronDaggerOverrideHandler overrideHandler;

        public IronDaggerBakedModel(BakedModel base, Map<CacheKey, BakedModel> cache)
        {
            this.base = base;
            this.overrideHandler = new IronDaggerOverrideHandler(cache);
        }

        @Override
        public List<BakedQuad> getQuads(BlockState var1, Direction var2, Random var3)
        {
            return base.getQuads(var1, var2, var3);
        }

        @Override
        public boolean useAmbientOcclusion()
        {
            return base.useAmbientOcclusion();
        }

        @Override
        public boolean hasDepthInGui()
        {
            return base.hasDepthInGui();
        }

        @Override
        public boolean isBuiltin()
        {
            return false;
        }

        @Override
        public Sprite getSprite()
        {
            return base.getSprite();
        }

        @Override
        public ModelTransformation getTransformation()
        {
            return base.getTransformation();
        }

        @Override
        public ModelItemPropertyOverrideList getItemPropertyOverrides()
        {
            return overrideHandler;
        }
    }

    private static class CacheKey
    {
        public final ForgeCrystal hilt;
        public final ForgeCrystal pommel;

        private CacheKey(@Nonnull ForgeCrystal hilt, @Nonnull ForgeCrystal pommel)
        {
            this.hilt = hilt;
            this.pommel = pommel;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return hilt == cacheKey.hilt && pommel == cacheKey.pommel;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(hilt, pommel);
        }
    }
}
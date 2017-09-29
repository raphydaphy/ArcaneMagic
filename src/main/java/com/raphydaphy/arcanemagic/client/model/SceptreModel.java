package com.raphydaphy.arcanemagic.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.item.ItemScepter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Loads the Sceptre base model and handles retexturing it based on the Tip and Core in the ItemStack.
 */
public class SceptreModel implements IModel {
    private static final ResourceLocation SCEPTRE_BASE = new ResourceLocation(ArcaneMagic.MODID, "item/scepter_base");

    private static Class VanillaModelWrapper;
    private static Method bakeImpl;
    static {
        try {
            VanillaModelWrapper = Class.forName("net.minecraftforge.client.model.ModelLoader$VanillaModelWrapper");
            bakeImpl = ReflectionHelper.findMethod(VanillaModelWrapper, "bakeImpl", null, IModelState.class, VertexFormat.class, Function.class);
        } catch (Exception e){
            throw new RuntimeException("Could not get Vanilla loader wrappers", e);
        }
    }

    /**
     * Reflected reference to forge's VanillaModelWrapper to get around the cache.
     */
    private static IBakedModel bakeImpl(IModel model, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter){
        try {
            return (IBakedModel)bakeImpl.invoke(model, state, format,bakedTextureGetter);
        } catch (Exception e){
            throw new RuntimeException("Error invoking bakeImpl", e);
        }
    }

    private IModel sceptreBase;
    private boolean textured = false;

    public SceptreModel(){
        try {
            sceptreBase = ModelLoaderRegistry.getModel(SCEPTRE_BASE);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private SceptreModel(IModel base){
        this.sceptreBase = base;
        this.textured = true;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of(SCEPTRE_BASE);
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        return new SceptreModel(sceptreBase.retexture(textures));
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {

        return new BakedSceptre(format, bakedTextureGetter);
    }

    public class BakedSceptre implements IBakedModel{

        private VertexFormat format;
        private Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
        private IBakedModel bakedBase;

        public BakedSceptre(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter){
            this.format = format;
            this.bakedTextureGetter = bakedTextureGetter;
            if (SceptreModel.this.textured){
                SceptreModel.this.sceptreBase.getTextures();//ensure VanillaModelWrapper loads parents
                this.bakedBase = bakeImpl(SceptreModel.this.sceptreBase, SceptreModel.this.sceptreBase.getDefaultState(), format, bakedTextureGetter);
            }
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return this.bakedBase != null ? this.bakedBase.getQuads(state,side,rand) : ImmutableList.of();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return this.bakedBase != null && this.bakedBase.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return this.bakedBase != null && this.bakedBase.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return this.bakedBase != null && this.bakedBase.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return this.bakedBase != null ? this.bakedBase.getParticleTexture() : Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("missingno");
        }

        @Override
        public ItemOverrideList getOverrides() {
            return new ItemOverrideList(ImmutableList.of()){
                @Override
                public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                    if (stack.getItem() == ModRegistry.SCEPTER) {
                        ScepterPart tip = ItemScepter.getTip(stack);
                        ScepterPart core = ItemScepter.getCore(stack);
                        Pair<ScepterPart,ScepterPart> cacheKey = Pair.of(tip,core);
                        if (Loader.INSTANCE.cache.containsKey(cacheKey)){
                            return Loader.INSTANCE.cache.get(cacheKey);
                        }
                        ImmutableMap.Builder<String,String> tex = ImmutableMap.builder();
                        tex.put("tip", tip != null ? tip.getTexture().toString() : "missingno");
                        tex.put("core", core != null ? core.getTexture().toString() : "missingno");
                        IBakedModel generated = SceptreModel.this.retexture(tex.build()).bake(SceptreModel.this.getDefaultState(), BakedSceptre.this.format, BakedSceptre.this.bakedTextureGetter);
                        Loader.INSTANCE.cache.put(cacheKey, generated);
                        return generated;
                    }
                    return originalModel;//u wot?
                }
            };
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            return Pair.of(this, bakedBase.handlePerspective(cameraTransformType).getRight());
        }
    }

    public enum Loader implements ICustomModelLoader{
        INSTANCE;

        Map<Pair<ScepterPart,ScepterPart>,IBakedModel> cache = new HashMap<>();

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation instanceof ModelResourceLocation &&
                    modelLocation.getResourceDomain().equals(ArcaneMagic.MODID) &&
                    modelLocation.getResourcePath().equals("scepter") &&
                    ((ModelResourceLocation) modelLocation).getVariant().equals("inventory");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) throws Exception {
            return new SceptreModel();
        }


        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {
            cache.clear();
        }
    }
}

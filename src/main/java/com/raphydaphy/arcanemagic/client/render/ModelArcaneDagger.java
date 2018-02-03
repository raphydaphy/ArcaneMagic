package com.raphydaphy.arcanemagic.client.render;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.block.BlockArcaneForge.EnumForgeGem;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
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
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelArcaneDagger implements IModel {
	public static final IModel MODEL = new ModelArcaneDagger();

	private final ResourceLocation resourceBase = new ResourceLocation(ArcaneMagic.MODID, "items/iron_dagger");

	private final ResourceLocation resourceHilt;
	private final ResourceLocation resourcePommel;

	public ModelArcaneDagger() {
		this(null, null);
	}

	public ModelArcaneDagger(EnumForgeGem hilt, EnumForgeGem pommel) {

		if (hilt != null) {
			this.resourceHilt = new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/" + hilt + "_hilt");
		} else {
			resourceHilt = null;
		}

		if (pommel != null) {
			this.resourcePommel = new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/" + pommel + "_pommel");
		} else {
			resourcePommel = null;
		}
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/iron_dagger"));

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/depth_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/depth_pommel"));

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/horizon_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/horizon_pommel"));

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/inferno_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/inferno_pommel"));

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/ozone_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/ozone_pommel"));
		
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/peace_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/peace_pommel"));

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/chaos_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/chaos_pommel"));
		
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/diamond_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/diamond_pommel"));

		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/emerald_hilt"));
		builder.add(new ResourceLocation(ArcaneMagic.MODID, "items/weapon_gems/emerald_pommel"));

		return builder.build();
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData) {

		EnumForgeGem hilt = EnumForgeGem.getFromName(customData.get(EnumForgeGem.HILT_NBT));
		EnumForgeGem pommel = EnumForgeGem.getFromName(customData.get(EnumForgeGem.POMMEL_NBT));

		return new ModelArcaneDagger(hilt, pommel);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
		TextureAtlasSprite baseSprite = bakedTextureGetter.apply(this.resourceBase);
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

		IBakedModel baseModel = (new ItemLayerModel(ImmutableList.of(this.resourceBase))).bake(state, format,
				bakedTextureGetter);
		builder.addAll(baseModel.getQuads(null, null, 0));

		if (this.resourceHilt != null) {
			IBakedModel model = (new ItemLayerModel(ImmutableList.of(this.resourceHilt))).bake(state, format,
					bakedTextureGetter);
			builder.addAll(model.getQuads(null, null, 0));
		}

		if (this.resourcePommel != null) {
			IBakedModel model = (new ItemLayerModel(ImmutableList.of(this.resourcePommel))).bake(state, format,
					bakedTextureGetter);
			builder.addAll(model.getQuads(null, null, 0));
		}

		return new BakedArcaneDagger(this, builder.build(), baseSprite, format, Maps.immutableEnumMap(transformMap),
				Maps.<String, IBakedModel>newHashMap());
	}

	private static final class BakedArcaneDaggerOverrideHandler extends ItemOverrideList {
		public static final BakedArcaneDaggerOverrideHandler INSTANCE = new BakedArcaneDaggerOverrideHandler();

		private BakedArcaneDaggerOverrideHandler() {
			super(ImmutableList.<ItemOverride>of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModelIn, ItemStack stack, World world,
				EntityLivingBase entity) {
			boolean isTool = stack.getItem() == ModRegistry.ARCANE_DAGGER;
			EnumForgeGem hilt = null;
			EnumForgeGem pommel = null;
			String key;

			if (isTool && stack.hasTagCompound()) {
				hilt = EnumForgeGem.getFromName(stack.getTagCompound().getString(EnumForgeGem.HILT_NBT));
				pommel = EnumForgeGem.getFromName(stack.getTagCompound().getString(EnumForgeGem.POMMEL_NBT));
			}

			key = "" + hilt + pommel;
			BakedArcaneDagger originalModel = (BakedArcaneDagger) originalModelIn;

			if (originalModel.cache.containsKey(key) == false) {
				ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
				map.put(EnumForgeGem.HILT_NBT, hilt + "");
				map.put(EnumForgeGem.POMMEL_NBT, pommel + "");

				IModel model = originalModel.parent.process(map.build());

				Function<ResourceLocation, TextureAtlasSprite> textureGetter;
				textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
					public TextureAtlasSprite apply(ResourceLocation location) {
						return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
					}
				};

				IBakedModel bakedModel = model.bake(new SimpleModelState(originalModel.transforms),
						originalModel.format, textureGetter);
				originalModel.cache.put(key, bakedModel);

				return bakedModel;
			}

			return originalModel.cache.get(key);
		}
	}

	protected static class BakedArcaneDagger implements IBakedModel {
		private final ModelArcaneDagger parent;
		private final Map<String, IBakedModel> cache;
		private final ImmutableMap<TransformType, TRSRTransformation> transforms;
		private final ImmutableList<BakedQuad> quads;
		private final TextureAtlasSprite particle;
		private final VertexFormat format;

		public BakedArcaneDagger(ModelArcaneDagger parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle,
				VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
				Map<String, IBakedModel> cache) {
			this.quads = quads;
			this.particle = particle;
			this.format = format;
			this.parent = parent;
			this.transforms = transforms;
			this.cache = cache;
		}

		@Override
		public ItemOverrideList getOverrides() {
			return BakedArcaneDaggerOverrideHandler.INSTANCE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
			return PerspectiveMapWrapper.handlePerspective(this, this.transforms, cameraTransformType);
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			if (side == null)
				return quads;
			return ImmutableList.of();
		}

		public boolean isAmbientOcclusion() {
			return true;
		}

		public boolean isGui3d() {
			return false;
		}

		public boolean isBuiltInRenderer() {
			return false;
		}

		public TextureAtlasSprite getParticleTexture() {
			return particle;
		}

		public ItemCameraTransforms getItemCameraTransforms() {
			return ItemCameraTransforms.DEFAULT;
		}
	}

	public enum LoaderArcaneDagger implements ICustomModelLoader {
		instance;

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getResourceDomain().equals(ArcaneMagic.MODID)
					&& modelLocation.getResourcePath().contains("generated_model_arcane_dagger");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws IOException {
			return MODEL;
		}

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
		}
	}
}

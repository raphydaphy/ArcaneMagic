package com.raphydaphy.arcanemagic.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.raphydaphy.arcanemagic.Thaumcraft;
import com.raphydaphy.arcanemagic.api.ThaumcraftAPI;
import com.raphydaphy.arcanemagic.api.wand.IWandCap;
import com.raphydaphy.arcanemagic.api.wand.IWandRod;
import com.raphydaphy.arcanemagic.init.VanillaThaumcraftParts;
import com.raphydaphy.arcanemagic.item.ItemWand;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

public class ModelWand implements IModel
{
	private static IModel basicWand = new ModelWand(VanillaThaumcraftParts.cap_iron, VanillaThaumcraftParts.rod_wood);
	private final IWandCap cap;
	private final IWandRod rod;

	public ModelWand(IWandCap cap, IWandRod rod)
	{
		this.rod = rod;
		this.cap = cap;

		if (rod == null)
		{
			rod = VanillaThaumcraftParts.rod_greatwood;
		}
		if (cap == null)
		{
			cap = VanillaThaumcraftParts.cap_gold;
		}

		System.out.println(
				"Making new wand with rod " + rod.getUnlocalizedName() + " and cap " + cap.getUnlocalizedName());
	}

	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{

		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
		for (IWandCap cap : ThaumcraftAPI.WAND_CAPS.values())
		{

			builder.add(cap.getTexture());
		}

		for (IWandRod rod : ThaumcraftAPI.WAND_RODS.values())
		{

			builder.add(rod.getTexture());
		}
		return builder.build();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
		TextureAtlasSprite coreSprite = null;
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

		if (cap != null)
		{
			IBakedModel modelCap = (new ItemLayerModel(ImmutableList.of(this.cap.getTexture()))).bake(state, format,
					bakedTextureGetter);
			builder.addAll(modelCap.getQuads(null, null, 0));
		} else
		{
			System.out.println("CAP IS NULL THIS IS BAD REPORT PLZ THANKS AND HELP ME FIX");
		}

		if (rod != null)
		{
			System.out.println("Adding rod to baked model");
			coreSprite = bakedTextureGetter.apply(this.rod.getTexture());
			IBakedModel modelRod = (new ItemLayerModel(ImmutableList.of(this.rod.getTexture()))).bake(state, format,
					bakedTextureGetter);
			builder.addAll(modelRod.getQuads(null, null, 0));
		} else
		{
			System.out.println("ROD IS NULL THIS IS BAD REPORT PLZ THANKS AND HELP ME FIX!");
		}
		if (rod != null)
		{
			return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager()
					.getModel(new ModelResourceLocation(
							new ResourceLocation(Thaumcraft.MODID, "models/item/generated_model_wand"), "inventory"));
		} else
		{
			return new BakedWand(this, builder.build(), coreSprite, format, Maps.<String, IBakedModel>newHashMap(),
					Maps.immutableEnumMap(transformMap));
		}
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData)
	{
		IWandRod rod = VanillaThaumcraftParts.rod_wood;
		IWandCap cap = VanillaThaumcraftParts.cap_iron;
		try
		{
			rod = ThaumcraftAPI.WAND_RODS.get(customData.get(ItemWand.KEY_ROD));
		} catch (Exception e)
		{
			System.out.println("IT DIDNT WORK!");
		}
		try
		{
			cap = ThaumcraftAPI.WAND_CAPS.get(customData.get(ItemWand.KEY_CAP));
		} catch (Exception e)
		{
			System.out.println("iT REALLY DIDNT WORK");
		}

		// System.out.println("processing new wand model with parts " +
		// rod.getUnlocalizedName() + ", " + cap.getUnlocalizedName());
		return new ModelWand(cap, rod);
	}

	private static final class BakedWandOverrideHandler extends ItemOverrideList
	{

		public static final BakedWandOverrideHandler INSTANCE = new BakedWandOverrideHandler();

		private BakedWandOverrideHandler()
		{
			super(ImmutableList.<ItemOverride>of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModelIn, ItemStack stack, World world,
				EntityLivingBase entity)
		{

			IWandRod rodType = ItemWand.getRod(stack);
			IWandCap capType = ItemWand.getCap(stack);

			String key = rodType.getUnlocalizedName() + capType.getUnlocalizedName();

			BakedWand originalModel = (BakedWand) originalModelIn;

			if (originalModel.cache.containsKey(key) == false)
			{
				ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
				map.put(ItemWand.KEY_ROD, rodType.getUnlocalizedName());
				map.put(ItemWand.KEY_CAP, capType.getUnlocalizedName());

				IModel model = originalModel.parent.process(map.build());

				Function<ResourceLocation, TextureAtlasSprite> textureGetter;
				textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
				{
					public TextureAtlasSprite apply(ResourceLocation location)
					{
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

	protected static class BakedWand implements IBakedModel
	{
		private final ModelWand parent;
		private final Map<String, IBakedModel> cache;
		private final ImmutableList<BakedQuad> quads;
		private final TextureAtlasSprite particle;
		private final VertexFormat format;
		private final ImmutableMap<TransformType, TRSRTransformation> transforms;

		public BakedWand(ModelWand parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle,
				VertexFormat format, Map<String, IBakedModel> cache,
				ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms)
		{
			System.out.println("Making the actual baked wand");
			this.quads = quads;
			this.particle = particle;
			this.format = format;
			this.parent = parent;
			this.cache = cache;
			this.transforms = transforms;
		}

		@Override
		public ItemOverrideList getOverrides()
		{
			return BakedWandOverrideHandler.INSTANCE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
		{
			if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND)
			{
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(-0.25f, 0.6f, 0),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 49.9f)), new Vector3f(0.90f, 0.90f, 0.90f),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 49.9f)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
			{
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(0.5f, 0f, 0),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 0, 22)),
						new Vector3f(0.90f, 0.90f, 0.90f),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 0, 22)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND)
			{
				// Vector3f(left/right, forwards,back, up/down)
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(-0.05f, 0.8f, 1.2f),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(340f, 230, 310f)),
						new Vector3f(0.85f, 0.85f, 0.85f),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(340f, 200, 280f)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
			{
				// Vector3f(left/right, forwards,back, up/down)
				// quatFromXYZDegrees(?, ?, tilt left/right)
				// TRSRTransformation tr = new TRSRTransformation(new
				// Vector3f(0f,0f,0f), TRSRTransformation.quatFromXYZDegrees(new
				// Vector3f(0f,0,0f)), new Vector3f(0.85f,0.85f,0.85f),
				// TRSRTransformation.quatFromXYZDegrees(new
				// Vector3f(0f,0,0f)));
				// Matrix4f mat = null;
				// if(tr != null && !tr.equals(TRSRTransformation.identity()))
				// mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				// return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.GROUND)
			{
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(0, 0, 0f),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 0)), new Vector3f(0.85f, 0.85f, 0.85f),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 0)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			}
			return PerspectiveMapWrapper.handlePerspective(this, this.transforms, cameraTransformType);
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			if (side == null)
				return quads;
			return ImmutableList.of();
		}

		public boolean isAmbientOcclusion()
		{
			return true;
		}

		public boolean isGui3d()
		{
			return false;
		}

		public boolean isBuiltInRenderer()
		{
			return false;
		}

		public TextureAtlasSprite getParticleTexture()
		{
			return particle;
		}

		public ItemCameraTransforms getItemCameraTransforms()
		{
			return ItemCameraTransforms.DEFAULT;
		}
	}

	public enum ModelWandLoader implements ICustomModelLoader
	{

		instance;

		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			boolean canAccept = modelLocation.getResourceDomain().equals(Thaumcraft.MODID)
					&& modelLocation.getResourcePath().contains("generated_model_wand");
			if (canAccept)
			{
				System.out.println("Accepted " + modelLocation.toString());
			}
			return canAccept;
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws IOException
		{
			return basicWand;
		}

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			// no need to clear cache since we create a new model instance
		}

	}

	private static class CompositeBakedModel implements IBakedModel
	{

		private final IBakedModel cap;
		private final IBakedModel rod;
		private final List<BakedQuad> genQuads;
		private final Map<EnumFacing, List<BakedQuad>> faceQuads = new EnumMap<>(EnumFacing.class);

		CompositeBakedModel(IBakedModel cap, IBakedModel rod)
		{
			this.cap = cap;
			this.rod = rod;

			ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
			final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(
					new TRSRTransformation(new Vector3f(-0.4F, 0.25F, 0), null, new Vector3f(0.625F, 0.625F, 0.625F),
							TRSRTransformation.quatFromXYZ(0, (float) Math.PI / 2, 0)));

			for (EnumFacing e : EnumFacing.VALUES)
			{
				faceQuads.put(e, new ArrayList<>());
			}

			genBuilder.addAll(rod.getQuads(null, null, 0));
			for (EnumFacing e : EnumFacing.VALUES)
			{
				faceQuads.get(e).addAll(rod.getQuads(null, e, 0));
			}

			genBuilder.addAll(cap.getQuads(null, null, 0));
			for (EnumFacing e : EnumFacing.VALUES)
			{
				faceQuads.get(e).addAll(cap.getQuads(null, e, 0));
			}

			genQuads = genBuilder.build();

		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand)
		{
			return face == null ? genQuads : faceQuads.get(face);
		}

		// Forward all to gun model
		@Override
		public boolean isAmbientOcclusion()
		{
			return rod.isAmbientOcclusion() || cap.isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d()
		{
			return rod.isGui3d() || cap.isGui3d();
		}

		@Override
		public boolean isBuiltInRenderer()
		{
			return rod.isBuiltInRenderer() || cap.isBuiltInRenderer();
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			return rod.getParticleTexture();
		}

		@Nonnull
		@Override
		public ItemCameraTransforms getItemCameraTransforms()
		{
			return rod.getItemCameraTransforms();
		}

		@Nonnull
		@Override
		public ItemOverrideList getOverrides()
		{
			return BakedWandOverrideHandler.INSTANCE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
				@Nonnull ItemCameraTransforms.TransformType cameraTransformType)
		{
			Pair<? extends IBakedModel, Matrix4f> pair = rod.handlePerspective(cameraTransformType);
			if (pair != null && pair.getRight() != null)
				return Pair.of(this, pair.getRight());
			return Pair.of(this, TRSRTransformation.identity().getMatrix());
		}
	}

}
package com.raphydaphy.thaumcraft.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.raphydaphy.thaumcraft.Thaumcraft;
import com.raphydaphy.thaumcraft.item.ItemWand;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelWand implements IBakedModel {
	IBakedModel basicWand = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(Thaumcraft.MODID, "models/items/wand"), "inventory"));

	private final ItemOverrideList itemHandler = new ItemOverrideList(ImmutableList.of()) {
		@Nonnull
		@Override
		public IBakedModel handleItemState(@Nonnull IBakedModel model, ItemStack stack, World world, EntityLivingBase entity) {
			ResourceLocation cap = ItemWand.getCap(stack);
			ResourceLocation rod = ItemWand.getRod(stack);
			if(cap != null) {
				IBakedModel capModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(Thaumcraft.MODID, "models/items/wand_cap"), "inventory"));
				IBakedModel rodModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(Thaumcraft.MODID, "models/items/wand_rod"), "inventory"));
				return ModelWand.this.getModel(cap.toString() + rod.toString(), capModel, rodModel);
			}
			else return ModelWand.this;
		}
	};

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	@Nonnull @Override public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) { return basicWand.getQuads(state, side, rand); }
	@Override public boolean isAmbientOcclusion() { return basicWand.isAmbientOcclusion(); }
	@Override public boolean isGui3d() { return basicWand.isGui3d(); }
	@Override public boolean isBuiltInRenderer() { return basicWand.isBuiltInRenderer(); }
	@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return basicWand.getParticleTexture(); }
	@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return basicWand.getItemCameraTransforms(); }

	private final IdentityHashMap<String, CompositeBakedModel> cache = new IdentityHashMap<>();

	private CompositeBakedModel getModel(String name, IBakedModel cap, IBakedModel rod) {
		CompositeBakedModel model = cache.get(name);
		if(model == null) {
			model = new CompositeBakedModel(cap, rod);
			cache.put(name, model);
		}
		return model;
	}

	protected static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform) {
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
		final IVertexConsumer consumer = new VertexTransformer(builder) {
			@Override
			public void put(int element, float... data) {
				VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
				switch(formatElement.getUsage()) {
				case POSITION: {
					float[] newData = new float[4];
					Vector4f vec = new Vector4f(data);
					transform.getMatrix().transform(vec);
					vec.get(newData);
					parent.put(element, newData);
					break;
				}
				default: {
					parent.put(element, data);
					break;
				}
				}
			}
		};
		quad.pipe(consumer);
		return builder.build();
	}

	private static class CompositeBakedModel implements IBakedModel {

		private final IBakedModel gun;
		private final List<BakedQuad> genQuads;
		private final Map<EnumFacing, List<BakedQuad>> faceQuads = new EnumMap<>(EnumFacing.class);

		CompositeBakedModel(IBakedModel lens, IBakedModel gun) {
			this.gun = gun;

			ImmutableList.Builder<BakedQuad> genBuilder = ImmutableList.builder();
			final TRSRTransformation transform = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(-0.4F, 0.25F, 0), null, new Vector3f(0.625F, 0.625F, 0.625F), TRSRTransformation.quatFromXYZ(0, (float) Math.PI / 2, 0)));

			for(EnumFacing e : EnumFacing.VALUES)
				faceQuads.put(e, new ArrayList<>());

			// Add lens quads, scaled and translated
			for(BakedQuad quad : lens.getQuads(null, null, 0)) {
				genBuilder.add(transform(quad, transform));
			}

			for(EnumFacing e : EnumFacing.VALUES) {
				faceQuads.get(e).addAll(lens.getQuads(null, e, 0).stream().map(input -> transform(input, transform)).collect(Collectors.toList()));
			}

			// Add gun quads
			genBuilder.addAll(gun.getQuads(null, null, 0));
			for(EnumFacing e : EnumFacing.VALUES) {
				faceQuads.get(e).addAll(gun.getQuads(null, e, 0));
			}

			genQuads = genBuilder.build();

		}

		@Nonnull @Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing face, long rand) { return face == null ? genQuads : faceQuads.get(face); }

		// Forward all to gun model
		@Override public boolean isAmbientOcclusion() { return gun.isAmbientOcclusion(); }
		@Override public boolean isGui3d() { return gun.isGui3d(); }
		@Override public boolean isBuiltInRenderer() { return gun.isBuiltInRenderer(); }
		@Nonnull @Override public TextureAtlasSprite getParticleTexture() { return gun.getParticleTexture();}
		@Nonnull @Override public ItemCameraTransforms getItemCameraTransforms() { return gun.getItemCameraTransforms(); }
		@Nonnull @Override public ItemOverrideList getOverrides() { return ItemOverrideList.NONE; }

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
			Pair<? extends IBakedModel, Matrix4f> pair = gun.handlePerspective(cameraTransformType);
			if(pair != null && pair.getRight() != null)
				return Pair.of(this, pair.getRight());
			return Pair.of(this, TRSRTransformation.identity().getMatrix());
		}
	}

}
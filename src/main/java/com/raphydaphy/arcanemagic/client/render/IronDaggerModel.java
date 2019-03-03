package com.raphydaphy.arcanemagic.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelItemOverride;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
@Environment(EnvType.CLIENT)
public class IronDaggerModel implements UnbakedModel
{
	public static final UnbakedModel MODEL = new IronDaggerModel();
	private static final Identifier BASE = new Identifier(ArcaneMagic.DOMAIN, "items/iron_dagger");

	private final Identifier hilt;
	private final Identifier pommel;

	public IronDaggerModel()
	{
		this(null, null);
	}

	public IronDaggerModel(ArcaneMagicUtils.ForgeCrystal hilt, ArcaneMagicUtils.ForgeCrystal pommel)
	{
		if (hilt != null)
		{
			this.hilt = hilt.hilt;
		} else
		{
			this.hilt = null;
		}

		if (pommel != null)
		{
			this.pommel = pommel.pommel;
		} else
		{
			this.pommel = null;
		}
	}

	@Override
	public Collection<Identifier> getModelDependencies()
	{
		return ImmutableList.of();
	}

	@Override
	public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> var1, Set<String> var2)
	{
		ImmutableSet.Builder<Identifier> builder = ImmutableSet.builder();

		builder.add(BASE);

		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/coal_hilt"));
		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/coal_pommel"));

		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/diamond_hilt"));
		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/diamond_pommel"));

		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/emerald_hilt"));
		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/emerald_pommel"));

		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/gold_hilt"));
		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/gold_pommel"));

		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/lapis_hilt"));
		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/lapis_pommel"));

		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/redstone_hilt"));
		builder.add(new Identifier(ArcaneMagic.DOMAIN, "items/weapon_gems/redstone_pommel"));

		return builder.build();
	}

	public IronDaggerModel process(ImmutableMap<String, String> customData)
	{
		ArcaneMagicUtils.ForgeCrystal hilt = ArcaneMagicUtils.ForgeCrystal.getFromID(customData.get(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
		ArcaneMagicUtils.ForgeCrystal pommel = ArcaneMagicUtils.ForgeCrystal.getFromID(customData.get(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));

		return new IronDaggerModel(hilt, pommel);
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<Identifier, Sprite> bakedTextureGetter, ModelRotationContainer rotationContainer)
	{
		Sprite baseSprite = bakedTextureGetter.apply(BASE);
		ImmutableList.Builder builder = ImmutableList.builder();

		return null;
	}

	private static final class IronDaggerOverrideHandler extends ModelItemPropertyOverrideList
	{

		public IronDaggerOverrideHandler(ModelLoader modelLoader_1, JsonUnbakedModel jsonUnbakedModel_1, Function<Identifier, UnbakedModel> function_1, List<ModelItemOverride> list_1)
		{
			super(modelLoader_1, jsonUnbakedModel_1, function_1, list_1);
		}

		@Override
		public BakedModel apply(BakedModel originalModel, ItemStack stack, World world, LivingEntity entity)
		{
			ArcaneMagicUtils.ForgeCrystal hilt = null;
			ArcaneMagicUtils.ForgeCrystal pommel = null;
			String key;

			CompoundTag tag = stack.getTag();
			if (tag != null)
			{
				hilt = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY));
				pommel = ArcaneMagicUtils.ForgeCrystal.getFromID(tag.getString(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY));
			}

			key = "" + hilt + pommel;

			if (!((IronDaggerBakedModel) originalModel).cache.containsKey(key))
			{
				ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
				map.put(ArcaneMagicConstants.ACTIVE_CRYSTAL_KEY, hilt + "");
				map.put(ArcaneMagicConstants.PASSIVE_CRYSTAL_KEY, pommel + "");

				IronDaggerModel model = ((IronDaggerBakedModel) originalModel).parent.process(map.build());
				Function<Identifier, Sprite> textureGetter = id -> MinecraftClient.getInstance().getSpriteAtlas().getSprite(id);

				//IronDaggerBakedModel bakedModel = model.bake()

			}

			return ((IronDaggerBakedModel) originalModel).cache.get(key);
		}
	}

	protected static class IronDaggerBakedModel implements BakedModel
	{
		private final IronDaggerModel parent;
		private final Sprite particle;
		private final Map<String, BakedModel> cache;

		public IronDaggerBakedModel(IronDaggerModel parent, Sprite particle, Map<String, BakedModel> cache)
		{
			this.parent = parent;
			this.particle = particle;
			this.cache = cache;
		}

		@Override
		public List<BakedQuad> getQuads(BlockState var1, Direction var2, Random var3)
		{
			return null;
		}

		@Override
		public boolean useAmbientOcclusion()
		{
			return true;
		}

		@Override
		public boolean hasDepthInGui()
		{
			return false;
		}

		@Override
		public boolean isBuiltin()
		{
			return false;
		}

		@Override
		public Sprite getSprite()
		{
			return particle;
		}

		@Override
		public ModelTransformation getTransformation()
		{
			return ModelTransformation.NONE;
		}

		@Override
		public ModelItemPropertyOverrideList getItemPropertyOverrides()
		{
			return null;
		}
	}
}
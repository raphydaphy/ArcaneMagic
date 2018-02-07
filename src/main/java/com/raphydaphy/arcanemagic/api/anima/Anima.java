package com.raphydaphy.arcanemagic.api.anima;

import java.awt.Color;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.anima.AnimaCreation;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.network.PacketAnimaTransfer;

import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Anima extends IForgeRegistryEntry.Impl<Anima>
{

	public static final AnimaRegistry REGISTRY = new AnimaRegistry();

	public static final Anima OZONE = new Anima("ozone", ArcaneMagic.MODID + ".ozone", 0xea, 0xea, 0xea);
	public static final Anima DEPTH = new Anima("depth", ArcaneMagic.MODID + ".depth", 0x18, 0x7e, 0xa3);
	public static final Anima INFERNO = new Anima("inferno", ArcaneMagic.MODID + ".inferno", 0xbf, 0x56, 0);
	public static final Anima HORIZON = new Anima("horizon", ArcaneMagic.MODID + ".horizon", 0x06, 0x60, 0x18);
	public static final Anima PEACE = new Anima("peace", ArcaneMagic.MODID + ".peace", 0x30, 0x3a, 0x1f);
	public static final Anima CHAOS = new Anima("chaos", ArcaneMagic.MODID + ".chaos", 0x85, 0xb7, 0x2f);
	public static final Anima CREATION = new AnimaCreation();

	public static class AnimaSubscriber
	{

		@SubscribeEvent
		public void onAnimaRegister(Register<Anima> event)
		{
			event.getRegistry().registerAll(OZONE, DEPTH, INFERNO, HORIZON, PEACE, CHAOS, CREATION);
			OZONE.setItemForm(new ItemStack(ModRegistry.ANIMA));
			DEPTH.setItemForm(new ItemStack(ModRegistry.ANIMA, 1, 1));
			INFERNO.setItemForm(new ItemStack(ModRegistry.ANIMA, 1, 2));
			HORIZON.setItemForm(new ItemStack(ModRegistry.ANIMA, 1, 3));
			PEACE.setItemForm(new ItemStack(ModRegistry.ANIMA, 1, 4));
			CHAOS.setItemForm(new ItemStack(ModRegistry.ANIMA, 1, 5));
			CREATION.setItemForm(new ItemStack(ModRegistry.CREATION));
		}
	}

	private final Color color;
	private String unlocName;
	private String indexName;
	protected ItemStack itemForm = ItemStack.EMPTY;

	private Anima(String name, String unlocName, int r, int g, int b)
	{
		this(name, unlocName, new Color(r, g, b));
	}

	private Anima(String name, String unlocName, Color color)
	{
		setRegistryName(ArcaneMagic.MODID, name);
		setUnlocalizedName(unlocName);
		this.indexName = name;
		this.color = color;
	}

	public Anima(String unlocName, Color color)
	{
		setUnlocalizedName(unlocName);
		this.color = color;
		this.indexName = unlocName;
	}

	public Anima(Color color)
	{
		this.color = color;
		this.indexName = "unnamed";
	}

	public Anima setIndexName(String index)
	{
		this.indexName = index;
		return this;
	}

	public Anima setUnlocalizedName(String unloc)
	{
		this.unlocName = unloc;
		return this;
	}

	public Anima setItemForm(ItemStack stack)
	{
		if (itemForm.isEmpty())
			itemForm = stack;
		return this;
	}

	public static boolean sendAnima(World world, AnimaStack stack, Vec3d from, Vec3d to, boolean simulate,
			boolean spawnParticles)
	{
		return sendAnima(world, stack, from, to, to, simulate, spawnParticles);
	}

	public static Anima getOpposite(Anima anima)
	{
		if (anima.equals(INFERNO))
			return DEPTH;
		else if (anima.equals(DEPTH))
			return INFERNO;
		else if (anima.equals(CHAOS))
			return PEACE;
		else if (anima.equals(PEACE))
			return CHAOS;
		else if (anima.equals(HORIZON))
			return OZONE;
		else if (anima.equals(OZONE))
			return HORIZON;
		else
			return null;

	}
	
	public static int getNum(Anima anima)
	{
		if (anima.equals(HORIZON))
			return 0;
		else if (anima.equals(OZONE))
			return 1;
		else if (anima.equals(INFERNO))
			return 2;
		else if (anima.equals(DEPTH))
			return 3;
		else if (anima.equals(PEACE))
			return 4;
		else if (anima.equals(CHAOS))
			return 5;
		else
			return 6;

	}

	public static boolean sendAnima(World world, AnimaStack stack, Vec3d from, Vec3d to, Vec3d toCosmetic,
			boolean simulate, boolean spawnParticles)
	{
		BlockPos fromPos = new BlockPos(Math.floor(from.x), Math.floor(from.y), Math.floor(from.z));
		BlockPos toPos = new BlockPos(Math.floor(to.x), Math.floor(to.y), Math.floor(to.z));

		TileEntity fromTEUnchecked = world.getTileEntity(fromPos);
		TileEntity toTEUnchecked = world.getTileEntity(toPos);

		if (toTEUnchecked != null && toTEUnchecked.hasCapability(IAnimaStorage.CAP, null))
		{
			IAnimaStorage storage = toTEUnchecked.getCapability(IAnimaStorage.CAP, null);
			if (storage != null)
			{
				if (fromTEUnchecked != null && fromTEUnchecked.hasCapability(IAnimaStorage.CAP, null))
				{

					IAnimaStorage storageFrom = fromTEUnchecked.getCapability(IAnimaStorage.CAP, null);
					// sending from block to block, such as concentrator ->
					// crystallizer
					if (storageFrom != null)
					{
						// sender block has enough anima to transfer it
						if (storageFrom.take(stack, true) == null)
						{
							// recieving block has enough capacity to accept it
							if (storage.store(stack, true) == null)
							{
								if (!simulate)
								{

									if (!world.isRemote)
									{
										// send and recieve anima
										storage.store(stack, false);
										storageFrom.take(stack, false);

										toTEUnchecked.markDirty();
										fromTEUnchecked.markDirty();

										ArcaneMagicPacketHandler.INSTANCE.sendToAll(
												new PacketAnimaTransfer(stack, from, to, toCosmetic, spawnParticles));
									} else if (spawnParticles)
									{
										ArcaneMagic.proxy.spawnAnimaParticles(world, from, new Vec3d(0, 0, 0),
												stack.getAnima(), toCosmetic, false);
									}
								}
								return true;
							}
						}
					}
				}
				// sending from bedrock/wand to block
				else
				{
					// if the receiving block can accept 100% of the animus
					if (storage.store(stack, true) == null)
					{
						if (!simulate)
						{
							if (!world.isRemote)
							{
								// do the thing!
								storage.store(stack, false);

								toTEUnchecked.markDirty();

								ArcaneMagicPacketHandler.INSTANCE.sendToAll(
										new PacketAnimaTransfer(stack, from, to, toCosmetic, spawnParticles));
							} else
							{
								ArcaneMagic.proxy.spawnAnimaParticles(world, from, new Vec3d(0, 0, 0), stack.getAnima(),
										toCosmetic, to.equals(toCosmetic) ? false : true);

							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public ItemStack getItemForm()
	{
		return itemForm.copy();
	}

	public String getUnlocalizedName()
	{
		return "anima." + unlocName;
	}

	public String getIndexName()
	{
		return this.indexName;
	}

	public String getTranslationName()
	{
		return getUnlocalizedName() + ".name";
	}

	public int getColorInt()
	{
		return color.getRGB();
	}

	public Color getColor()
	{
		return color;
	}

	public static Anima getAnimaByID(int id)
	{
		if (id < Anima.REGISTRY.getValues().size())
		{
			return Anima.REGISTRY.getValues().get(id);
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "Anima: " + getRegistryName().toString();
	}

	public static Anima getFromBiome(Biome biome)
	{
		if (BiomeDictionary.hasType(biome, Type.WATER))
			return Anima.DEPTH;
		else if (BiomeDictionary.hasType(biome, Type.FOREST) || BiomeDictionary.hasType(biome, Type.PLAINS))
			return Anima.HORIZON;
		else if (BiomeDictionary.hasType(biome, Type.DRY) || BiomeDictionary.hasType(biome, Type.NETHER)
				|| BiomeDictionary.hasType(biome, Type.HOT))
			return Anima.INFERNO;
		else if (BiomeDictionary.hasType(biome, Type.HILLS) || BiomeDictionary.hasType(biome, Type.MOUNTAIN)
				|| BiomeDictionary.hasType(biome, Type.COLD))
			return Anima.OZONE;
		else if (BiomeDictionary.hasType(biome, Type.MESA))
			return Anima.CHAOS;
		else if (biome.equals(Biomes.MUSHROOM_ISLAND) || biome.equals(Biomes.MUSHROOM_ISLAND_SHORE))
			return Anima.PEACE;
		else
			return Anima.HORIZON;
	}

}
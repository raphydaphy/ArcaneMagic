package com.raphydaphy.arcanemagic.api.essence;

import java.awt.Color;

import com.raphydaphy.arcanemagic.common.ArcaneMagic;
import com.raphydaphy.arcanemagic.common.essence.EssenceCreation;
import com.raphydaphy.arcanemagic.common.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;
import com.raphydaphy.arcanemagic.common.network.PacketEssenceTransfer;
import com.raphydaphy.arcanemagic.common.tileentity.TileEntityEssenceStorage;

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

public class Essence extends IForgeRegistryEntry.Impl<Essence>
{

	public static final EssenceRegistry REGISTRY = new EssenceRegistry();

	public static final Essence OZONE = new Essence("ozone", ArcaneMagic.MODID + ".ozone", 0xea, 0xea, 0xea);
	public static final Essence DEPTH = new Essence("depth", ArcaneMagic.MODID + ".depth", 0x18, 0x7e, 0xa3);
	public static final Essence INFERNO = new Essence("inferno", ArcaneMagic.MODID + ".inferno", 0xbf, 0x56, 0);
	public static final Essence HORIZON = new Essence("horizon", ArcaneMagic.MODID + ".horizon", 0x06, 0x60, 0x18);
	public static final Essence PEACE = new Essence("peace", ArcaneMagic.MODID + ".peace", 0x30, 0x3a, 0x1f);
	public static final Essence CHAOS = new Essence("chaos", ArcaneMagic.MODID + ".chaos", 0x85, 0xb7, 0x2f);
	public static final Essence CREATION = new EssenceCreation();

	public static class EssenceSubscriber
	{

		@SubscribeEvent
		public void onEssenceRegister(Register<Essence> event)
		{
			event.getRegistry().registerAll(OZONE, DEPTH, INFERNO, HORIZON, PEACE, CHAOS, CREATION);
			OZONE.setItemForm(new ItemStack(ModRegistry.ESSENCE));
			DEPTH.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 1));
			INFERNO.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 2));
			HORIZON.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 3));
			PEACE.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 4));
			CHAOS.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 5));
			CREATION.setItemForm(new ItemStack(ModRegistry.CREATION));
		}
	}

	private final Color color;
	private String unlocName;
	protected ItemStack itemForm = ItemStack.EMPTY;

	private Essence(String name, String unlocName, int r, int g, int b)
	{
		this(name, unlocName, new Color(r, g, b));
	}

	private Essence(String name, String unlocName, Color color)
	{
		setRegistryName(ArcaneMagic.MODID, name);
		setUnlocalizedName(unlocName);
		this.color = color;
	}

	public Essence(String unlocName, Color color)
	{
		setUnlocalizedName(unlocName);
		this.color = color;
	}

	public Essence(Color color)
	{
		this.color = color;
	}

	public Essence setUnlocalizedName(String unloc)
	{
		this.unlocName = unloc;
		return this;
	}

	public Essence setItemForm(ItemStack stack)
	{
		if (itemForm.isEmpty())
			itemForm = stack;
		return this;
	}

	public static boolean sendEssence(World world, EssenceStack stack, Vec3d from, Vec3d to, boolean simulate,
			boolean spawnParticles)
	{
		return sendEssence(world, stack, from, to, to, simulate, spawnParticles);
	}

	public static boolean sendEssence(World world, EssenceStack stack, Vec3d from, Vec3d to, Vec3d toCosmetic,
			boolean simulate, boolean spawnParticles)
	{
		BlockPos fromPos = new BlockPos(Math.floor(from.x), Math.floor(from.y), Math.floor(from.z));
		BlockPos toPos = new BlockPos(Math.floor(to.x), Math.floor(to.y), Math.floor(to.z));

		TileEntity fromTEUnchecked = world.getTileEntity(fromPos);
		TileEntity toTEUnchecked = world.getTileEntity(toPos);
		
		if (toTEUnchecked instanceof TileEntityEssenceStorage)
		{
			IEssenceStorage storage = toTEUnchecked.getCapability(IEssenceStorage.CAP, null);
			if (storage != null)
			{
				if (fromTEUnchecked instanceof TileEntityEssenceStorage)
				{
					
					IEssenceStorage storageFrom = fromTEUnchecked.getCapability(IEssenceStorage.CAP, null);
					// sending from block to block, such as concentrator -> crystallizer
					if (storageFrom != null)
					{
						// sender block has enough essence to transfer it
						if (storageFrom.take(stack, true) == null)
						{
							// recieving block has enough capacity to accept it
							if (storage.store(stack, true) == null)
							{
								if (!simulate)
								{
									
									if (!world.isRemote)
									{
										// send and recieve essence
										storage.store(stack, false);
										storageFrom.take(stack, false);

										toTEUnchecked.markDirty();
										fromTEUnchecked.markDirty();

										ArcaneMagicPacketHandler.INSTANCE.sendToAll(
												new PacketEssenceTransfer(stack, from, to, toCosmetic, spawnParticles));
									} else if (spawnParticles)
									{
										ArcaneMagic.proxy.spawnEssenceParticles(world, from, new Vec3d(0, 0, 0),
												stack.getEssence(), toCosmetic, false);
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
					// if the receiving block can accept 100% of the essence
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
										new PacketEssenceTransfer(stack, from, to, toCosmetic, spawnParticles));
							} else
							{
								ArcaneMagic.proxy.spawnEssenceParticles(world, from, new Vec3d(0, 0, 0),
										stack.getEssence(), toCosmetic, to.equals(toCosmetic) ? false : true);

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
		return "essence." + unlocName;
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

	public static Essence getEssenceByID(int id)
	{
		if (id < Essence.REGISTRY.getValues().size())
		{
			return Essence.REGISTRY.getValues().get(id);
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "Essence: " + getRegistryName().toString();
	}

	// kms this is worse function evar die pls
	public static Essence getFromBiome(Biome biome)
	{
		if (BiomeDictionary.hasType(biome, Type.WATER))
			return Essence.DEPTH;
		else if (BiomeDictionary.hasType(biome, Type.FOREST) || BiomeDictionary.hasType(biome, Type.PLAINS))
			return Essence.HORIZON;
		else if (BiomeDictionary.hasType(biome, Type.DRY) || BiomeDictionary.hasType(biome, Type.NETHER))
			return Essence.INFERNO;
		else if (BiomeDictionary.hasType(biome, Type.HILLS))
			return Essence.OZONE;
		else if (BiomeDictionary.hasType(biome, Type.MESA))
			return Essence.CHAOS;
		else if (biome.equals(Biomes.MUSHROOM_ISLAND) || biome.equals(Biomes.MUSHROOM_ISLAND_SHORE))
			return Essence.PEACE;
		else
			return Essence.HORIZON;
	}

}
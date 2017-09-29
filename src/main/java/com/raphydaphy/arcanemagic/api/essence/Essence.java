package com.raphydaphy.arcanemagic.api.essence;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.capabilities.EssenceStorage;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicPacketHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.PacketEssenceTransfer;
import com.raphydaphy.arcanemagic.tileentity.TileEntityEssenceStorage;

import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.awt.Color;

public class Essence extends IForgeRegistryEntry.Impl<Essence>
{

	public static final IForgeRegistry<Essence> REGISTRY = new EssenceRegistry();

	public static final Essence INFERNO = new Essence("inferno", ArcaneMagic.MODID + ".inferno", 0xbf, 0x56, 0);
	public static final Essence DEPTH = new Essence("depth", ArcaneMagic.MODID + ".depth", 0x18, 0x7e, 0xa3);
	public static final Essence OZONE = new Essence("ozone", ArcaneMagic.MODID + ".ozone", 0xea, 0xea, 0xea);
	public static final Essence HORIZON = new Essence("horizon", ArcaneMagic.MODID + ".horizon", 0x06, 0x60, 0x18);
	public static final Essence PEACE = new Essence("peace", ArcaneMagic.MODID + ".peace", 0x30, 0x3a, 0x1f);
	public static final Essence CHAOS = new Essence("chaos", ArcaneMagic.MODID + ".chaos", 0x85, 0xb7, 0x2f);

	public static class EssenceSubscriber
	{

		@SubscribeEvent
		public void onEssenceRegister(Register<Essence> event)
		{
			event.getRegistry().registerAll(INFERNO, DEPTH, OZONE, HORIZON, PEACE, CHAOS);
			INFERNO.setItemForm(new ItemStack(ModRegistry.ESSENCE));
			DEPTH.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 1));
			OZONE.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 2));
			HORIZON.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 3));
			PEACE.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 4));
			CHAOS.setItemForm(new ItemStack(ModRegistry.ESSENCE, 1, 5));
		}
	}

	private final Color color;
	private String unlocName;
	protected ItemStack itemForm = ItemStack.EMPTY;

	private Essence(String name, String unlocName, int r, int g, int b){
		this(name, unlocName, new Color(r,g,b));
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

	public static boolean sendEssence(World world, EssenceStack stack, Vec3d from, Vec3d to, boolean simulate)
	{
		BlockPos fromPos = new BlockPos(Math.floor(from.x), Math.floor(from.y), Math.floor(from.z));
		BlockPos toPos = new BlockPos(Math.floor(to.x), Math.floor(to.y), Math.floor(to.z));

		TileEntity fromTEUnchecked = world.getTileEntity(fromPos);
		TileEntity toTEUnchecked = world.getTileEntity(toPos);

		if (toTEUnchecked instanceof TileEntityEssenceStorage)
		{
			TileEntityEssenceStorage toTE = (TileEntityEssenceStorage) toTEUnchecked;

			// sending from block to block, such as concentrator -> crystallizer
			if (fromTEUnchecked instanceof TileEntityEssenceStorage)
			{
				TileEntityEssenceStorage fromTE = (TileEntityEssenceStorage) fromTEUnchecked;

				// sender block has enough essence to transfer it
				if (fromTE.getCapability(EssenceStorage.CAP, null).take(stack, true) == null)
				{
					// recieving block has enough capacity to accept it
					if (toTE.getCapability(EssenceStorage.CAP, null).store(stack, true) == null)
					{
						if (!simulate)
						{
							// send and recieve essence
							fromTE.getCapability(EssenceStorage.CAP, null).take(stack, false);
							toTE.getCapability(EssenceStorage.CAP, null).store(stack, false);

							if (!world.isRemote)
							{
								ArcaneMagicPacketHandler.INSTANCE.sendToAll(new PacketEssenceTransfer(stack, from, to));
							}
							else
							{
								ArcaneMagic.proxy.spawnEssenceParticles(world, from, new Vec3d(0, 0, 0), stack.getEssence(),
										to, false);
							}
						}
						return true;
					}
				}
			}
			// sending from bedrock/wand to block
			else
			{
				// if the receiving block can accept 100% of the essence
				if (toTE.getCapability(EssenceStorage.CAP, null).store(stack, true) == null)
				{
					if (!simulate)
					{
						// do the thing!
						toTE.getCapability(EssenceStorage.CAP, null).store(stack, false);
						
						if (!world.isRemote)
						{
							ArcaneMagicPacketHandler.INSTANCE.sendToAll(new PacketEssenceTransfer(stack, from, to));
						}
						else
						{
							ArcaneMagic.proxy.spawnEssenceParticles(world, from, new Vec3d(0, 0, 0), stack.getEssence(),
									to, false);
						}
					}
					return true;
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

	public String toString()
	{
		return getRegistryName().toString();
	}

	// kms this is worse function evar
	public static Essence getFromBiome(Biome biome)
	{
		if (biome.equals(Biomes.DEEP_OCEAN) || biome.equals(Biomes.RIVER) || biome.equals(Biomes.FROZEN_OCEAN)
				|| biome.equals(Biomes.FROZEN_OCEAN) || biome.equals(Biomes.DEEP_OCEAN)
				|| biome.equals(Biomes.STONE_BEACH) || biome.equals(Biomes.BEACH) || biome.equals(Biomes.COLD_BEACH)
				|| biome.equals(Biomes.SWAMPLAND) || biome.equals(Biomes.MUTATED_SWAMPLAND))
		{
			return Essence.DEPTH;
		} else if (biome.equals(Biomes.PLAINS) || biome.equals(Biomes.PLAINS) || biome.equals(Biomes.DEFAULT)
				|| biome.equals(Biomes.FOREST) || biome.equals(Biomes.FOREST_HILLS) || biome.equals(Biomes.JUNGLE)
				|| biome.equals(Biomes.JUNGLE_EDGE) || biome.equals(Biomes.JUNGLE_HILLS)
				|| biome.equals(Biomes.MUTATED_BIRCH_FOREST) || biome.equals(Biomes.MUTATED_BIRCH_FOREST_HILLS)
				|| biome.equals(Biomes.MUTATED_JUNGLE) || biome.equals(Biomes.MUTATED_FOREST)
				|| biome.equals(Biomes.MUTATED_JUNGLE_EDGE) || biome.equals(Biomes.MUTATED_REDWOOD_TAIGA)
				|| biome.equals(Biomes.MUTATED_REDWOOD_TAIGA_HILLS) || biome.equals(Biomes.ROOFED_FOREST)
				|| biome.equals(Biomes.REDWOOD_TAIGA) || biome.equals(Biomes.REDWOOD_TAIGA_HILLS))
		{
			return Essence.HORIZON;
		} else if (biome.equals(Biomes.DESERT) || biome.equals(Biomes.DESERT_HILLS) || biome.equals(Biomes.HELL)
				|| biome.equals(Biomes.MUTATED_DESERT) || biome.equals(Biomes.MUTATED_SAVANNA)
				|| biome.equals(Biomes.SAVANNA) || biome.equals(Biomes.SAVANNA_PLATEAU)
				|| biome.equals(Biomes.MUTATED_SAVANNA_ROCK))
		{
			return Essence.INFERNO;
		} else if (biome.equals(Biomes.EXTREME_HILLS) || biome.equals(Biomes.EXTREME_HILLS_EDGE)
				|| biome.equals(Biomes.EXTREME_HILLS_WITH_TREES) || biome.equals(Biomes.ICE_MOUNTAINS)
				|| biome.equals(Biomes.ICE_PLAINS) || biome.equals(Biomes.MUTATED_EXTREME_HILLS)
				|| biome.equals(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES))
		{
			return Essence.OZONE;
		} else if (biome.equals(Biomes.MESA) || biome.equals(Biomes.MESA_CLEAR_ROCK) || biome.equals(Biomes.MESA_ROCK)
				|| biome.equals(Biomes.MUTATED_MESA) || biome.equals(Biomes.MUTATED_MESA_CLEAR_ROCK)
				|| biome.equals(Biomes.MUTATED_MESA_ROCK))
		{
			return Essence.CHAOS;
		} else if (biome.equals(Biomes.MUSHROOM_ISLAND) || biome.equals(Biomes.MUSHROOM_ISLAND_SHORE))
		{
			return Essence.PEACE;
		}
		return Essence.HORIZON;
	}

}
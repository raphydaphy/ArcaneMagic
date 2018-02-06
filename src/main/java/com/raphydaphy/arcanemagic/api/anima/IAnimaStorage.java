package com.raphydaphy.arcanemagic.api.anima;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * TODO more operations
 */
public interface IAnimaStorage extends INBTSerializable<NBTTagCompound>
{

	/* NB: do NOT use this field UNLESS you have a hard dep on this mod */
	@CapabilityInject(IAnimaStorage.class)
	static Capability<IAnimaStorage> CAP = null;

	/**
	 * @return a map of all the currently stored AnimaStacks
	 */
	Map<Anima, AnimaStack> getStored();

	/**
	 * @return the total animus stored across all different types
	 */
	int getTotalStored();

	/**
	 * Store the stack in the storage
	 * 
	 * @param in
	 *            stack to store
	 * @param simulate
	 *            actually do the thing?
	 * @return the amount that couldn't be stored, or null if everything got
	 *         stored
	 */
	AnimaStack store(AnimaStack in, boolean simulate);

	/**
	 * Take the stack out of the storage
	 * 
	 * @param out
	 *            stack to remove
	 * @param simulate
	 *            actually do the thing?
	 * @return the amount that couldn't be taken, or null if the entire request
	 *         was taken
	 */
	AnimaStack take(AnimaStack out, boolean simulate);

	/**
	 * @return the amount of anima that can be stored
	 * 
	 * @param type
	 *            the type of anima of which the capacity should be checked
	 */
	int getCapacity(Anima type);
}

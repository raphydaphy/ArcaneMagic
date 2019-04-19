package com.raphydaphy.arcanemagic.block.entity;

import com.raphydaphy.arcanemagic.block.SmelterBlock;
import com.raphydaphy.arcanemagic.block.entity.base.DoubleFluidBlockEntity;
import com.raphydaphy.arcanemagic.init.ArcaneMagicConstants;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.network.ClientBlockEntityUpdatePacket;
import com.raphydaphy.crochet.network.PacketHandler;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class MixerBlockEntity extends DoubleFluidBlockEntity implements SidedInventory, Tickable {
    public static final int MAX_FLUID = DropletValues.BUCKET * 4;
    private static final String WATER_KEY = "Water";
    private static final String LIQUIFIED_SOUL_KEY = "LiquifiedSoul";
    private static final int WATER_USE = DropletValues.NUGGET;
    private final int[] slots = {0};
    public long ticks = 0;
    private FluidInstance water = new FluidInstance(Fluids.WATER);
    private FluidInstance liquified_soul = new FluidInstance(ModRegistry.LIQUIFIED_SOUL);

    public MixerBlockEntity() {
        super(ModRegistry.MIXER_TE, 1);
    }

    @Override
    public void tick() {
        if (world.isClient) {
            ticks++;
        } else if (isBottom() && world.getTime() % 10 == 0 && (world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.add(0, 1, 0)))) {
            ItemStack pendant = getInvStack(0);
            if (!pendant.isEmpty() && pendant.getItem() == ModRegistry.SOUL_PENDANT) {
                CompoundTag tag = pendant.getTag();
                if (liquified_soul.getAmount() + ArcaneMagicConstants.LIQUIFIED_SOUL_RATIO <= MAX_FLUID && water.getAmount() >= WATER_USE && tag != null) {
                    int pendantSoul = tag.getInt(ArcaneMagicConstants.SOUL_KEY);
                    if (pendantSoul >= 1) {
                        water.subtractAmount(WATER_USE);
                        liquified_soul.addAmount(ArcaneMagicConstants.LIQUIFIED_SOUL_RATIO);
                        tag.putInt(ArcaneMagicConstants.SOUL_KEY, pendantSoul - 1);
                        markDirty();
                    }
                }
            }
        }
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        if (tag.containsKey(WATER_KEY)) {
            water = new FluidInstance((CompoundTag) tag.getTag(WATER_KEY));
        } else {
            water = new FluidInstance(Fluids.WATER);
        }
        if (tag.containsKey(LIQUIFIED_SOUL_KEY)) {
            liquified_soul = new FluidInstance((CompoundTag) tag.getTag(LIQUIFIED_SOUL_KEY));
        } else {
            liquified_soul = new FluidInstance(ModRegistry.LIQUIFIED_SOUL);
        }
    }

    @Override
    public void writeContents(CompoundTag tag) {
        if (isBottom()) {
            Inventories.toTag(tag, contents);
            if (!water.isEmpty()) {
                tag.put(WATER_KEY, water.toTag(new CompoundTag()));
            }
            if (!liquified_soul.isEmpty()) {
                tag.put(LIQUIFIED_SOUL_KEY, liquified_soul.toTag(new CompoundTag()));
            }
        }
    }

    @Override
    public int getMaxCapacity() {
        return MAX_FLUID;
    }

    @Override
    protected boolean canInsertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount) {
        return fluid == Fluids.WATER && this.water.getAmount() + amount <= MAX_FLUID;
    }

    @Override
    protected boolean canExtractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount) {
        return bottom && fluid == ModRegistry.LIQUIFIED_SOUL && this.liquified_soul.getAmount() - amount >= 0;
    }

    @Override
    protected void insertFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount) {
        if (!world.isClient && fluid == Fluids.WATER && this.water.getAmount() + amount <= MAX_FLUID) {
            this.water.addAmount(amount);

            if (this.water.getFluid() != fluid) {
                this.water.setFluid(fluid);
            }
            markDirty();
        }
    }

    @Override
    protected void extractFluidImpl(boolean bottom, Direction fromSide, Fluid fluid, int amount) {
        if (!world.isClient && this.liquified_soul.getFluid() == fluid && this.liquified_soul.getAmount() - amount >= 0) {
            this.liquified_soul.subtractAmount(amount);
            markDirty();
        }
    }

    @Override
    protected void setFluidImpl(boolean bottom, Direction fromSide, FluidInstance instance) {
        if (!world.isClient) {
            if (bottom) {
                this.liquified_soul = instance;
            } else {
                this.water = instance;
            }
            markDirty();
        }
    }

    @Override
    protected FluidInstance[] getFluidsImpl(boolean bottom, Direction fromSide) {
        if (!bottom) {
            return fromSide == null ? new FluidInstance[]{water} : new FluidInstance[]{};
        } else {
            Direction facing = world.getBlockState(pos).get(SmelterBlock.FACING);
            return (facing != fromSide && facing != fromSide.getOpposite()) ? new FluidInstance[]{liquified_soul, water} : new FluidInstance[]{};
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.sendToAllAround(new ClientBlockEntityUpdatePacket(toInitialChunkDataTag()), world, getPos(), 300);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        CompoundTag tag = super.toInitialChunkDataTag();
        writeContents(tag);
        return new BlockEntityUpdateS2CPacket(getPos(), -1, tag);
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        CompoundTag tag = super.toInitialChunkDataTag();
        writeContents(tag);
        return tag;
    }

    @Override
    public int getInvMaxStackAmount() {
        return 1;
    }

    @Override
    public boolean isValidInvStackBottom(int slot, ItemStack item) {
        return getInvStack(slot).isEmpty() && !item.isEmpty() && item.getItem() == ModRegistry.SOUL_PENDANT;
    }

    @Override
    public int[] getInvAvailableSlots(Direction dir) {
        return dir == Direction.UP ? new int[0] : slots;
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, Direction dir) {
        return isValidInvStack(slot, stack);
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}

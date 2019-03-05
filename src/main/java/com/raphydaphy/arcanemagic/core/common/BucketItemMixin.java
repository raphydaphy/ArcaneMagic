package com.raphydaphy.arcanemagic.core.common;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item
{
	@Final
	@Shadow
	private Fluid fluid;

	private BucketItemMixin(Settings settings)
	{
		super(settings);
	}

	@Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
	private void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult> info)
	{
		ItemStack stack = player.getStackInHand(hand);
		HitResult hitResult = getHitResult(world, player, fluid == Fluids.EMPTY ? RayTraceContext.FluidHandling.SOURCE_ONLY : RayTraceContext.FluidHandling.NONE);
		if (hitResult.getType() == HitResult.Type.BLOCK)
		{
			BlockHitResult blockHitResult_1 = (BlockHitResult) hitResult;
			Block block = world.getBlockState(blockHitResult_1.getBlockPos()).getBlock();

			if (!player.isSneaking())
			{
				if (block == ModRegistry.MIXER || block == ModRegistry.PUMP)
				{
					info.setReturnValue(new TypedActionResult<>(ActionResult.PASS, stack));
				}
			}
		}
	}
}

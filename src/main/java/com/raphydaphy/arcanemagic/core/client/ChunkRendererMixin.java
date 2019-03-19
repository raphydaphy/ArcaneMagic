package com.raphydaphy.arcanemagic.core.client;

import com.raphydaphy.arcanemagic.client.render.IExtraRenderLayers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.class_852;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkRenderData;
import net.minecraft.client.render.chunk.ChunkRenderTask;
import net.minecraft.client.render.chunk.ChunkRenderer;
import net.minecraft.client.world.SafeWorldView;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by Thiakil on 19/03/2019.
 */
@Mixin(ChunkRenderer.class)
public abstract class ChunkRendererMixin {
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/block/Block.getRenderLayer()Lnet/minecraft/block/BlockRenderLayer;"), method = "rebuildChunk(FFFLnet/minecraft/client/render/chunk/ChunkRenderTask;)V", locals = LocalCapture.CAPTURE_FAILHARD)
	public void rebuildChunk(float float_1, float float_2, float float_3, ChunkRenderTask chunkRenderTask_1, CallbackInfo cbInfo, ChunkRenderData chunkRenderData_1, BlockPos blockPos_1, BlockPos blockPos_2, class_852 class_852_1, Set set_1, SafeWorldView safeWorldView_1, boolean[] booleans_1, Random random_1, BlockRenderManager blockRenderManager_1, Iterator var16, BlockPos blockPos_3, BlockState blockState_1, Block block_1, BlockRenderLayer normalRenderLayer){
		if (block_1 instanceof IExtraRenderLayers){
			for (BlockRenderLayer layer : ((IExtraRenderLayers) block_1).getExtraRenderLayers()){
				if (layer == normalRenderLayer){
					throw new IllegalStateException("Normal render layer should not be returned by getExtraRenderLayers()");
				}
				BufferBuilder bufferBuilder_2 = chunkRenderTask_1.getBufferBuilders().get(layer.ordinal());
				if (!chunkRenderData_1.isBufferInitialized(layer)) {
					chunkRenderData_1.markBufferInitialized(layer);
					this.beginBufferBuilding(bufferBuilder_2, blockPos_1);
				}

				booleans_1[layer.ordinal()] |= blockRenderManager_1.tesselateBlock(blockState_1, blockPos_3, safeWorldView_1, bufferBuilder_2, random_1);
			}
		}
	}

	@Shadow
	private void beginBufferBuilding(BufferBuilder bufferBuilder_2, BlockPos blockPos_1){}

}

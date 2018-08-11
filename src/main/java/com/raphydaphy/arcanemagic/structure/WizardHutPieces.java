package com.raphydaphy.arcanemagic.structure;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.List;
import java.util.Random;

public class WizardHutPieces
{
	private static final ResourceLocation WIZARD_HUT_STRUCTURE = new ResourceLocation(ArcaneMagicResources.MOD_ID, "wizard_hut");

	public static void addPieces(List<StructurePiece> pieces, IWorld world, TemplateManager manager, BlockPos pos)
	{
		pieces.add(new WizardHutPieces.Piece(world, manager, pos));
	}


	public static class Piece extends TemplateStructurePiece
	{
		public Piece()
		{
		}

		public Piece(IWorld world, TemplateManager manager, BlockPos pos)
		{
			this.templatePosition = pos;
			initPiece(world, manager);
		}

		@Override
		protected void handleDataMarker(String name, BlockPos pos, IWorld world, Random rand, MutableBoundingBox bounds)
		{

		}

		private void initPiece(IWorld world, TemplateManager manager)
		{
			Template template = manager.func_200220_a(WIZARD_HUT_STRUCTURE);

			PlacementSettings settings = (new PlacementSettings());
			this.setup(template, this.templatePosition, settings);
		}

		@Override
		public boolean addComponentParts(IWorld world, Random rand, MutableBoundingBox bounds, ChunkPos chunkPos)
		{
			if (template == null)
			{
				initPiece(world, world.getSaveHandler().getStructureTemplateManager());
			}
			int worldSurface = getAverageGroundLevel(world, bounds) + 1;
			if (worldSurface == -1)
			{
				return false;
			}
			System.out.println("New wizard hut at " + templatePosition);
			BlockPos tempPosition = this.templatePosition;
			this.templatePosition = this.templatePosition.add(0, worldSurface - 90 - 1, 0);
			boolean superAddParts = super.addComponentParts(world, rand, bounds, chunkPos);
			this.templatePosition = tempPosition;
			return superAddParts;
		}

		private int getAverageGroundLevel(IWorld world, MutableBoundingBox bounds) {
			int lvt_3_1_ = 0;
			int lvt_4_1_ = 0;
			BlockPos.MutableBlockPos lvt_5_1_ = new BlockPos.MutableBlockPos();

			for(int lvt_6_1_ = this.boundingBox.minZ; lvt_6_1_ <= this.boundingBox.maxZ; ++lvt_6_1_) {
				for(int lvt_7_1_ = this.boundingBox.minX; lvt_7_1_ <= this.boundingBox.maxX; ++lvt_7_1_) {
					lvt_5_1_.setPos(lvt_7_1_, 64, lvt_6_1_);
					if (bounds.isVecInside(lvt_5_1_)) {
						lvt_3_1_ += world.getTopBlock(net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, lvt_5_1_).getY();
						++lvt_4_1_;
					}
				}
			}

			if (lvt_4_1_ == 0) {
				return -2;
			} else {
				return lvt_3_1_ / lvt_4_1_;
			}
		}
	}
}

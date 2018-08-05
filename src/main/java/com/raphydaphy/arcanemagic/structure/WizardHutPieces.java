package com.raphydaphy.arcanemagic.structure;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class WizardHutPieces
{
    private static final ResourceLocation WIZARD_HUT = new ResourceLocation(ArcaneMagicResources.MOD_ID, "wizard_hut");


    public static void addPieces(List<StructurePiece> pieces, IWorld world, TemplateManager manager, BlockPos pos)
    {
        pieces.add(new WizardHutPieces.Piece(world, manager, pos));
    }


    public static class Piece extends TemplateStructurePiece
    {
        public Piece(IWorld world, TemplateManager manager, BlockPos pos)
        {
            this.templatePosition = pos;
            initPiece(world, manager);
        }

        @Override
        protected void handleDataMarker(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableBoundingBox mutableBoundingBox)
        {

        }

        private void initPiece(IWorld world, TemplateManager manager)
        {
            System.out.println("Wizard hut at " + templatePosition);
            Template template = manager.func_200220_a(WIZARD_HUT);

            PlacementSettings settings = (new PlacementSettings()).setRotation(Rotation.NONE).setMirror(Mirror.NONE).func_207665_a(new BlockPos(0, 0, 0));
            this.setup(template, this.templatePosition, settings);
        }

        @Override
        public boolean addComponentParts(IWorld world, Random rand, MutableBoundingBox bounds, ChunkPos pos)

        {
            // Copied from VillagePieces.Village
            int averageGroundLvl = this.getAverageGroundLevel(world, bounds);
            if (averageGroundLvl < 0)
            {
                return true;
            }
            this.templatePosition.add(0, averageGroundLvl, 0);
            return super.addComponentParts(world, rand, bounds, pos);
        }

        // Copied from VillagePieces.Village
        private int getAverageGroundLevel(IWorld world, MutableBoundingBox bounds)
        {
            int lvt_3_1_ = 0;
            int lvt_4_1_ = 0;
            BlockPos.MutableBlockPos lvt_5_1_ = new BlockPos.MutableBlockPos();

            for (int lvt_6_1_ = this.boundingBox.minZ; lvt_6_1_ <= this.boundingBox.maxZ; ++lvt_6_1_)
            {
                for (int lvt_7_1_ = this.boundingBox.minX; lvt_7_1_ <= this.boundingBox.maxX; ++lvt_7_1_)
                {
                    lvt_5_1_.setPos(lvt_7_1_, 64, lvt_6_1_);
                    if (bounds.isVecInside(lvt_5_1_))
                    {
                        lvt_3_1_ += world.getTopBlock(net.minecraft.world.gen.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, lvt_5_1_).getY();
                        ++lvt_4_1_;
                    }
                }
            }

            if (lvt_4_1_ == 0)
            {
                return -1;
            } else
            {
                return lvt_3_1_ / lvt_4_1_;
            }
        }
    }
}

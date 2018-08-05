package com.raphydaphy.arcanemagic.structure;

import com.raphydaphy.arcanemagic.util.ArcaneMagicResources;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
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
    private static final ResourceLocation WIZARD_HUT = new ResourceLocation(ArcaneMagicResources.MOD_ID,"wizard_hut");


    public static void addPieces (List<StructurePiece> pieces, TemplateManager manager, BlockPos pos)
    {
        System.out.println("GENERATING A NEW WIZARD HUT AT " + pos);
        pieces.add(new WizardHutPieces.Piece(manager, pos));
    }

    public static class Piece extends TemplateStructurePiece
    {
        public Piece(TemplateManager manager, BlockPos pos)
        {
            this.templatePosition = pos;
            initPiece(manager);
        }

        @Override
        protected void handleDataMarker(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableBoundingBox mutableBoundingBox)
        {

        }

        private void initPiece(TemplateManager manager)
        {
            Template template = manager.func_200220_a(WIZARD_HUT);
            PlacementSettings settings = (new PlacementSettings()).setRotation(Rotation.NONE).setMirror(Mirror.NONE).func_207665_a(new BlockPos(0, 0, 0));
            this.setup(template, this.templatePosition, settings);
        }
    }
}

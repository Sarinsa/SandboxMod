package com.sandboxmod.common.block;

import com.sandboxmod.common.blockentity.PurifyingFlowerTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

@SuppressWarnings("deprecation")
public class PurityFlowerBlock extends FlowerBlock {

    public PurityFlowerBlock() {
        super(Effects.REGENERATION, 450, AbstractBlock.Properties.of(Material.PLANT, MaterialColor.COLOR_GREEN)
                .instabreak()
                .lightLevel((state) -> 8)
                .noOcclusion()
                .noCollission()
                .randomTicks()
                .sound(SoundType.GRASS));
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos belowPos = pos.below();

        if (state.getBlock() == this)
            return worldReader.getBlockState(belowPos).isFaceSturdy(worldReader, belowPos, Direction.UP);
        return this.mayPlaceOn(worldReader.getBlockState(belowPos), worldReader, belowPos);
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader blockReader) {
        return new PurifyingFlowerTileEntity();
    }
}

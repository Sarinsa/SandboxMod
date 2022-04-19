package com.sandboxmod.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class CorruptedLogBlock extends RotatedPillarBlock {

    public CorruptedLogBlock() {
        super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_PURPLE)
                .strength(1.0F)
                .sound(SoundType.WART_BLOCK));
    }
}

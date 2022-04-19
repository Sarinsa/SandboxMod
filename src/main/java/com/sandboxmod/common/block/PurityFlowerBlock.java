package com.sandboxmod.common.block;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.util.BiomeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

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
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Biome corrupted = BiomeHelper.getFromRegistry(world, SandboxMod.resourceLoc("corrupted_lands"));

        if (corrupted == null)
            return;

        BlockPos randomPos = pos.offset(
                random.nextInt(5) - random.nextInt(5),
                random.nextInt(5) - random.nextInt(5),
                random.nextInt(5) - random.nextInt(5)
        );

        if (world.getBiome(randomPos) == corrupted) {
            Biome originalBiome = BiomeHelper.getUncachedBiome(world.getBiomeManager().biomeZoomSeed, world, pos.getX(), pos.getZ());
            BiomeHelper.setBiomeAt(world, originalBiome, randomPos);
        }
    }
}

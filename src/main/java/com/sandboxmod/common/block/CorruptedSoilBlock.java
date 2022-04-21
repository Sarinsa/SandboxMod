package com.sandboxmod.common.block;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.core.registry.SMBlocks;
import com.sandboxmod.common.tag.SMBlockTags;
import com.sandboxmod.common.util.BiomeHelper;
import com.sandboxmod.common.util.BlockHelper;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.FastRandom;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CorruptedSoilBlock extends Block {

    public CorruptedSoilBlock() {
        super(AbstractBlock.Properties.of(BlockHelper.Materials.CORRUPTED_DIRT)
                .randomTicks()
                .strength(1.0F)
                .sound(SoundType.WART_BLOCK));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Biome corrupted = BiomeHelper.getFromRegistry(world, SandboxMod.resourceLoc("corrupted_lands"));

        // >:(
        if (corrupted == null)
            return;

        if (world.getBiome(pos) != corrupted) {
            // Random chance of dying away if we are not in the corrupted lands biome.
            // More persistent than corrupted mass.
            if (random.nextInt(10) == 0)
                world.setBlock(pos, Blocks.DIRT.defaultBlockState(), Constants.BlockFlags.DEFAULT);

            // Rarely attempt to spread, even if not in the corrupted biome
            if (random.nextInt(100) == 0) {
                BlockPos spreadPos = this.randomSpreadPos(world, pos, random);
                BlockState spreadState = CorruptedMassBlock.stateForSpread(world, spreadPos);

                if (spreadPos == null || spreadState == null)
                    return;

                if (world.getBiome(spreadPos) != corrupted) {
                    BiomeHelper.setBiomeAt(world, corrupted, spreadPos);
                }
                world.setBlock(spreadPos, spreadState, Constants.BlockFlags.DEFAULT);
                world.playSound(null, spreadPos, SoundEvents.COMPOSTER_READY, SoundCategory.BLOCKS, 0.35F, 0.6F + (random.nextFloat() / 3));
            }
        }
    }

    @Nullable
    private BlockPos randomSpreadPos(IWorld world, BlockPos origin, Random random) {
        List<BlockPos> validPositions = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockPos pos = origin.relative(direction);

            if (pos.getY() < world.getMaxBuildHeight() && pos.getY() > 1) {
                BlockState state = world.getBlockState(pos);

                if (state.getFluidState().isEmpty() && (state.getMaterial().isReplaceable() || state.is(SMBlockTags.CORRUPTIBLE_BLOCKS))) {
                    validPositions.add(pos.immutable());
                }
            }
        }
        return validPositions.isEmpty() ? null : validPositions.get(random.nextInt(validPositions.size()));
    }
}

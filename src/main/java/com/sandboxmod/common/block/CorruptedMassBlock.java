package com.sandboxmod.common.block;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.core.registry.SMBlocks;
import com.sandboxmod.common.util.BiomeHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class CorruptedMassBlock extends Block  {

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public CorruptedMassBlock() {
        super(AbstractBlock.Properties.of(Material.SPONGE, MaterialColor.COLOR_PURPLE)
                .isValidSpawn((state, blockReader, pos, entityType) -> false)
                .lightLevel((state) -> 2)
                .strength(0.8F)
                .noOcclusion()
                .noCollission()
                .sound(SoundType.WART_BLOCK)
                .randomTicks()
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState s, boolean flag) {
        super.onPlace(state, world, pos, s, flag);

        if (!world.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) world;

            Biome corrupted = BiomeHelper.getFromRegistry(world, SandboxMod.resourceLoc("corrupted_lands"));

            if (corrupted == null)
                return;

            BiomeHelper.setBiomeAt(serverWorld, corrupted, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Direction direction = Direction.getRandom(random);
        BlockPos neighbor = pos.relative(direction);

        if (canSpreadTo(world, neighbor)) {
            world.setBlock(neighbor, SMBlocks.CORRUPTED_MASS.get().defaultBlockState(), 3);
            world.playSound(null, neighbor, SoundEvents.COMPOSTER_READY, SoundCategory.BLOCKS, 0.7F, 0.6F + (random.nextFloat() / 3));
            Biome corrupted = BiomeHelper.getFromRegistry(world, SandboxMod.resourceLoc("corrupted_lands"));

            if (corrupted == null)
                return;

            BiomeHelper.setBiomeAt(world, corrupted, neighbor);
        }
    }


    private boolean canSpreadTo(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState belowState = world.getBlockState(pos.below());
        BlockState aboveState = world.getBlockState(pos.above());

        if (state.canBeReplacedByLeaves(world, pos)) {
            if (belowState.isSolidRender(world, pos) && state.getBlock() != SMBlocks.CORRUPTED_MASS.get()) {
                return true;
            }
        }
        return false;
    }
}

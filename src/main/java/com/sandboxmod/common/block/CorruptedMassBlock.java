package com.sandboxmod.common.block;

import com.google.common.collect.Maps;
import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.core.registry.SMBlocks;
import com.sandboxmod.common.tag.SMBlockTags;
import com.sandboxmod.common.util.BiomeHelper;
import com.sandboxmod.common.util.BlockHelper;
import com.sandboxmod.common.util.CorruptionTransformHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class CorruptedMassBlock extends Block {

    private static final Direction[] DIRECTIONS = Direction.values();

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
    });
    protected VoxelShape[] shapeByIndex;


    public CorruptedMassBlock() {
        super(AbstractBlock.Properties.of(BlockHelper.Materials.CORRUPTED_MASS)
                .strength(0.8F)
                .noOcclusion()
                .noCollission()
                .sound(SoundType.WART_BLOCK)
                .randomTicks()
                .speedFactor(0.8F)
        );
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));

        this.shapeByIndex = makeShapes();
    }

    private VoxelShape[] makeShapes() {
        // Same order as Direction's enum order
        VoxelShape[] dirShapes = new VoxelShape[] {
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D),
                Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D),
                Block.box(16.0D, 0.0D, 16.0D, 0.0D, 16.0D, 15.0D),
                Block.box(0.0D, 0.0D, 16.0D, 1.0D, 16.0D, 0.0D),
                Block.box(16.0D, 0.0D, 16.0D, 15.0D, 16.0D, 0.0D),
        };
        VoxelShape[] allShapes = new VoxelShape[64];

        // No idea how this works!
        // Copied from SixWayBlock.java
        for(int k = 0; k < 64; ++k) {
            VoxelShape shape = VoxelShapes.empty();

            for(int j = 0; j < DIRECTIONS.length; ++j) {
                if ((k & 1 << j) != 0) {
                    shape = VoxelShapes.or(shape, dirShapes[j]);
                }
            }
            allShapes[k] = shape;
        }
        return allShapes;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
        return this.shapeByIndex[this.getAABBIndex(state)];
    }

    protected int getAABBIndex(BlockState state) {
        int i = 0;

        for(int j = 0; j < DIRECTIONS.length; ++j) {
            if (state.getValue(PROPERTY_BY_DIRECTION.get(DIRECTIONS[j]))) {
                i |= 1 << j;
            }
        }
        return i;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldReader, BlockPos pos) {
        boolean onSturdyFace = false;

        for (Direction direction : Direction.values()) {
            BlockPos relative = pos.relative(direction);

            if (worldReader.getBlockState(relative).isFaceSturdy(worldReader, relative, direction.getOpposite())) {
                onSturdyFace = true;
                break;
            }
        }
        return onSturdyFace;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else {
            BlockState newState = this.placeState(world, pos);
            return newState == null ? super.updateShape(state, direction, neighborState, world, pos, neighborPos) : newState;
        }
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClientSide && entity instanceof LivingEntity) {

            // Occasional ouchie
            if (world.random.nextInt(400) == 0) {
                ((LivingEntity) entity).addEffect(new EffectInstance(Effects.WITHER, 80));
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        World world = useContext.getLevel();
        BlockPos pos = useContext.getClickedPos();
        BlockState state = this.placeState(world, pos);

        return state == null ? this.defaultBlockState() : state;
    }

    @Nullable
    private BlockState placeState(IWorldReader world, BlockPos pos) {
        BlockState state = this.defaultBlockState();
        boolean foundSturdy = false;

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);

            if (world.getBlockState(neighbor).isFaceSturdy(world, neighbor, direction.getOpposite())) {
                state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), true);
                foundSturdy = true;
            }
        }
        return foundSturdy ? state : null;
    }

    @Nullable
    public static BlockState stateForSpread(IWorldReader world, @Nullable BlockPos spreadPos) {
        if (spreadPos == null)
            return null;

        BlockState spreadState = SMBlocks.CORRUPTED_MASS.get().defaultBlockState();
        boolean foundSturdy = false;

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = spreadPos.relative(direction);

            if (world.getBlockState(neighbor).isFaceSturdy(world, neighbor, direction.getOpposite())) {
                spreadState = spreadState.setValue(PROPERTY_BY_DIRECTION.get(direction), true);
                foundSturdy = true;
            }
        }
        return foundSturdy ? spreadState : null;
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState s, boolean flag) {
        super.onPlace(state, world, pos, s, flag);

        if (!world.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) world;

            Biome corrupted = BiomeHelper.getFromRegistry(world, SandboxMod.resourceLoc("corrupted_lands"));

            if (corrupted == null || world.getBiome(pos) == corrupted)
                return;

            BiomeHelper.setBiomeAt(serverWorld, corrupted, pos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Biome corrupted = BiomeHelper.getFromRegistry(world, SandboxMod.resourceLoc("corrupted_lands"));

        // >:(
        if (corrupted == null)
            return;

        // Die away if we are not in the corrupted biome
        if (world.getBiome(pos) != corrupted) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT);
        }
        // Attempt to corrupt random neighbor
        if (random.nextInt(15) == 1) {
            BlockPos neighbor = pos.relative(Direction.getRandom(random));
            this.transformBlock(world, neighbor);
        }
        // Attempt to spread
        else {
            BlockPos spreadPos = this.randomSpreadPos(world, pos, random);
            BlockState spreadState = stateForSpread(world, spreadPos);

            if (spreadPos == null || spreadState == null)
                return;

            if (world.getBiome(spreadPos) != corrupted) {
                BiomeHelper.setBiomeAt(world, corrupted, spreadPos);
            }
            world.setBlock(spreadPos, spreadState, Constants.BlockFlags.DEFAULT);
            world.playSound(null, spreadPos, SoundEvents.COMPOSTER_READY, SoundCategory.BLOCKS, 0.35F, 0.6F + (random.nextFloat() / 3));
        }
    }

    @Nullable
    private BlockPos randomSpreadPos(IWorld world, BlockPos origin, Random random) {
        Iterable<BlockPos> positions = BlockPos.betweenClosed(origin.offset(-1, -1, -1), origin.offset(1, 1, 1));
        List<BlockPos> validPositions = new ArrayList<>();

        for (BlockPos pos : positions) {
            if (pos.getY() < world.getMaxBuildHeight() && pos.getY() > 1) {
                BlockState state = world.getBlockState(pos);

                if (state.getFluidState().isEmpty() && (state.getMaterial().isReplaceable() || state.is(SMBlockTags.CORRUPTIBLE_BLOCKS))) {
                    validPositions.add(pos.immutable());
                }
            }
        }
        return validPositions.isEmpty() ? null : validPositions.get(random.nextInt(validPositions.size()));
    }

    private void transformBlock(IWorld world, BlockPos pos) {
        BlockState toCorrupt = world.getBlockState(pos);
        Function<BlockState, BlockState> converter = CorruptionTransformHelper.getConverter(toCorrupt.getBlock());

        if (converter != null) {
            BlockState state = converter.apply(toCorrupt);
            world.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(UP, DOWN, NORTH, WEST, EAST, SOUTH);
    }
}

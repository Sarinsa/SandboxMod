package com.sandboxmod.common.util;

import com.mojang.datafixers.util.Pair;
import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.core.registry.SMBlocks;
import com.sandboxmod.common.tag.SMBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.BlockStateVariantBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class CorruptionTransformHelper {

    /** Holds all registered converters until it is safe to properly construct them (after block registration) */
    private static final List<Pair<Supplier<Block>, Function<BlockState, BlockState>>> converterHolder = new ArrayList<>();


    /**
     * A Map containing all registered BlockState converters for corruption transformable blocks.<br>
     * <br>
     *
     * The key is the Block that can get transformed.<br>
     * <br>
     *
     * The value is a Function that provides the block's
     * default state as input and expects the result block's
     * state as output.
     */
    private static final Map<Block, Function<BlockState, BlockState>> stateConverters = new HashMap<>();



    private static void addDefaultConverters() {
        addConverter(() -> Blocks.OAK_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_OAK_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.OAK_WOOD, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_OAK_WOOD, DefaultConverters.LOG_ANY);

        addConverter(() -> Blocks.BIRCH_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_BIRCH_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.BIRCH_WOOD, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_BIRCH_WOOD, DefaultConverters.LOG_ANY);

        addConverter(() -> Blocks.SPRUCE_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_SPRUCE_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.SPRUCE_WOOD, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_SPRUCE_WOOD, DefaultConverters.LOG_ANY);

        addConverter(() -> Blocks.ACACIA_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_ACACIA_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.ACACIA_WOOD, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_ACACIA_WOOD, DefaultConverters.LOG_ANY);

        addConverter(() -> Blocks.JUNGLE_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_JUNGLE_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.JUNGLE_WOOD, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_JUNGLE_WOOD, DefaultConverters.LOG_ANY);

        addConverter(() -> Blocks.DARK_OAK_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_DARK_OAK_LOG, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.DARK_OAK_WOOD, DefaultConverters.LOG_ANY);
        addConverter(() -> Blocks.STRIPPED_DARK_OAK_WOOD, DefaultConverters.LOG_ANY);

        addConverter(() -> Blocks.GRASS_BLOCK, DefaultConverters.transformSimple(SMBlocks.CORRUPTED_SOIL.get()::defaultBlockState));
        addConverter(() -> Blocks.DIRT, DefaultConverters.transformSimple(SMBlocks.CORRUPTED_SOIL.get()::defaultBlockState));
        addConverter(() -> Blocks.PODZOL, DefaultConverters.transformSimple(SMBlocks.CORRUPTED_SOIL.get()::defaultBlockState));
        addConverter(() -> Blocks.COARSE_DIRT, DefaultConverters.transformSimple(SMBlocks.CORRUPTED_SOIL.get()::defaultBlockState));
    }


    public static void registerAll() {
        stateConverters.clear();

        addDefaultConverters();

        converterHolder.forEach((pair) -> {
            registerConverter(pair.getFirst(), pair.getSecond());
        });
    }

    public static void addConverter(@Nonnull Supplier<Block> supplier, @Nonnull Function<BlockState, BlockState> converter) {
        converterHolder.add(new Pair<>(supplier, converter));
    }

    private static void registerConverter(@Nonnull Supplier<Block> supplier, @Nonnull Function<BlockState, BlockState> converter) {
        @Nullable Block block = supplier.get();

        if (block == null)
            return;

        if (stateConverters.containsKey(block)) {
            SandboxMod.LOGGER.warn("Attempted to register corruption block converter for block with ID \"{}\", but one has already been registered for this block.", block.getRegistryName());
            return;
        }
        stateConverters.put(block, converter);
    }

    @Nullable
    public static Function<BlockState, BlockState> getConverter(Block block) {
        if (stateConverters.containsKey(block)) {
            return stateConverters.get(block);
        }
        return null;
    }


    public static class DefaultConverters {

        public static final Function<BlockState, BlockState> LOG_ANY = (state) -> {
            return SMBlocks.CORRUPTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
        };

        public static Function<BlockState, BlockState> transformSimple(Supplier<BlockState> stateSupplier) {
            return (state) -> stateSupplier.get();
        }
    }
}

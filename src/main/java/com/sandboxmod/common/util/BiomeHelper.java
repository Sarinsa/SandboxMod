package com.sandboxmod.common.util;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.network.NetworkHelper;
import net.minecraft.util.FastRandom;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IBiomeReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.world.biome.BiomeContainer.HORIZONTAL_MASK;
import static net.minecraft.world.biome.BiomeContainer.VERTICAL_MASK;

public class BiomeHelper {

    public static void setBiomeAt(@Nonnull ServerWorld world, @Nonnull Biome biome, @Nonnull BlockPos pos) {
        Chunk chunk = world.getChunkAt(pos);
        BiomeContainer biomeContainer = chunk.getBiomes();

        if (biomeContainer != null) {
            int biomeIndex = getBiomeColumnIndex(world.getBiomeManager().biomeZoomSeed, pos.getX(), pos.getZ());
            biomeContainer.biomes[biomeIndex] = biome;
            NetworkHelper.sendBiomeUpdate(world, pos, biome.getRegistryName(), biomeIndex);
        }
    }

    @Nullable
    public static Biome getFromRegistry(IBiomeReader biomeReader, ResourceLocation biomeId) {
        Biome biome = biomeReader.registryAccess().registry(WorldGenRegistries.BIOME.key()).orElseThrow(() -> new IllegalStateException("Attempted to access the biome registry, but got null."))
                .get(biomeId);

        if (biome == null) {
            SandboxMod.LOGGER.error("Tried to fetch Biome with ID \"{}\" from the registry, but got null.", biomeId);
            return null;
        }
        return biome;
    }

    //
    //------------------------------- COPY-PASTE STUFF FROM VANILLA -----------------------------------
    //


    public static Biome getUncachedBiome(long biomeZoomSeed, IWorldReader worldReader, int x, int z) {
        int i = x - 2;
        int j = - 2;
        int k = z - 2;
        int l = i >> 2;
        int i1 = j >> 2;
        int j1 = k >> 2;
        double d0 = (double)(i & 3) / 4.0D;
        double d1 = (double)(j & 3) / 4.0D;
        double d2 = (double)(k & 3) / 4.0D;
        double[] adouble = new double[8];

        for(int k1 = 0; k1 < 8; ++k1) {
            boolean flag = (k1 & 4) == 0;
            boolean flag1 = (k1 & 2) == 0;
            boolean flag2 = (k1 & 1) == 0;
            int l1 = flag ? l : l + 1;
            int i2 = flag1 ? i1 : 0;
            int j2 = flag2 ? j1 : j1 + 1;
            double d3 = flag ? d0 : d0 - 1.0D;
            double d4 = flag1 ? d1 : d1 - 1.0D;
            double d5 = flag2 ? d2 : d2 - 1.0D;
            adouble[k1] = getFiddledDistance(biomeZoomSeed, l1, i2, j2, d3, d4, d5);
        }

        int k2 = 0;
        double d6 = adouble[0];

        for(int l2 = 1; l2 < 8; ++l2) {
            if (d6 > adouble[l2]) {
                k2 = l2;
                d6 = adouble[l2];
            }
        }

        int i3 = (k2 & 4) == 0 ? l : l + 1;
        int j3 = (k2 & 2) == 0 ? i1 : 0;
        int k3 = (k2 & 1) == 0 ? j1 : j1 + 1;
        return worldReader.getUncachedNoiseBiome(i3, j3, k3);
    }

    public static int getBiomeColumnIndex(long biomeZoomSeed, int x, int z) {
        int i = x - 2;
        int j = - 2;
        int k = z - 2;
        int l = i >> 2;
        int i1 = j >> 2;
        int j1 = k >> 2;
        double d0 = (double)(i & 3) / 4.0D;
        double d1 = (double)(j & 3) / 4.0D;
        double d2 = (double)(k & 3) / 4.0D;
        double[] adouble = new double[8];

        for(int k1 = 0; k1 < 8; ++k1) {
            boolean flag = (k1 & 4) == 0;
            boolean flag1 = (k1 & 2) == 0;
            boolean flag2 = (k1 & 1) == 0;
            int l1 = flag ? l : l + 1;
            int i2 = flag1 ? i1 : 0;
            int j2 = flag2 ? j1 : j1 + 1;
            double d3 = flag ? d0 : d0 - 1.0D;
            double d4 = flag1 ? d1 : d1 - 1.0D;
            double d5 = flag2 ? d2 : d2 - 1.0D;
            adouble[k1] = getFiddledDistance(biomeZoomSeed, l1, i2, j2, d3, d4, d5);
        }

        int k2 = 0;
        double d6 = adouble[0];

        for(int l2 = 1; l2 < 8; ++l2) {
            if (d6 > adouble[l2]) {
                k2 = l2;
                d6 = adouble[l2];
            }
        }

        int i3 = (k2 & 4) == 0 ? l : l + 1;
        int j3 = (k2 & 2) == 0 ? i1 : 0;
        int k3 = (k2 & 1) == 0 ? j1 : j1 + 1;
        return getNoiseBiomeIndex(i3, j3, k3);
    }

    private static int getNoiseBiomeIndex(int x, int y, int z) {
        int i = x & HORIZONTAL_MASK;
        int j = MathHelper.clamp(y, 0, VERTICAL_MASK);
        int k = z & HORIZONTAL_MASK;
        final int WIDTH_BITS = (int)Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
        return j << WIDTH_BITS + WIDTH_BITS | k << WIDTH_BITS | i;
    }

    private static double getFiddledDistance(long p_226845_0_, int p_226845_2_, int p_226845_3_, int p_226845_4_, double p_226845_5_, double p_226845_7_, double p_226845_9_) {
        long lvt_11_1_ = FastRandom.next(p_226845_0_, p_226845_2_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_3_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_4_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_2_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_3_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_4_);
        double d0 = getFiddle(lvt_11_1_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_0_);
        double d1 = getFiddle(lvt_11_1_);
        lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_0_);
        double d2 = getFiddle(lvt_11_1_);
        return sqr(p_226845_9_ + d2) + sqr(p_226845_7_ + d1) + sqr(p_226845_5_ + d0);
    }

    private static double getFiddle(long p_226844_0_) {
        double d0 = (double)((int)Math.floorMod(p_226844_0_ >> 24, 1024L)) / 1024.0D;
        return (d0 - 0.5D) * 0.9D;
    }

    private static double sqr(double d) {
        return d * d;
    }
}

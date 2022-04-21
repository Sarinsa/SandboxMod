package com.sandboxmod.common.blockentity;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.core.config.SMCommonConfig;
import com.sandboxmod.common.core.registry.SMBlockEntities;
import com.sandboxmod.common.util.BiomeHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class PurifyingFlowerTileEntity extends TileEntity implements ITickableTileEntity {

    public static int NEW_PURIFY_TIME = 20;
    private int timeUntilPurify = NEW_PURIFY_TIME;

    private static final ResourceLocation corruptedBiome = SandboxMod.resourceLoc("corrupted_lands");


    public PurifyingFlowerTileEntity() {
        super(SMBlockEntities.PURIFYING_FLOWER.get());
    }

    @Override
    public void tick() {
        if (this.level != null && !level.isClientSide) {

            if (--this.timeUntilPurify <= 0) {
                this.timeUntilPurify = NEW_PURIFY_TIME;

                ServerWorld world = (ServerWorld) this.level;
                Random random = world.getRandom();
                Biome corrupted = BiomeHelper.getFromRegistry(world, corruptedBiome);

                if (corrupted == null)
                    return;

                BlockPos randomPos = this.worldPosition.offset(
                        random.nextInt(6) - random.nextInt(6),
                        0,
                        random.nextInt(6) - random.nextInt(6)
                );

                if (world.getBiome(randomPos) == corrupted) {
                    Biome originalBiome = BiomeHelper.getBiomeForPurifying(world.getBiomeManager().biomeZoomSeed, world, randomPos.getX(), randomPos.getZ());

                    if (originalBiome != null) {
                        BiomeHelper.setBiomeAt(world, originalBiome, randomPos);
                    }
                }
            }
        }
    }
}

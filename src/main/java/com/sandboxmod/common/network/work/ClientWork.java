package com.sandboxmod.common.network.work;

import com.sandboxmod.common.network.message.S2CUpdateBiome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class ClientWork {

    public static void handleBiomeUpdate(S2CUpdateBiome message) {
        ClientWorld world = Minecraft.getInstance().level;
        BlockPos pos = message.pos;

        if (world == null || !world.isAreaLoaded(pos, 1)) {
            return;
        }
        Biome biome = world.registryAccess().registry(WorldGenRegistries.BIOME.key()).get().get(message.biomeId);
        Chunk chunk = world.getChunkAt(pos);
        chunk.getBiomes().biomes[message.biomeIndex] = biome;

        final int x = chunk.getPos().x;
        final int z = chunk.getPos().z;

        for(int i = 0; i < 16; ++i) {
            world.setSectionDirtyWithNeighbors(x, i, z);
        }
    }
}

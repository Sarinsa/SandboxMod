package com.sandboxmod.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class NetworkHelper {

    public static void sendBiomeUpdate(ServerWorld world, BlockPos pos, ResourceLocation biomeId, int biomeIndex) {
        for (ServerPlayerEntity player : world.players()) {
            player.connection.send(new SChunkDataPacket((Chunk) world.getChunk(pos), 65535));
            //acketHandler.sendToClient(new S2CUpdateBiome(biomeId, pos, biomeIndex), player);
        }
    }
}

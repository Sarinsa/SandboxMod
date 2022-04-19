package com.sandboxmod.common.network;

import com.sandboxmod.common.network.message.S2CUpdateBiome;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class NetworkHelper {

    public static void sendBiomeUpdate(ServerWorld world, BlockPos pos, ResourceLocation biomeId, int biomeIndex) {
        for (ServerPlayerEntity player : world.players()) {
            //player.connection.send(new SChunkDataPacket((Chunk) world.getChunk(pos), 65535));
            PacketHandler.sendToClient(new S2CUpdateBiome(biomeId, pos, biomeIndex), player);
        }
    }
}

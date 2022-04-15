package com.sandboxmod.common.network.message;

import com.sandboxmod.common.network.work.ClientWork;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CUpdateBiome {

    public final ResourceLocation biomeId;
    public final BlockPos pos;
    public final int biomeIndex;

    public S2CUpdateBiome(final ResourceLocation biomeId, final BlockPos pos, int biomeIndex) {
        this.biomeId = biomeId;
        this.pos = pos;
        this.biomeIndex = biomeIndex;
    }

    public static void handle(S2CUpdateBiome message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if(context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> ClientWork.handleBiomeUpdate(message));
        }
        context.setPacketHandled(true);
    }

    public static S2CUpdateBiome decode(PacketBuffer buffer) {
        return new S2CUpdateBiome(buffer.readResourceLocation(), buffer.readBlockPos(), buffer.readInt());
    }

    public static void encode(S2CUpdateBiome message, PacketBuffer buffer) {
        buffer.writeResourceLocation(message.biomeId);
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.biomeIndex);
    }
}

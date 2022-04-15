package com.sandboxmod.common.network;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.network.message.S2CUpdateBiome;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    private static final String PROTOCOL_NAME = "SANDBOX";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(SandboxMod.resourceLoc("channel"))
            .clientAcceptedVersions(PROTOCOL_NAME::equals)
            .serverAcceptedVersions(PROTOCOL_NAME::equals)
            .networkProtocolVersion(() -> PROTOCOL_NAME)
            .simpleChannel();


    private static int messageIndex;
    private static boolean registered = false;


    public static void registerMessages() {
        if (registered) {
            SandboxMod.LOGGER.error("Attempted to register mod packets when they have apparently already been registered.");
            return;
        }
        registerMessage(S2CUpdateBiome.class, S2CUpdateBiome::encode, S2CUpdateBiome::decode, S2CUpdateBiome::handle);
        registered = true;
    }

    public static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        CHANNEL.registerMessage(messageIndex++, messageType, encoder, decoder, messageConsumer, Optional.empty());
    }

    /**
     * Sends the specified message to the client.
     *
     * @param message The message to send to the client.
     * @param player The player client that should receive this message.
     * @param <MSG> Packet type.
     */
    public static <MSG> void sendToClient(MSG message, ServerPlayerEntity player) {
        CHANNEL.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}

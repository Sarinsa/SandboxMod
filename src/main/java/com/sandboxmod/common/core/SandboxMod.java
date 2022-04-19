package com.sandboxmod.common.core;

import com.sandboxmod.common.core.registry.SMBiomes;
import com.sandboxmod.common.core.registry.SMBlocks;
import com.sandboxmod.common.core.registry.SMItems;
import com.sandboxmod.common.network.PacketHandler;
import com.sandboxmod.common.util.CorruptionTransformHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SandboxMod.MODID)
public class SandboxMod {

    public static final String MODID = "sandboxmod";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public SandboxMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);

        SMBlocks.BLOCKS.register(eventBus);
        SMItems.ITEMS.register(eventBus);
        SMBiomes.BIOMES.register(eventBus);

        PacketHandler.registerMessages();
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SMBiomes.registerBiomes();
            CorruptionTransformHelper.registerAll();
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}

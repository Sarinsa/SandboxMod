package com.sandboxmod.common.event;

import com.sandboxmod.common.core.registry.SMBiomes;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BiomeEvents {

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getName() == null)
            return;

    }
}

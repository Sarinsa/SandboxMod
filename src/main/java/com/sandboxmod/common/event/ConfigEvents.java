package com.sandboxmod.common.event;

import com.sandboxmod.common.blockentity.PurifyingFlowerTileEntity;
import com.sandboxmod.common.core.config.SMCommonConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigEvents {

    @SubscribeEvent
    public void onConfigReload(ModConfig.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            PurifyingFlowerTileEntity.NEW_PURIFY_TIME = SMCommonConfig.COMMON.getFlowerTickInterval();
        }
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfig.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            PurifyingFlowerTileEntity.NEW_PURIFY_TIME = SMCommonConfig.COMMON.getFlowerTickInterval();
        }
    }
}

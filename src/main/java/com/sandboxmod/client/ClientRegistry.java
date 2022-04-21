package com.sandboxmod.client;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.core.registry.SMBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

import static com.sandboxmod.common.util.References.CORRUPTED_COLOR;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = SandboxMod.MODID)
public class ClientRegistry {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        setBlockRenderType(SMBlocks.CORRUPTED_MASS, RenderType.cutout());
        setBlockRenderType(SMBlocks.PURIFYING_FLOWER, RenderType.cutout());
    }

    private static void setBlockRenderType(Supplier<Block> blockSupplier, RenderType renderType) {
        RenderTypeLookup.setRenderLayer(blockSupplier.get(), renderType);
    }

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        BlockColors colors = event.getBlockColors();

        colors.register((state, blockDisplayReader, pos, color) -> blockDisplayReader != null && pos != null ? BiomeColors.getAverageGrassColor(blockDisplayReader, pos) : CORRUPTED_COLOR,
                SMBlocks.CORRUPTED_MASS.get(), SMBlocks.CORRUPTED_SOIL.get());
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        BlockColors blockColors = event.getBlockColors();

        itemColors.register((itemStack, color) -> {
            BlockState blockState = ((BlockItem)itemStack.getItem()).getBlock().defaultBlockState();
            return blockColors.getColor(blockState, null, null, color);
        }, SMBlocks.CORRUPTED_MASS.get(), SMBlocks.CORRUPTED_SOIL.get());
    }
}

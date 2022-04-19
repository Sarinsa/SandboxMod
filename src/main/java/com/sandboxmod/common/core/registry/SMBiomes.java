package com.sandboxmod.common.core.registry;

import com.sandboxmod.common.core.SandboxMod;
import com.sandboxmod.common.util.References;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.function.Supplier;

public class SMBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, SandboxMod.MODID);


    public static final RegistryObject<Biome> CORRUPTED_LANDS = register("corrupted_lands", SMBiomes::createCorruptedLands);


    private static RegistryObject<Biome> register(String name, Supplier<Biome> biomeSupplier) {
        return BIOMES.register(name, biomeSupplier);
    }

    private static Biome createCorruptedLands() {
        final int BIOME_COLOR = References.CORRUPTED_COLOR;

        BiomeGenerationSettings.Builder settings = new BiomeGenerationSettings.Builder();

        DefaultBiomeFeatures.addDefaultCarvers(settings);
        DefaultBiomeFeatures.addOceanCarvers(settings);
        DefaultBiomeFeatures.addDefaultOres(settings);
        DefaultBiomeFeatures.addDefaultLakes(settings);
        DefaultBiomeFeatures.addDefaultMonsterRoom(settings);
        DefaultBiomeFeatures.addDefaultOverworldLandStructures(settings);
        DefaultBiomeFeatures.addDefaultSoftDisks(settings);
        settings.surfaceBuilder(SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_GRASS));

        MobSpawnInfo.Builder spawnInfo = new MobSpawnInfo.Builder();

        BiomeAmbience.Builder ambience = new BiomeAmbience.Builder();

        ambience.foliageColorOverride(BIOME_COLOR);
        ambience.grassColorOverride(BIOME_COLOR);
        ambience.waterColor(BIOME_COLOR);
        ambience.fogColor(BIOME_COLOR);
        ambience.waterFogColor(BIOME_COLOR);
        ambience.skyColor(BIOME_COLOR);

        return new Biome.Builder()
                .biomeCategory(Biome.Category.NONE)
                .temperature(0.9F)
                .downfall(0.1F)
                .depth(0.8F)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .precipitation(Biome.RainType.RAIN)
                .scale(1.0F)
                .generationSettings(settings.build())
                .mobSpawnSettings(spawnInfo.build())
                .specialEffects(ambience.build())
                .build();
    }

    public static void registerBiomes() {
        registerBiome(CORRUPTED_LANDS, BiomeManager.BiomeType.WARM, 2);
    }

    private static void registerBiome(RegistryObject<Biome> registryObject, BiomeManager.BiomeType type, int weight) {
        BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(getRegistryKey(registryObject.get()), weight));
    }

    public static RegistryKey<Biome> getRegistryKey(Biome biome) {
        return ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getKey(((ForgeRegistry<Biome>)ForgeRegistries.BIOMES).getID(biome));
    }
}

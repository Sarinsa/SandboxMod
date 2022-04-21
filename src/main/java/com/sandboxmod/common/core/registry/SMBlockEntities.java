package com.sandboxmod.common.core.registry;

import com.sandboxmod.common.blockentity.PurifyingFlowerTileEntity;
import com.sandboxmod.common.core.SandboxMod;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMBlockEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SandboxMod.MODID);


    public static final RegistryObject<TileEntityType<PurifyingFlowerTileEntity>> PURIFYING_FLOWER = TILE_ENTITIES.register("purifying_flower",
            () -> TileEntityType.Builder.of(PurifyingFlowerTileEntity::new, SMBlocks.PURIFYING_FLOWER.get()).build(null));
}

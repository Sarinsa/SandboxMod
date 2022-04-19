package com.sandboxmod.common.core.registry;

import com.sandboxmod.common.block.CorruptedLogBlock;
import com.sandboxmod.common.block.CorruptedMassBlock;
import com.sandboxmod.common.block.PurityFlowerBlock;
import com.sandboxmod.common.core.SandboxMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SMBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SandboxMod.MODID);


    public static final RegistryObject<Block> CORRUPTED_MASS = register("corrupted_mass", CorruptedMassBlock::new, ItemGroup.TAB_DECORATIONS);
    public static final RegistryObject<Block> CORRUPTED_LOG = register("corrupted_log", CorruptedLogBlock::new, ItemGroup.TAB_DECORATIONS);

    public static final RegistryObject<Block> PURIFYING_FLOWER = register("purifying_flower", PurityFlowerBlock::new, ItemGroup.TAB_DECORATIONS);


    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, ItemGroup itemGroup) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        SMItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(itemGroup)));
        return block;
    }

    private static <T extends Block> RegistryObject<T> registerNoBlockItem(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }
}

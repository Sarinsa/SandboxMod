package com.sandboxmod.common.core.registry;

import com.sandboxmod.common.block.CorruptedMassBlock;
import com.sandboxmod.common.core.SandboxMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SMBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SandboxMod.MODID);


    public static final RegistryObject<Block> CORRUPTED_MASS = register("corrupted_mass", CorruptedMassBlock::new, ItemGroup.TAB_MISC);


    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, ItemGroup itemGroup) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        SMItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(itemGroup)));
        return block;
    }

    private static <T extends Block> RegistryObject<T> registerNoBlockItem(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }
}

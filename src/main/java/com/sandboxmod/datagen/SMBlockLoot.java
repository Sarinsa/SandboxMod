package com.sandboxmod.datagen;

import com.sandboxmod.common.core.registry.SMBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootTable;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;
import java.util.Set;

public class SMBlockLoot extends BlockLootTables {

    private final Set<Block> knownBlocks = new HashSet<>();

    protected void addTables() {
        this.add(SMBlocks.CORRUPTED_MASS.get(), noDrop());
        this.dropOther(SMBlocks.CORRUPTED_SOIL.get(), Blocks.DIRT);
        this.dropSelf(SMBlocks.CORRUPTED_LOG.get());
        this.dropSelf(SMBlocks.PURIFYING_FLOWER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return this.knownBlocks;
    }

    @Override
    protected void add(Block block, LootTable.Builder table) {
        super.add(block, table);
        this.knownBlocks.add(block);
    }
}

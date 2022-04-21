package com.sandboxmod.common.tag;

import com.sandboxmod.common.core.SandboxMod;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionReader;
import net.minecraftforge.common.Tags;

public class SMBlockTags {

    public static final Tags.IOptionalNamedTag<Block> CORRUPTIBLE_BLOCKS = modTag("corruptible_blocks");


    private static Tags.IOptionalNamedTag<Block> modTag(String tagName) {
        return BlockTags.createOptional(SandboxMod.resourceLoc(tagName));
    }

    // Class loading
    public static void init() {}
}

package com.sandboxmod.common.tag;

import com.sandboxmod.common.core.SandboxMod;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class SMBlockTags {

    public static final ITag.INamedTag<Block> CORRUPTIBLE_BLOCKS = modTag("corruptible_blocks");


    private static ITag.INamedTag<Block> modTag(String tagName) {
        return BlockTags.createOptional(SandboxMod.resourceLoc(tagName));
    }
}

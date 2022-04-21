package com.sandboxmod.common.util;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockHelper {

    public static class Materials {

        public static final Material CORRUPTED_MASS = new Material.Builder(MaterialColor.COLOR_PURPLE).noCollider().nonSolid().build();
        public static final Material CORRUPTED_DIRT = new Material.Builder(MaterialColor.COLOR_PURPLE).build();
    }
}

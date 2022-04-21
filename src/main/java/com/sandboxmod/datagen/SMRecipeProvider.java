package com.sandboxmod.datagen;

import com.sandboxmod.common.core.registry.SMBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SMRecipeProvider extends RecipeProvider {

    public SMRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(SMBlocks.PURIFYING_FLOWER.get())
                .pattern(" G ")
                .pattern("GDG")
                .pattern(" T ")
                .define('G', Tags.Items.DUSTS_GLOWSTONE)
                .define('D', Items.OXEYE_DAISY)
                .define('T', Items.GHAST_TEAR)
                .unlockedBy("has_ghast_tear", has(Items.GHAST_TEAR))
                .unlockedBy("has_glowstone", has(Tags.Items.DUSTS_GLOWSTONE))
                .save(consumer);
    }
}

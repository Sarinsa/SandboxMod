package com.sandboxmod.common.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class SMCommonConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    public static class Common {

        private final ForgeConfigSpec.IntValue purifyingFlowerTickInterval;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            this.purifyingFlowerTickInterval = builder.comment("Decides the interval in which the purifying daisy will attempt to cleanse a nearby spot (in ticks). A value higher than 20 ticks will generally not be effective against corruption with default random tick speed, and very low values may lag the game.")
                            .defineInRange("purifyingFlowerTickInterval", 20, 1, 1000);

            builder.pop();
        }

        public int getFlowerTickInterval() {
            return this.purifyingFlowerTickInterval.get();
        }
    }
}

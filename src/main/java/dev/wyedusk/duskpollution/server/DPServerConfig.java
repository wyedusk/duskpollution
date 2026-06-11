package dev.wyedusk.duskpollution.server;

import dev.wyedusk.duskpollution.DuskPollution;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DuskPollution.MODID)
public class DPServerConfig implements IModBusEvent {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue GAS_MAXIMUM_HEIGHT = BUILDER
            .comment("The maximum Y level that gas can float up to before stopping.")
            .defineInRange("gasMaximumHeight", 195, 128, 319);

    private static final ModConfigSpec.IntValue MOVEMENTS_BEFORE_GAS_CAN_DISSIPATE = BUILDER
            .comment("The amount of times a gas in the air can move before it starts being able to dissipate. Set to -1 to disable natural dissipation.")
            .defineInRange("movementsBeforeGasCanDissipate", 350, -1, Integer.MAX_VALUE);
    private static final ModConfigSpec.ConfigValue<Float> BASE_DISSIPATION_CHANCE = BUILDER
            .comment("The default percentage chance for a gas to dissipate.\n Default: 25.0\n Range: 0.0 ~ 100.0")
            .define("baseDissipationChance", 25.0f, DPServerConfig::percentValidator);
    private static final ModConfigSpec.ConfigValue<Float> DISSIPATION_CHANCE_INCREASE = BUILDER
            .comment("The amount to increase the dissipation chance by each time a gas moves without dissipating.\n Default: 0.5\n Range: 0.0 ~ 100.0")
            .define("dissipationChanceIncrease", 0.5f, DPServerConfig::percentValidator);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean percentValidator(
            Object value) {
        if (value instanceof Float floatValue) return floatValue > 0.0f && floatValue < 100.0f;
        return false;
    }

    public static int gasMaximumHeight;

    public static int movementsBeforeGasCanDissipate;
    public static float baseDissipationChance;
    public static float dissipationChanceIncrease;
    public static boolean gasCanNaturallyDissipate;

    @SubscribeEvent
    static void onLoad(
            final ModConfigEvent event) {
        gasMaximumHeight = GAS_MAXIMUM_HEIGHT.getAsInt();

        movementsBeforeGasCanDissipate = MOVEMENTS_BEFORE_GAS_CAN_DISSIPATE.getAsInt();
        baseDissipationChance = BASE_DISSIPATION_CHANCE.get();
        dissipationChanceIncrease = DISSIPATION_CHANCE_INCREASE.get();
        gasCanNaturallyDissipate = movementsBeforeGasCanDissipate != -1;
    }
}
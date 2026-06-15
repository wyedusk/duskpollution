package dev.wyedusk.duskpollution.pollution;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public record PollutionEmitterData(
        Map<String, String> conditions,
        int pollutionRelease,
        int toxicPollutionRelease,
        int pollutionInterval,
        int toxicPollutionInterval
) {
    public static final Codec<PollutionEmitterData> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("conditions")
                                    .forGetter(PollutionEmitterData::conditions),
                            Codec.INT.fieldOf("pollution_release")
                                    .forGetter(PollutionEmitterData::pollutionRelease),
                            Codec.INT.fieldOf("toxic_pollution_release")
                                    .forGetter(PollutionEmitterData::toxicPollutionRelease),
                            Codec.INT.fieldOf("pollution_interval")
                                    .forGetter(PollutionEmitterData::pollutionInterval),
                            Codec.INT.fieldOf("toxic_pollution_interval")
                                    .forGetter(PollutionEmitterData::toxicPollutionInterval)
                    ).apply(instance, PollutionEmitterData::new)
            );
}

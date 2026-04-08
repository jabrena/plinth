package info.jab.ms.service;

import info.jab.ms.api.model.PantheonSource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

final class InMemoryPantheonDataSource implements PantheonDataSource {

    private static final Map<PantheonSource, List<GodData>> FIXTURES = Map.of(
        PantheonSource.GREEK,
        List.of(
            new GodData("nyx", new BigInteger("30000000000000000000")),
            new GodData("zeus", new BigInteger("123"))
        ),
        PantheonSource.ROMAN,
        List.of(
            new GodData("neptune", new BigInteger("20000000000000000000")),
            new GodData("mars", new BigInteger("456"))
        ),
        PantheonSource.NORDIC,
        List.of(
            new GodData("njord", new BigInteger("28179288397447443426")),
            new GodData("odin", new BigInteger("789"))
        )
    );

    @Override
    public List<GodData> fetch(PantheonSource source) {
        return FIXTURES.getOrDefault(source, List.of());
    }
}

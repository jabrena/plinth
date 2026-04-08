package info.jab.ms.service;

import info.jab.ms.api.model.PantheonSource;
import java.util.List;

public interface PantheonDataSource {

    List<GodData> fetch(PantheonSource source);
}

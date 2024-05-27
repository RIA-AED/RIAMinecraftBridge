package ink.magma.riaserverapi.record;

import java.util.Collection;

public record Plugin(String name, String version, Collection<String> authors, String description) {
}

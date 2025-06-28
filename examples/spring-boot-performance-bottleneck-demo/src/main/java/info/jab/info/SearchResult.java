package info.jab.info;

import java.util.List;

record SearchResult(
    List<User> users,
    long executionTimeMs,
    String algorithm,
    int totalComparisons
) {
}

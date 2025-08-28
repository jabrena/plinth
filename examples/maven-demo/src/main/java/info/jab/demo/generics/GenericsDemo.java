package info.jab.demo.generics;

/**
 * Main demonstration class for all advanced generics examples.
 * This class showcases non-trivial generics usage patterns that go beyond
 * basic parameterized types and provide real value in complex scenarios.
 */
public class GenericsDemo {

    public static void main(String[] args) {
        System.out.println("=== Advanced Java Generics Demonstration ===\n");

        try {
            System.out.println("1. Complex Bounded Generics Examples:");
            System.out.println("------------------------------------");
            ComplexBoundedGenerics.demonstrateComplexScenarios();
            System.out.println();

            System.out.println("2. Self-Bounded Generics (CRTP) Examples:");
            System.out.println("-----------------------------------------");
            SelfBoundedGenerics.demonstrateSelfBoundedPatterns();
            System.out.println();

            System.out.println("3. Type Erasure Workarounds:");
            System.out.println("----------------------------");
            TypeErasureWorkarounds.demonstrateTypeErasureWorkarounds();
            System.out.println();

            System.out.println("4. Modern Java Generics (Records, Sealed Types, Pattern Matching):");
            System.out.println("-------------------------------------------------------------------");
            ModernJavaGenerics.demonstrateModernJavaGenerics();
            System.out.println();

            System.out.println("5. Edge Cases and Advanced Scenarios:");
            System.out.println("-------------------------------------");
            EdgeCaseGenerics.demonstrateEdgeCases();
            System.out.println();

            System.out.println("=== All Generics Demonstrations Completed Successfully ===");

        } catch (Exception e) {
            System.err.println("Error during demonstration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Utility method to demonstrate the value proposition of these advanced generics patterns.
     */
    public static void explainGenericsValue() {
        System.out.println("\n=== Why These Advanced Generics Patterns Matter ===");
        System.out.println();

        System.out.println("1. COMPLEX BOUNDED GENERICS:");
        System.out.println("   - Enable type-safe operations on constrained types");
        System.out.println("   - Provide compile-time guarantees about available methods");
        System.out.println("   - Allow for more flexible and reusable APIs");
        System.out.println();

        System.out.println("2. SELF-BOUNDED GENERICS (CRTP):");
        System.out.println("   - Enable fluent builders that maintain type safety across inheritance");
        System.out.println("   - Allow base classes to return specific subtype instances");
        System.out.println("   - Support method chaining without losing type information");
        System.out.println();

        System.out.println("3. TYPE ERASURE WORKAROUNDS:");
        System.out.println("   - Preserve type information at runtime where needed");
        System.out.println("   - Enable type-safe operations on erased types");
        System.out.println("   - Support frameworks that need runtime type information");
        System.out.println();

        System.out.println("4. MODERN JAVA GENERICS:");
        System.out.println("   - Combine generics with Records for immutable, type-safe data");
        System.out.println("   - Use sealed types to constrain generic hierarchies");
        System.out.println("   - Leverage pattern matching for type-safe conditional logic");
        System.out.println();

        System.out.println("5. EDGE CASES AND ADVANCED SCENARIOS:");
        System.out.println("   - Handle wildcard capture for flexible APIs");
        System.out.println("   - Prevent heap pollution with proper varargs usage");
        System.out.println("   - Create type-safe heterogeneous containers");
        System.out.println("   - Navigate complex variance scenarios");
        System.out.println();

        System.out.println("These patterns represent the difference between:");
        System.out.println("• BASIC GENERICS: List<String> instead of List");
        System.out.println("• ADVANCED GENERICS: Type-safe, flexible, maintainable APIs");
        System.out.println("  that eliminate ClassCastException, provide better IDE support,");
        System.out.println("  and enable more expressive domain models.");
    }
}

package info.jab.generics.examples;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Advanced examples demonstrating type erasure workarounds, type tokens,
 * and techniques for preserving type information at runtime.
 */
public class TypeErasureWorkarounds {

    // Type token pattern for preserving generic type information
    public abstract static class TypeToken<T> {
        private final Type type;
        private final Class<T> rawType;

        @SuppressWarnings("unchecked")
        protected TypeToken() {
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
                this.rawType = (Class<T>) getRawType(type);
            } else {
                throw new IllegalArgumentException("TypeToken must be parameterized");
            }
        }

        public Type getType() {
            return type;
        }

        public Class<T> getRawType() {
            return rawType;
        }

        private static Class<?> getRawType(Type type) {
            if (type instanceof Class<?>) {
                return (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) type).getRawType();
            } else if (type instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) type).getGenericComponentType();
                return Array.newInstance(getRawType(componentType), 0).getClass();
            } else {
                throw new IllegalArgumentException("Cannot determine raw type for " + type);
            }
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TypeToken<?> &&
                   ((TypeToken<?>) obj).type.equals(this.type);
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }

        @Override
        public String toString() {
            return "TypeToken<" + type.getTypeName() + ">";
        }
    }

    // Typesafe heterogeneous container using Class as type token
    public static class TypeSafeContainer {
        private final Map<Class<?>, Object> container = new ConcurrentHashMap<>();

        public <T> void put(Class<T> type, T instance) {
            Objects.requireNonNull(type, "Type cannot be null");
            container.put(type, type.cast(instance));
        }

        public <T> T get(Class<T> type) {
            return type.cast(container.get(type));
        }

        public <T> boolean contains(Class<T> type) {
            return container.containsKey(type);
        }

        public <T> T remove(Class<T> type) {
            return type.cast(container.remove(type));
        }

        public Set<Class<?>> getStoredTypes() {
            return new HashSet<>(container.keySet());
        }

        // Advanced method with type hierarchy support
        @SuppressWarnings("unchecked")
        public <T> List<T> getInstancesOfType(Class<T> type) {
            List<T> instances = new ArrayList<>();
            for (Map.Entry<Class<?>, Object> entry : container.entrySet()) {
                if (type.isAssignableFrom(entry.getKey())) {
                    instances.add((T) entry.getValue());
                }
            }
            return instances;
        }
    }

    // Generic array factory that works around erasure
    public static class GenericArrayFactory {

        @SuppressWarnings("unchecked")
        public static <T> T[] createArray(Class<T> componentType, int size) {
            return (T[]) Array.newInstance(componentType, size);
        }

        public static <T> T[] createArray(TypeToken<T[]> arrayTypeToken, int size) {
            Type type = arrayTypeToken.getType();
            if (type instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) type).getGenericComponentType();
                Class<?> componentClass = TypeToken.getRawType(componentType);
                return createArray((Class<T>) componentClass, size);
            } else if (type instanceof Class && ((Class<?>) type).isArray()) {
                Class<?> componentClass = ((Class<?>) type).getComponentType();
                return createArray((Class<T>) componentClass, size);
            } else {
                throw new IllegalArgumentException("Type is not an array type: " + type);
            }
        }

        // Utility method for creating parameterized collections
        @SuppressWarnings("unchecked")
        public static <T> List<T> createList(Class<T> elementType) {
            return new ArrayList<>();
        }

        public static <K, V> Map<K, V> createMap(Class<K> keyType, Class<V> valueType) {
            return new HashMap<>();
        }
    }

    // Generic factory with type preservation
    public static class TypePreservingFactory<T> {
        private final Class<T> type;
        private final Map<String, Function<Object[], T>> constructors;

        public TypePreservingFactory(Class<T> type) {
            this.type = Objects.requireNonNull(type);
            this.constructors = new HashMap<>();
            registerDefaultConstructors();
        }

        private void registerDefaultConstructors() {
            // Register no-arg constructor if available
            try {
                Constructor<T> noArgConstructor = type.getDeclaredConstructor();
                constructors.put("default", args -> {
                    try {
                        return noArgConstructor.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create instance", e);
                    }
                });
            } catch (NoSuchMethodException e) {
                // No default constructor available
            }
        }

        public TypePreservingFactory<T> registerConstructor(String name,
                Function<Object[], T> constructor) {
            constructors.put(name, constructor);
            return this;
        }

        public <P1> TypePreservingFactory<T> registerConstructor(String name,
                Class<P1> param1Type,
                Function<P1, T> constructor) {
            constructors.put(name, args -> {
                if (args.length != 1) {
                    throw new IllegalArgumentException("Expected 1 argument");
                }
                P1 p1 = param1Type.cast(args[0]);
                return constructor.apply(p1);
            });
            return this;
        }

        public <P1, P2> TypePreservingFactory<T> registerConstructor(String name,
                Class<P1> param1Type, Class<P2> param2Type,
                java.util.function.BiFunction<P1, P2, T> constructor) {
            constructors.put(name, args -> {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Expected 2 arguments");
                }
                P1 p1 = param1Type.cast(args[0]);
                P2 p2 = param2Type.cast(args[1]);
                return constructor.apply(p1, p2);
            });
            return this;
        }

        public T create(String constructorName, Object... args) {
            Function<Object[], T> constructor = constructors.get(constructorName);
            if (constructor == null) {
                throw new IllegalArgumentException("No constructor registered with name: " + constructorName);
            }
            return constructor.apply(args);
        }

        public T createDefault() {
            return create("default");
        }

        public Class<T> getType() {
            return type;
        }

        public boolean canCreate(String constructorName) {
            return constructors.containsKey(constructorName);
        }
    }

    // Generic serialization helper that preserves type information
    public static class TypeAwareSerializer {
        private final Map<TypeToken<?>, Function<Object, String>> serializers = new HashMap<>();
        private final Map<TypeToken<?>, Function<String, Object>> deserializers = new HashMap<>();

        public <T> TypeAwareSerializer registerType(TypeToken<T> typeToken,
                Function<T, String> serializer,
                Function<String, T> deserializer) {
            serializers.put(typeToken, (Function<Object, String>) serializer);
            deserializers.put(typeToken, (Function<String, Object>) deserializer);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> String serialize(T object, TypeToken<T> typeToken) {
            Function<Object, String> serializer = serializers.get(typeToken);
            if (serializer == null) {
                throw new IllegalArgumentException("No serializer registered for type: " + typeToken);
            }
            return serializer.apply(object);
        }

        @SuppressWarnings("unchecked")
        public <T> T deserialize(String data, TypeToken<T> typeToken) {
            Function<String, Object> deserializer = deserializers.get(typeToken);
            if (deserializer == null) {
                throw new IllegalArgumentException("No deserializer registered for type: " + typeToken);
            }
            return (T) deserializer.apply(data);
        }
    }

    // Safe generic collection conversion utilities
    public static class SafeCollectionConverter {

        @SuppressWarnings("unchecked")
        public static <T> List<T> castList(List<?> list, Class<T> elementType) {
            for (Object item : list) {
                if (item != null && !elementType.isInstance(item)) {
                    throw new ClassCastException(
                        "List contains element of type " + item.getClass().getName() +
                        " which is not assignable to " + elementType.getName()
                    );
                }
            }
            return (List<T>) list;
        }

        @SuppressWarnings("unchecked")
        public static <K, V> Map<K, V> castMap(Map<?, ?> map, Class<K> keyType, Class<V> valueType) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();

                if (key != null && !keyType.isInstance(key)) {
                    throw new ClassCastException(
                        "Map contains key of type " + key.getClass().getName() +
                        " which is not assignable to " + keyType.getName()
                    );
                }

                if (value != null && !valueType.isInstance(value)) {
                    throw new ClassCastException(
                        "Map contains value of type " + value.getClass().getName() +
                        " which is not assignable to " + valueType.getName()
                    );
                }
            }
            return (Map<K, V>) map;
        }

        public static <T> Set<T> castSet(Set<?> set, Class<T> elementType) {
            return new HashSet<>(castList(new ArrayList<>(set), elementType));
        }
    }

    // Reflection-based generic method invoker
    public static class GenericMethodInvoker {

        public static <T> T invokeGenericMethod(Object target, String methodName,
                Class<T> returnType, Object... args) {
            try {
                Class<?> targetClass = target.getClass();
                Method[] methods = targetClass.getMethods();

                // First try to find exact parameter type match
                for (Method method : methods) {
                    if (method.getName().equals(methodName) &&
                        method.getParameterCount() == args.length) {

                        Class<?>[] paramTypes = method.getParameterTypes();
                        boolean matches = true;

                        for (int i = 0; i < args.length; i++) {
                            if (args[i] != null && !paramTypes[i].isAssignableFrom(args[i].getClass())) {
                                matches = false;
                                break;
                            }
                        }

                        if (matches) {
                            Object result = method.invoke(target, args);
                            return returnType.cast(result);
                        }
                    }
                }

                throw new NoSuchMethodException("Method not found: " + methodName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke generic method", e);
            }
        }

        @SuppressWarnings("unchecked")
        public static <T> T invokeGenericStaticMethod(Class<?> targetClass, String methodName,
                Class<T> returnType, Object... args) {
            try {
                Method[] methods = targetClass.getMethods();

                for (Method method : methods) {
                    if (Modifier.isStatic(method.getModifiers()) &&
                        method.getName().equals(methodName) &&
                        method.getParameterCount() == args.length) {

                        Object result = method.invoke(null, args);
                        return returnType.cast(result);
                    }
                }

                throw new NoSuchMethodException("Static method not found: " + methodName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke static generic method", e);
            }
        }
    }

    // Demonstration methods
    public static void demonstrateTypeErasureWorkarounds() {
        // Type token usage
        TypeToken<List<String>> listToken = new TypeToken<List<String>>() {};
        TypeToken<Map<String, Integer>> mapToken = new TypeToken<Map<String, Integer>>() {};

        System.out.println("List token type: " + listToken.getType());
        System.out.println("Map token type: " + mapToken.getType());

        // Typesafe heterogeneous container
        TypeSafeContainer container = new TypeSafeContainer();
        container.put(String.class, "Hello World");
        container.put(Integer.class, 42);
        container.put(List.class, Arrays.asList(1, 2, 3));

        String str = container.get(String.class);
        Integer num = container.get(Integer.class);

        // Generic array creation
        String[] stringArray = GenericArrayFactory.createArray(String.class, 5);
        stringArray[0] = "Hello";

        List<String> stringList = GenericArrayFactory.createList(String.class);
        Map<String, Integer> stringIntMap = GenericArrayFactory.createMap(String.class, Integer.class);

        // Type preserving factory
        TypePreservingFactory<StringBuilder> sbFactory = new TypePreservingFactory<>(StringBuilder.class);
        sbFactory.registerConstructor("withString", String.class, StringBuilder::new);
        sbFactory.registerConstructor("withCapacity", Integer.class, StringBuilder::new);

        StringBuilder sb1 = sbFactory.create("withString", "Hello");
        StringBuilder sb2 = sbFactory.create("withCapacity", 100);

        // Safe collection conversion
        List<Object> objectList = Arrays.asList("a", "b", "c");
        List<String> stringListConverted = SafeCollectionConverter.castList(objectList, String.class);

        // Type-aware serialization
        TypeAwareSerializer serializer = new TypeAwareSerializer();
        serializer.registerType(
            new TypeToken<List<String>>() {},
            list -> String.join(",", list),
            data -> Arrays.asList(data.split(","))
        );

        List<String> testList = Arrays.asList("apple", "banana", "cherry");
        String serialized = serializer.serialize(testList, new TypeToken<List<String>>() {});
        List<String> deserialized = serializer.deserialize(serialized, new TypeToken<List<String>>() {});

        System.out.println("Type erasure workarounds demonstration completed");
        System.out.println("Container contains String: " + container.contains(String.class));
        System.out.println("String from container: " + str);
        System.out.println("Number from container: " + num);
        System.out.println("Array length: " + stringArray.length);
        System.out.println("StringBuilder 1: " + sb1);
        System.out.println("StringBuilder 2 capacity: " + sb2.capacity());
        System.out.println("Converted list: " + stringListConverted);
        System.out.println("Serialized: " + serialized);
        System.out.println("Deserialized: " + deserialized);
    }
}

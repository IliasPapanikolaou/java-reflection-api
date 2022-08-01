package com.unipi.ipap.javareflectionapi;

import com.unipi.ipap.javareflectionapi.model.EnemyGunShip;
import com.unipi.ipap.javareflectionapi.model.EnemyShip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class JavaReflectionApiApplicationTest {

    @BeforeAll
    static void setUp() {
        System.out.println("Unit Testing Reflection API");
        System.out.println("---------------------------");
    }

    @AfterEach
    void separationLine() {
        System.out.println("---------------------------");
    }

    @Test
    @DisplayName("Get Class name")
    void testClassReflection() {
        Class<EnemyGunShip> reflectClass = EnemyGunShip.class;
        String className = reflectClass.getName();

        System.out.println(className);

        assertEquals("com.unipi.ipap.javareflectionapi.model.EnemyGunShip", className);
    }

    @Test
    @DisplayName("Get Class modifiers")
    void testClassModifiers() {
        Class<EnemyGunShip> reflectClass = EnemyGunShip.class;
        int classModifier = reflectClass.getModifiers();

        System.out.println(classModifier);

        // Class modifiers
        assertAll(() -> {
            assertTrue(Modifier.isPublic(classModifier));
            assertFalse(Modifier.isPrivate(classModifier));
            assertFalse(Modifier.isProtected(classModifier));
            assertFalse(Modifier.isAbstract(classModifier));
            assertFalse(Modifier.isFinal(classModifier));
            assertFalse(Modifier.isStatic(classModifier));
            assertFalse(Modifier.isStrict(classModifier));
            assertFalse(Modifier.isSynchronized(classModifier));
            assertFalse(Modifier.isVolatile(classModifier));
        });
    }

    @Test
    @DisplayName("Array of Interfaces")
    void testGetArrayOfInterfaces() {
        Class<EnemyGunShip> reflectClass = EnemyGunShip.class;
        Class<?>[] interfaces = reflectClass.getInterfaces();

        System.out.println(interfaces.length);

        assertEquals(0, interfaces.length);
    }

    @Test
    @DisplayName("Get Super Class")
    void testGetSuperClass() {
        Class<EnemyGunShip> reflectClass = EnemyGunShip.class;
        Class<? super EnemyGunShip> superClass = reflectClass.getSuperclass();

        System.out.println(superClass.getName());

        assertAll(() -> {
            assertEquals(EnemyShip.class, superClass);
            assertEquals("com.unipi.ipap.javareflectionapi.model.EnemyShip", superClass.getName());
        });
    }

    @Test
    @DisplayName("Get Methods of a Class")
    void testGetMethodsOfClass() {
        Class<EnemyGunShip> reflectClass = EnemyGunShip.class;
        Method[] methods = reflectClass.getMethods();

        Arrays.stream(methods).forEach(System.out::println);

        List<Method> getMethods = Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("get"))
                .toList();

        List<Method> setMethods = Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("set"))
                .toList();

        // Collect different return types of methods
        Set<String> methodTypes = Arrays.stream(methods)
                .map(method -> {
                    System.out.println(method.getReturnType());
                    return method.getReturnType().toString();
                })
                .collect(Collectors.toSet());

        // Collect different parameters for each method
        Set<String> methodParameters = Arrays.stream(methods)
                .map(method -> {
                    Parameter[] parameters = method.getParameters();
                    return Arrays.stream(parameters)
                            .map(Parameter::getName)
                            .collect(Collectors.toSet());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        System.out.println("---------------------------");
        methodParameters.forEach(System.out::println);

        assertAll(() -> {
            assertEquals(13, methods.length);
            assertEquals(3, getMethods.size());
            assertEquals(2, setMethods.size());
            assertEquals(6, methodTypes.size());
            assertEquals(2, methodParameters.size());
        });
    }

    @Test
    @DisplayName("Constructor and Object Creation")
    void testConstructorAndObjectCreation() {

        Class<EnemyGunShip> reflectClass = EnemyGunShip.class;

        Constructor<?> constructor;
        Object object;

        // Create object
        try {
            constructor = reflectClass.getConstructor(String.class, Double.class);
            object = reflectClass.getConstructor(String.class, Double.class).newInstance("Tie-Fighter", 200D);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Get Parameters of constructor
        Class<?>[] constructParameters = constructor.getParameterTypes();
        Arrays.stream(constructParameters).forEach(System.out::println);

        // Cast object to another class so we can get class methods
        EnemyShip enemyShip;
        EnemyGunShip enemyGunShip;
        try {
            enemyShip = (EnemyShip) constructor.newInstance("X-Wing", 210D);
            enemyGunShip = (EnemyGunShip) object;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Class methods are present
        assertAll(() -> {
            assertEquals("X-Wing", enemyShip.getName());
            assertEquals(210D, enemyShip.getSpeed());
            assertEquals("Tie-Fighter", enemyGunShip.getName());
            assertEquals(200D, enemyGunShip.getSpeed());
        });
    }

    @Test
    @DisplayName("Access Private Field")
    void testAccessPrivateField() {

        Field privateStringName;
        String valueOfName;
        EnemyGunShip enemyGunShip = new EnemyGunShip("X-Wing", 210D);

        try {
            String idCodeString = "idCode";
            privateStringName = EnemyGunShip.class.getDeclaredField(idCodeString);

            // Shut down security
            privateStringName.setAccessible(true);

            valueOfName = (String) privateStringName.get(enemyGunShip);

            // Accessing private field!!!
            System.out.println(valueOfName);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals(String.valueOf(100),  valueOfName);
    }

    @Test
    @DisplayName("Invoke Private Method")
    void testInvokePrivateMethod() {

        Method privateMethod;
        String privateReturnValue;
        EnemyGunShip enemyGunShip = new EnemyGunShip("X-Wing", 210D);

        try {
            String methodsName = "getPrivate";
            privateMethod = EnemyGunShip.class.getDeclaredMethod(methodsName);

            // Shut down security
            privateMethod.setAccessible(true);
            privateReturnValue = (String) privateMethod.invoke(enemyGunShip, null);

            // Invoking private method!!!
            System.out.println(privateReturnValue);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertEquals("How did you get this?", privateReturnValue);
    }

    @Test
    @DisplayName("Invoke Private Method with parameters")
    void testInvokePrivateMethodWithParameters() {

        Method privateMethod;
        String privateReturnValue;
        EnemyGunShip enemyGunShip = new EnemyGunShip("X-Wing", 210D);

        Class<?>[] methodParameters = new Class[] { Integer.TYPE, String.class };
        Object[] params = new Object[] { 10, "Oh deer!" };

        try {
            privateMethod = EnemyGunShip.class.getDeclaredMethod("getOtherPrivate", methodParameters);

            // Shut down security
            privateMethod.setAccessible(true);
            privateReturnValue = (String) privateMethod.invoke(enemyGunShip, params);

            // Invoking private method!!!
            System.out.println(privateReturnValue);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertEquals("How did you get here 10 Oh deer!?", privateReturnValue);
    }


}

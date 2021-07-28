package com.luxoft.ioc.appcontainer;

import com.luxoft.ioc.appcontainer.api.AppComponent;
import com.luxoft.ioc.appcontainer.api.AppComponentsContainer;
import com.luxoft.ioc.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var config = createConfigInstance(configClass);

        var methods = findAppComponentMethods(configClass);
        sortByOrderAppComponentMethods(methods);

        methods.forEach(method -> {
            var methodAnnotation = method.getAnnotation(AppComponent.class);

            var componentsForInjection = findComponentsForInjection(method);
            var component = createComponentInstance(method, config, componentsForInjection);

            var componentName = methodAnnotation.name();
            var componentClass = component.getClass();
            var componentReturnClass = method.getReturnType();

            List<String> componentAliases = List.of(
                componentName,
                componentClass.toString(),
                componentReturnClass.toString()
            );

            putComponentToComponents(component, componentAliases);
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Method> findAppComponentMethods(Class<?> configClass) {
        List<Method> methods = new ArrayList<>();
        for (var method : configClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(AppComponent.class)) {
                continue;
            }

            methods.add(method);
        }

        return methods;
    }

    private void sortByOrderAppComponentMethods(List<Method> methods) {
        methods.sort((m1, m2) -> {
            var a1 = m1.getAnnotation(AppComponent.class);
            var a2 = m2.getAnnotation(AppComponent.class);

            return Integer.compare(a1.order(), a2.order());
        });
    }

    private Object[] findComponentsForInjection(Method method) {
        var methodParameters = method.getParameters();

        var componentsForInjection = new Object[methodParameters.length];
        for (var i = 0; i < methodParameters.length; i++) {
            var parameterType = methodParameters[i].getType();

            var componentForInjection = appComponentsByName.get(parameterType.toString());
            if (componentForInjection == null) {
                var errorMessage = "If a component used as a parameter, its should be created before. Check ordering";
                throw new AppComponentNotFoundException(errorMessage);
            }

            componentsForInjection[i] = componentForInjection;
        }

        return componentsForInjection;
    }

    private Object createComponentInstance(Method method, Object config, Object[] componentsForInjection) {
        try {
            return method.invoke(config, componentsForInjection);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unexpected exception");
        }
    }

    private Object createConfigInstance(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (SecurityException | ReflectiveOperationException e) {
            throw new RuntimeException("Unexpected exception");
        }
    }

    private void putComponentToComponents(Object component, List<String> componentAliases) {
        componentAliases.forEach(alias -> appComponentsByName.put(alias, component));
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var component = appComponentsByName.get(componentClass.toString());
        if (component == null) {
            throw new AppComponentNotFoundException("No such component found");
        }

        return (C) component;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new AppComponentNotFoundException("No such component found");
        }

        return (C) component;
    }
}

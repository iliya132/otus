package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        var configInstance = tryInitializeConfigClass(configClass);
        var methods = configClass.getDeclaredMethods();
        List<Component> parsedComponents = new ArrayList<>(methods.length);
        for (Method method : methods) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                var metadata = method.getAnnotation(AppComponent.class);
                parsedComponents.add(new Component(metadata.order(), method, metadata.name()));
            }
        }

        var orderedComponents = parsedComponents.stream()
                .sorted(Comparator.comparingInt(Component::order))
                .toList();
        for (var component : orderedComponents) {
            var method = component.method();
            if (method.getParameterCount() == 0) {
                Object newComponent;
                try {
                    newComponent = method.invoke(configInstance);
                } catch (ReflectiveOperationException e) {
                    throw new ConfigurationException(e);
                }

                if (appComponentsByName.containsKey(component.name())) {
                    appComponentsByName.remove(component.name());
                    throw new ConfigurationException("found 2 components with same name.");
                }
                appComponents.add(newComponent);
                appComponentsByName.put(component.name(), newComponent);
            } else {
                var requiredTypes = method.getParameterTypes();
                List<Object> foundComponent = new ArrayList<>();
                for (var requiredType : requiredTypes) {
                    for (var registeredComponent : appComponents) {
                        if (requiredType.isAssignableFrom(registeredComponent.getClass())) {
                            foundComponent.add(registeredComponent);
                        }
                    }
                }
                if (foundComponent.size() != requiredTypes.length) {
                    throw new ConfigurationException("unable to configure " + configClass.getName());
                }
                Object newComponent;
                try {
                    System.out.println(foundComponent);
                    System.out.println(foundComponent.getClass());
                    newComponent = method.invoke(configInstance, foundComponent.toArray(new Object[0]));
                } catch (ReflectiveOperationException e) {
                    throw new ConfigurationException(e);
                }

                if (appComponentsByName.containsKey(component.name())) {
                    appComponentsByName.remove(component.name());
                    throw new ConfigurationException("found 2 components with same name.");
                }
                appComponents.add(newComponent);
                appComponentsByName.put(component.name(), newComponent);
            }
        }
    }

    private Object tryInitializeConfigClass(Class<?> configClass) {
        var maybeConstructor = Arrays.stream(configClass.getDeclaredConstructors())
                .findFirst();
        if (maybeConstructor.isEmpty()) {
            throw new ConfigurationException("Cant find default constructor for " + configClass.getName());
        }
        var constructor = maybeConstructor.get();
        if (constructor.getParameterCount() == 0) {
            return instantiate(constructor, null);
        } else {
            var requiredTypes = constructor.getParameterTypes();
            List<Object> arguments = new ArrayList<>();
            for (var reqType : requiredTypes) {
                if (appComponentsByName.containsKey(reqType.getName())) {
                    arguments.add(appComponentsByName.get(reqType.getName()));
                } else {
                    throw new ConfigurationException("unable to configure " +
                            configClass.getName() +
                            " cause of leak dependency: " + reqType.getName());
                }
            }
            return instantiate(constructor, arguments);
        }
    }

    private static Object instantiate(Constructor<?> constructor, List<Object> arguments) {
        try {
            if (arguments == null || arguments.isEmpty()) {
                return constructor.newInstance();
            } else {
                return constructor.newInstance(arguments.toArray(new Object[0]));
            }
        } catch (ReflectiveOperationException e) {
            throw new ConfigurationException(e);
        }
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> foundComponents = appComponents.stream()
                .filter(it -> componentClass.isAssignableFrom(it.getClass()))
                .map(it -> (C) it)
                .toList();
        if (foundComponents.size() > 1) {
            throw new ConfigurationException("Found 2 instances matching requested component. Specify name explicitly");
        } else if (foundComponents.size() == 0) {
            throw new ComponentNotRegisteredException("Component " + componentClass.getName() + " not registered");
        }
        return foundComponents.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (component == null) {
            throw new ComponentNotRegisteredException("Component " + componentName + " not registered");
        }
        return component;
    }
}

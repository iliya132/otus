package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ioc {

    private Ioc() {
    }

    @SuppressWarnings({"rawtype", "unchecked"})
    public static <T, TImpl extends T> T wrap(Class<T> clazz, Class<TImpl> implClass) {

        TImpl instance;
        try {
            instance = (TImpl) implClass.getDeclaredConstructors()[0].newInstance();
        } catch (Exception e) {
            System.out.println("Cant find constructor without params");
            return null;
        }
        LoggerInvocationHandler<TImpl> handler = new LoggerInvocationHandler<>(instance, implClass);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{clazz}, handler);
    }

    private record LoggerInvocationHandler<T>(T instance, Class<T> clazz) implements InvocationHandler {
        //static для случаев создания большого количества инстансов одного типа
        private static final Map<Method, Boolean> annotatedWithLog = new HashMap<>();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isAnnotatedCached(method)) {
                System.out.println("executed method: " + method.getName() + ", params: " + argsToString(method, args));
            }
            return method.invoke(instance, args);
        }

        private Boolean isAnnotatedCached(Method method) {
            var isAnnotatedWithLogCache = annotatedWithLog.get(method);
            if (isAnnotatedWithLogCache == null) {
                String methodName = method.getName();
                var params = method.getParameters();
                var implMethod = Arrays.stream(
                                clazz.getMethods())
                        .filter(it -> it.getName().equals(methodName) &&
                                it.getParameters().length == params.length)
                        .findFirst().orElseThrow();
                boolean isAnnotated = implMethod.isAnnotationPresent(Log.class);
                System.out.println("stored cache value for " + method.getName() + " [" + isAnnotated + "]");
                annotatedWithLog.put(method, isAnnotated);
                return isAnnotated;
            } else {
                System.out.println("got cached value for " + method.getName());
                return isAnnotatedWithLogCache;
            }
        }

        private String argsToString(Method method, Object[] args) {
            StringBuilder sb = new StringBuilder();
            var params = method.getParameters();
            for (int i = 0; i < args.length; i++) {
                sb.append(params[i].getName())
                        .append(": ")
                        .append(args[i].toString());
                if (i < args.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }
}

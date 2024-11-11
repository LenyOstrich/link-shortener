package ru.iukr.linkshortener.beanpostprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.iukr.linkshortener.annotation.LogExecutionTime;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "link-shortener.logging", value = "enable-log-execution-time", havingValue = "true")
public class LogExecutionTimeBeanPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(LogExecutionTimeBeanPostProcessor.class);

    private final Map<String, BeanMethodsData> beanMethodsDataByBeanName = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).filter(method -> method.isAnnotationPresent(LogExecutionTime.class)).forEach(method -> {
            beanMethodsDataByBeanName.putIfAbsent(beanName, new BeanMethodsData(clazz, new ArrayList<>()));
            beanMethodsDataByBeanName.get(beanName).annotatedMethods.add(method);
        });
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BeanMethodsData beanMethodsData = beanMethodsDataByBeanName.get(beanName);
        if (beanMethodsData == null) {
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
        Class<?> beanClass = beanMethodsData.clazz;
        List<Method> annotatedMethods = beanMethodsData.annotatedMethods;

        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
            System.out.println("Вызвано из прокси: " + method.getName());
            boolean isAnnotated = annotatedMethods.stream().anyMatch(pojoMethod -> equals(pojoMethod, method));
            if (isAnnotated) {
                long start = System.currentTimeMillis();
                try {
                    return method.invoke(bean, args);
                } catch (Exception e) {
                    throw e.getCause();
                } finally {
                    log.info("Время выполнения метода: {} {}", method.getName(), System.currentTimeMillis() - start);
                }
            }
            try {
                return method.invoke(bean, args);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    private record BeanMethodsData(Class<?> clazz, List<Method> annotatedMethods) {

    }

    private boolean equals(Method method, Method other) {
        if (method.getName().equals(other.getName())) {
            return equalParamTypes(method.getParameterTypes(), other.getParameterTypes());
        }
        return false;
    }

    private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        /* Avoid unnecessary cloning */
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i]) return false;
            }
            return true;
        }
        return false;
    }
}

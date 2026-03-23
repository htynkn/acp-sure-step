package com.huangyunkun.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApplicationAwareUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static final Map<Class<?>, Object> BEAN_REGISTRY = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    /**
     * 注册 Bean 实例（供 LiteFlow 等动态创建的组件使用）
     */
    public static <T> void regBean(T bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Bean cannot be null");
        }
        BEAN_REGISTRY.put(bean.getClass(), bean);
    }

    /**
     * 注册 Bean 实例，使用指定的接口或父类类型
     */
    public static <T> void regBean(T bean, Class<T> beanType) {
        if (bean == null || beanType == null) {
            throw new IllegalArgumentException("Bean and beanType cannot be null");
        }
        BEAN_REGISTRY.put(beanType, bean);
    }

    /**
     * 获取 Bean：优先从 Spring ApplicationContext 获取，回退到本地 registry
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> beanType) {
        if (applicationContext != null) {
            try {
                return applicationContext.getBean(beanType);
            } catch (BeansException ignored) {
            }
        }
        T bean = (T) BEAN_REGISTRY.get(beanType);
        if (bean == null) {
            throw new IllegalArgumentException("Bean not found for type: " + beanType.getName());
        }
        return bean;
    }

    /**
     * 根据类型获取 Bean 实例（泛型推断版本）
     */
    public static <T> T getBean() {
        throw new UnsupportedOperationException("Type inference not available, use getBean(Class<T>) instead");
    }

    /**
     * 检查是否已注册指定类型的 Bean
     */
    public static boolean containsBean(Class<?> beanType) {
        if (applicationContext != null) {
            try {
                applicationContext.getBean(beanType);
                return true;
            } catch (BeansException ignored) {
            }
        }
        return BEAN_REGISTRY.containsKey(beanType);
    }

    /**
     * 移除已注册的 Bean（仅本地 registry）
     */
    public static Object removeBean(Class<?> beanType) {
        return BEAN_REGISTRY.remove(beanType);
    }

    /**
     * 清空本地 registry 和 ApplicationContext 引用
     */
    public static void clear() {
        BEAN_REGISTRY.clear();
        applicationContext = null;
    }
}

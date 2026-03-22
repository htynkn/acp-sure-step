package com.huangyunkun.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationAwareUtil {

    private static final Map<Class<?>, Object> BEAN_REGISTRY = new ConcurrentHashMap<>();

    /**
     * 注册 Bean 实例
     *
     * @param bean 要注册的 Bean 实例
     * @param <T>  Bean 类型
     */
    public static <T> void regBean(T bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Bean cannot be null");
        }
        BEAN_REGISTRY.put(bean.getClass(), bean);
    }

    /**
     * 注册 Bean 实例，使用指定的接口或父类类型
     *
     * @param bean       要注册的 Bean 实例
     * @param beanType   Bean 的类型（接口或父类）
     * @param <T>        Bean 类型
     */
    public static <T> void regBean(T bean, Class<T> beanType) {
        if (bean == null || beanType == null) {
            throw new IllegalArgumentException("Bean and beanType cannot be null");
        }
        BEAN_REGISTRY.put(beanType, bean);
    }

    /**
     * 根据类型获取 Bean 实例
     *
     * @param beanType Bean 类型
     * @param <T>      Bean 类型
     * @return Bean 实例
     * @throws IllegalArgumentException 如果 Bean 未找到
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> beanType) {
        T bean = (T) BEAN_REGISTRY.get(beanType);
        if (bean == null) {
            throw new IllegalArgumentException("Bean not found for type: " + beanType.getName());
        }
        return bean;
    }

    /**
     * 根据类型获取 Bean 实例（泛型推断版本）
     *
     * @param <T> Bean 类型
     * @return Bean 实例
     * @throws IllegalArgumentException 如果 Bean 未找到
     */
    public static <T> T getBean() {
        throw new UnsupportedOperationException("Type inference not available, use getBean(Class<T>) instead");
    }

    /**
     * 检查是否已注册指定类型的 Bean
     *
     * @param beanType Bean 类型
     * @return 如果已注册返回 true，否则返回 false
     */
    public static boolean containsBean(Class<?> beanType) {
        return BEAN_REGISTRY.containsKey(beanType);
    }

    /**
     * 移除已注册的 Bean
     *
     * @param beanType Bean 类型
     * @return 被移除的 Bean 实例，如果不存在则返回 null
     */
    public static Object removeBean(Class<?> beanType) {
        return BEAN_REGISTRY.remove(beanType);
    }

    /**
     * 清空所有已注册的 Bean
     */
    public static void clear() {
        BEAN_REGISTRY.clear();
    }
}

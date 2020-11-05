package com.wtyicy.holder;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wtyicy
 * @version 1.0
 *  2020-11-03
 * @since 1.0
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext appContext = null;

    /**
     * 通过name获取 Bean.
     *
     * @param name 名称
     * @return Object
     */
    public static Object getBean(String name) {
        return appContext.getBean(name);

    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz  42
     * @param <T> 32
     * @return t
     */
    public static <T> T getBean(Class<T> clazz) {
        return appContext.getBean(clazz);
    }

//    /**
//     * 通过name,以及Clazz返回指定的Bean
//     *
//     * @param name 22
//     * @param clazz 22
//     * @return t
//     */
//    public static <T> T getBean(String name, Class<T> clazz) {
//        return appContext.getBean(name, clazz);
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (appContext == null) {
            appContext = applicationContext;
        }
    }
}

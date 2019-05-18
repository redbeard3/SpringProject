package redbeard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;

public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {

    //todo почему-то @Autowired не работает - factory = null!
    //@Autowired
    //private ConfigurableListableBeanFactory factory; // главная фабрика спринга

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        String[] names = applicationContext.getBeanDefinitionNames();
        // обходим factory = null
        ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        for (String name : names) {
            BeanDefinition definition = factory.getBeanDefinition(name);
            String originalClassName = definition.getBeanClassName(); // оригинальное имя класса, прописанное в xml
            try {
                Class<?> originalClass = Class.forName(originalClassName); // оригинальный класс
                Method[] methods = originalClass.getMethods(); // получаем все методы оригинального класса
                for (Method method : methods) {
                    // находим метод, аннотированный PostProxy
                    if (method.isAnnotationPresent(PostProxy.class)) {
                        Object bean = applicationContext.getBean(name); // получаем бин, который прокси
                        /**
                         * получаем метод из бина-прокси с именем метода оригинального бина
                         * с параметрами оригинального бина
                         */
                        Method currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                        currentMethod.invoke(bean);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}

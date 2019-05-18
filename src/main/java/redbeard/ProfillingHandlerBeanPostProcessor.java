package redbeard;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * настройка прокси
 * на этапе postProcessBeforeInitialization запоминаем имя оригинального бина
 * на этапе postProcessAfterInitialization проверяем, есть ли в map имя этого бина
 */
public class ProfillingHandlerBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> map = new HashMap();
    private ProfillingController controller = new ProfillingController();

    // регистрируем бин в MBean
    private ProfillingHandlerBeanPostProcessor() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        platformMBeanServer.registerMBean(controller, new ObjectName("profilling", "name", "controller"));
    }

    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Class<?> beanClass = o.getClass();
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            map.put(s, beanClass);
        }
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class beanClass = map.get(s);
        if (null != beanClass) {
            // если не нуль, то над данным классом стояла аннотация Profiling
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    if (controller.isEnable()) {
                        // проверяем, выставлен ли влаг isEnable
                        System.out.println("Профилирую");
                        Long start = System.nanoTime();
                        Object retVal = method.invoke(o, objects);
                        Long end = System.nanoTime();
                        System.out.println(end - start);
                        System.out.println("Все");
                        return retVal;
                    } else {
                        // делегировать метод на ориганальный объект и сделать ориганальную логику без всяких добавлений
                        return method.invoke(o, objects);
                    }
                }
            }); // Proxy.newProxyInstance - генерит класс налету
        }

        return o;
    }
}

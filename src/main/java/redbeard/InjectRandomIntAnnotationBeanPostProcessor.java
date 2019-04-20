package redbeard;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
           InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
           if (null != annotation) {
              int min = annotation.min();
              int max = annotation.max();
              Random random = new Random();
              int repeat = min + random.nextInt(max - min);
              field.setAccessible(true); // так как поле private
              ReflectionUtils.setField(field, o, repeat);
           }
        }
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return null;
    }
}

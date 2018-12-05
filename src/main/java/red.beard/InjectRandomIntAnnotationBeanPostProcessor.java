package red.beard;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import java.util.Random;
import org.springframework.util.ReflectionUtils;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor{
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName){
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields){
			InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
			int min = annotation.min();
			int max = annotation.max();
			Random random = new Random();
			int i = min + random.nextInt(max - min);
			field.setAccessible(true);
			ReflectionUtils.setField(field, bean, i);
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName){
		return null;
	}

}
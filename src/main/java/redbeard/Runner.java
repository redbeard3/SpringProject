package redbeard;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner{
	
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("common_beans.xml");
		while (true) {
			Thread.sleep(100);
			context.getBean(Quoter.class).sayQuote();
		}
	}
}

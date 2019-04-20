package redbeard;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner{
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("common_beans.xml");
		context.getBean(TerminatorQuoter.class).sayQuote();
	}
}

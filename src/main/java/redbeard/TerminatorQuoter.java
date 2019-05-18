package redbeard;

import javax.annotation.PostConstruct;

@Profiling
public class TerminatorQuoter implements Quoter{

	@InjectRandomInt(min = 2, max = 7)
	private int repeat;

	private String message;

	@PostConstruct
	private void init() {
		System.out.println("Фаза 2");
		System.out.println(repeat);
	}

	private TerminatorQuoter() {
		System.out.println("Фаза 1");
	}

	public void setMessage(String message) {
		this.message = message;
	} 

	public void sayQuote(){
		for (int i = 0; i < repeat; i ++) {
			System.out.println(message);
		}
	}
}
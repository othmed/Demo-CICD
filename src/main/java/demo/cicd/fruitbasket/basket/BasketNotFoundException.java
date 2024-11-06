package demo.cicd.fruitbasket.basket;

public class BasketNotFoundException extends RuntimeException {
		
	public BasketNotFoundException(Long id) {
		super("Could not find basket " + id);
	}
}

package demo.cicd.fruitbasket.basket;

import java.util.List;

public interface BasketService {
	List<BasketDTO> getAll() ; 
	BasketDTO getBasket( Long id); 
	BasketDTO createBasket(BasketDTO newBasketDTO); 
	void deleteBasket( Long id);
}

package demo.cicd.fruitbasket.basket;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BasketController {

	private BasketRepository basketRepository; 
	
	public BasketController(BasketRepository basketRepository) {
		this.basketRepository = basketRepository; 
	}
	
	@GetMapping("/baskets")
	public ResponseEntity<List<BasketDTO>> getAll() {
		List<Basket> basketList = basketRepository.findAll();
		if(CollectionUtils.isNotEmpty(basketList)) {
		return new ResponseEntity<>(	 basketList.stream()
					.map(basket->new BasketDTO(basket.getLabel(), basket.getQuantity()))
					.toList(),  HttpStatus.OK); 
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/basket/{id}")
	public ResponseEntity<BasketDTO> getBasket(@PathVariable Long id) {
	    return basketRepository.findById(id)
	        .map(basket -> ResponseEntity.ok(new BasketDTO(basket.getLabel(), basket.getQuantity())))
	        .orElseThrow(() -> new BasketNotFoundException(id));
	}

	
	@PostMapping("/basket")
	public ResponseEntity<Long> createBasket(@RequestBody @Valid BasketDTO newBasketDTO) {
		var basket = new Basket(); 
		basket.setLabel(newBasketDTO.label()); 
		basket.setQuantity(newBasketDTO.quantity());
		var newBasket = basketRepository.save(basket);
		return ResponseEntity.status(HttpStatus.CREATED).body(newBasket.getId()); 
	}
	
	
	@DeleteMapping("/basket/{id}")
	public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
	    basketRepository.deleteById(id);
	    return ResponseEntity.ok().build();
	}

	

}

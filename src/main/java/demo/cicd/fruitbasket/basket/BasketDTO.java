package demo.cicd.fruitbasket.basket;

import jakarta.validation.constraints.Min;

public record BasketDTO(String label,@Min(value = 0) Integer quantity) {

}

package demo.cicd.fruitbasket.basket;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BasketController.class)
class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BasketRepository basketRepository;

    private Basket basket;

    @BeforeEach
    void setUp() {
        basket = new Basket();
        basket.setId(1L);
        basket.setLabel("Test Label");
        basket.setQuantity(10);
    }

    @Test
    void getAll_ShouldReturnListOfBaskets() throws Exception {
        when(basketRepository.findAll()).thenReturn(Arrays.asList(basket));

        mockMvc.perform(get("/api/baskets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value(basket.getLabel()))
                .andExpect(jsonPath("$[0].quantity").value(basket.getQuantity()));
    }

    @Test
    void getAll_ShouldReturnNoContent_WhenBasketsEmpty() throws Exception {
        when(basketRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/baskets"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBasket_ShouldReturnBasket() throws Exception {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));

        mockMvc.perform(get("/api/basket/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value(basket.getLabel()))
                .andExpect(jsonPath("$.quantity").value(basket.getQuantity()));
    }

    @Test
    void getBasket_ShouldReturnNotFound_WhenBasketDoesNotExist() throws Exception {
        when(basketRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/basket/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBasket_ShouldReturnCreatedBasketId() throws Exception {
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        mockMvc.perform(post("/api/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\": \"Test Label\", \"quantity\": 10}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    void createBasket_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        mockMvc.perform(post("/api/basket")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"label\": \"\", \"quantity\": -1}"))
                .andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.quantity").value("must be greater than or equal to "+"0")); 
    }

    @Test
    void deleteBasket_ShouldReturnOk() throws Exception {
        doNothing().when(basketRepository).deleteById(1L);

        mockMvc.perform(delete("/api/basket/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBasket_ShouldReturnNotFound_WhenBasketDoesNotExist() throws Exception {
        doThrow(new BasketNotFoundException(1L)).when(basketRepository).deleteById(1L);

        mockMvc.perform(delete("/baskets/1"))
                .andExpect(status().isNotFound());
    }
}

package com.codedecode.order.repo;

import com.codedecode.order.dto.FoodItemsDTO;
import com.codedecode.order.dto.Restaurant;
import com.codedecode.order.dto.UserDTO;
import com.codedecode.order.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {
    "spring.mongodb.embedded.version=3.5.5"
})
class OrderRepoTestFixed {

    @Autowired
    private OrderRepo orderRepo;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
        sampleOrder = createSampleOrder();
    }

    @Test
    void save_ShouldPersistOrder_WhenValidOrderProvided() {
        // When
        Order savedOrder = orderRepo.save(sampleOrder);

        // Then
        assertNotNull(savedOrder);
        assertEquals(sampleOrder.getOrderId(), savedOrder.getOrderId());
        assertEquals(sampleOrder.getUserDTO().getUserId(), savedOrder.getUserDTO().getUserId());
    }

    @Test
    void findById_ShouldReturnOrder_WhenOrderExists() {
        // Given
        Order savedOrder = orderRepo.save(sampleOrder);

        // When
        Optional<Order> foundOrder = orderRepo.findById(savedOrder.getOrderId());

        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals(savedOrder.getOrderId(), foundOrder.get().getOrderId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenOrderDoesNotExist() {
        // When
        Optional<Order> foundOrder = orderRepo.findById(999);

        // Then
        assertFalse(foundOrder.isPresent());
    }

    private Order createSampleOrder() {
        Order order = new Order();
        order.setOrderId(1001);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUserName("John Doe");
        userDTO.setUserPassword("password");
        userDTO.setAddress("123 Main St");
        userDTO.setCity("Test City");
        order.setUserDTO(userDTO);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(101);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("Test Address");
        restaurant.setCity("Test City");
        order.setRestaurant(restaurant);

        FoodItemsDTO foodItem1 = new FoodItemsDTO();
        foodItem1.setId(1);
        foodItem1.setItemName("Pizza");
        foodItem1.setItemDescription("Delicious pizza");
        foodItem1.setVeg(true);
        foodItem1.setPrice(299);
        foodItem1.setRestaurantId(101);
        foodItem1.setQuantity(2);

        order.setFoodItemsList(Arrays.asList(foodItem1));
        return order;
    }
}
package com.codedecode.order;

import com.codedecode.order.dto.*;
import com.codedecode.order.entity.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTestNew {

    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Test
    void mapOrderToOrderDTO_ShouldMapAllFields_WhenValidOrderProvided() {
        // Given
        Order order = createSampleOrder();

        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(order);

        // Then
        assertNotNull(orderDTO);
        assertEquals(order.getOrderId(), orderDTO.getOrderId());
        
        // Verify UserDTO mapping
        assertNotNull(orderDTO.getUserDTO());
        assertEquals(order.getUserDTO().getUserId(), orderDTO.getUserDTO().getUserId());
        assertEquals(order.getUserDTO().getUserName(), orderDTO.getUserDTO().getUserName());
        
        // Verify Restaurant mapping
        assertNotNull(orderDTO.getRestaurant());
        assertEquals(order.getRestaurant().getId(), orderDTO.getRestaurant().getId());
        assertEquals(order.getRestaurant().getName(), orderDTO.getRestaurant().getName());
        
        // Verify FoodItemsList mapping
        assertNotNull(orderDTO.getFoodItemsList());
        assertEquals(order.getFoodItemsList().size(), orderDTO.getFoodItemsList().size());
    }

    @Test
    void mapOrderToOrderDTO_ShouldReturnNull_WhenNullOrderProvided() {
        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(null);

        // Then
        assertNull(orderDTO);
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
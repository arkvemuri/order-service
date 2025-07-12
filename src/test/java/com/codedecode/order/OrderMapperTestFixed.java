package com.codedecode.order;

import com.codedecode.order.dto.*;
import com.codedecode.order.entity.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTestFixed {

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
        assertEquals(order.getUserDTO().getUserPassword(), orderDTO.getUserDTO().getUserPassword());
        assertEquals(order.getUserDTO().getAddress(), orderDTO.getUserDTO().getAddress());
        assertEquals(order.getUserDTO().getCity(), orderDTO.getUserDTO().getCity());
        
        // Verify Restaurant mapping
        assertNotNull(orderDTO.getRestaurant());
        assertEquals(order.getRestaurant().getId(), orderDTO.getRestaurant().getId());
        assertEquals(order.getRestaurant().getName(), orderDTO.getRestaurant().getName());
        assertEquals(order.getRestaurant().getAddress(), orderDTO.getRestaurant().getAddress());
        assertEquals(order.getRestaurant().getCity(), orderDTO.getRestaurant().getCity());
        
        // Verify FoodItemsList mapping
        assertNotNull(orderDTO.getFoodItemsList());
        assertEquals(order.getFoodItemsList().size(), orderDTO.getFoodItemsList().size());
        
        for (int i = 0; i < order.getFoodItemsList().size(); i++) {
            FoodItemsDTO originalItem = order.getFoodItemsList().get(i);
            FoodItemsDTO mappedItem = orderDTO.getFoodItemsList().get(i);
            
            assertEquals(originalItem.getId(), mappedItem.getId());
            assertEquals(originalItem.getItemName(), mappedItem.getItemName());
            assertEquals(originalItem.getItemDescription(), mappedItem.getItemDescription());
            assertEquals(originalItem.isVeg(), mappedItem.isVeg());
            assertEquals(originalItem.getPrice(), mappedItem.getPrice());
            assertEquals(originalItem.getRestaurantId(), mappedItem.getRestaurantId());
            assertEquals(originalItem.getQuantity(), mappedItem.getQuantity());
        }
    }

    @Test
    void mapOrderDTOToOrder_ShouldMapAllFields_WhenValidOrderDTOProvided() {
        // Given
        OrderDTO orderDTO = createSampleOrderDTO();

        // When
        Order order = orderMapper.mapOrderDTOToOrder(orderDTO);

        // Then
        assertNotNull(order);
        assertEquals(orderDTO.getOrderId(), order.getOrderId());
        
        // Verify UserDTO mapping
        assertNotNull(order.getUserDTO());
        assertEquals(orderDTO.getUserDTO().getUserId(), order.getUserDTO().getUserId());
        assertEquals(orderDTO.getUserDTO().getUserName(), order.getUserDTO().getUserName());
        assertEquals(orderDTO.getUserDTO().getUserPassword(), order.getUserDTO().getUserPassword());
        assertEquals(orderDTO.getUserDTO().getAddress(), order.getUserDTO().getAddress());
        assertEquals(orderDTO.getUserDTO().getCity(), order.getUserDTO().getCity());
        
        // Verify Restaurant mapping
        assertNotNull(order.getRestaurant());
        assertEquals(orderDTO.getRestaurant().getId(), order.getRestaurant().getId());
        assertEquals(orderDTO.getRestaurant().getName(), order.getRestaurant().getName());
        assertEquals(orderDTO.getRestaurant().getAddress(), order.getRestaurant().getAddress());
        assertEquals(orderDTO.getRestaurant().getCity(), order.getRestaurant().getCity());
        
        // Verify FoodItemsList mapping
        assertNotNull(order.getFoodItemsList());
        assertEquals(orderDTO.getFoodItemsList().size(), order.getFoodItemsList().size());
        
        for (int i = 0; i < orderDTO.getFoodItemsList().size(); i++) {
            FoodItemsDTO originalItem = orderDTO.getFoodItemsList().get(i);
            FoodItemsDTO mappedItem = order.getFoodItemsList().get(i);
            
            assertEquals(originalItem.getId(), mappedItem.getId());
            assertEquals(originalItem.getItemName(), mappedItem.getItemName());
            assertEquals(originalItem.getItemDescription(), mappedItem.getItemDescription());
            assertEquals(originalItem.isVeg(), mappedItem.isVeg());
            assertEquals(originalItem.getPrice(), mappedItem.getPrice());
            assertEquals(originalItem.getRestaurantId(), mappedItem.getRestaurantId());
            assertEquals(originalItem.getQuantity(), mappedItem.getQuantity());
        }
    }

    @Test
    void mapOrderToOrderDTO_ShouldReturnNull_WhenNullOrderProvided() {
        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(null);

        // Then
        assertNull(orderDTO);
    }

    @Test
    void mapOrderDTOToOrder_ShouldReturnNull_WhenNullOrderDTOProvided() {
        // When
        Order order = orderMapper.mapOrderDTOToOrder(null);

        // Then
        assertNull(order);
    }

    @Test
    void mapOrderToOrderDTO_ShouldHandleEmptyFoodItemsList() {
        // Given
        Order order = createSampleOrder();
        order.setFoodItemsList(Arrays.asList());

        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(order);

        // Then
        assertNotNull(orderDTO);
        assertNotNull(orderDTO.getFoodItemsList());
        assertTrue(orderDTO.getFoodItemsList().isEmpty());
    }

    @Test
    void mapOrderToOrderDTO_ShouldHandleNullFoodItemsList() {
        // Given
        Order order = createSampleOrder();
        order.setFoodItemsList(null);

        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(order);

        // Then
        assertNotNull(orderDTO);
        assertNull(orderDTO.getFoodItemsList());
    }

    @Test
    void mapOrderToOrderDTO_ShouldHandleNullUserDTO() {
        // Given
        Order order = createSampleOrder();
        order.setUserDTO(null);

        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(order);

        // Then
        assertNotNull(orderDTO);
        assertNull(orderDTO.getUserDTO());
    }

    @Test
    void mapOrderToOrderDTO_ShouldHandleNullRestaurant() {
        // Given
        Order order = createSampleOrder();
        order.setRestaurant(null);

        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(order);

        // Then
        assertNotNull(orderDTO);
        assertNull(orderDTO.getRestaurant());
    }

    @Test
    void bidirectionalMapping_ShouldMaintainDataIntegrity() {
        // Given
        Order originalOrder = createSampleOrder();

        // When
        OrderDTO orderDTO = orderMapper.mapOrderToOrderDTO(originalOrder);
        Order mappedBackOrder = orderMapper.mapOrderDTOToOrder(orderDTO);

        // Then
        assertEquals(originalOrder.getOrderId(), mappedBackOrder.getOrderId());
        assertEquals(originalOrder.getUserDTO().getUserId(), mappedBackOrder.getUserDTO().getUserId());
        assertEquals(originalOrder.getUserDTO().getUserName(), mappedBackOrder.getUserDTO().getUserName());
        assertEquals(originalOrder.getRestaurant().getId(), mappedBackOrder.getRestaurant().getId());
        assertEquals(originalOrder.getFoodItemsList().size(), mappedBackOrder.getFoodItemsList().size());
        
        // Verify first food item details
        FoodItemsDTO originalFirstItem = originalOrder.getFoodItemsList().get(0);
        FoodItemsDTO mappedFirstItem = mappedBackOrder.getFoodItemsList().get(0);
        assertEquals(originalFirstItem.getId(), mappedFirstItem.getId());
        assertEquals(originalFirstItem.getItemName(), mappedFirstItem.getItemName());
        assertEquals(originalFirstItem.getPrice(), mappedFirstItem.getPrice());
    }

    private Order createSampleOrder() {
        Order order = new Order();
        order.setOrderId(1001);

        // Create UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUserName("John Doe");
        userDTO.setUserPassword("password");
        userDTO.setAddress("123 Main St");
        userDTO.setCity("Test City");
        order.setUserDTO(userDTO);

        // Create Restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setId(101);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("Test Address");
        restaurant.setCity("Test City");
        order.setRestaurant(restaurant);

        // Create FoodItems
        FoodItemsDTO foodItem1 = createFoodItem(1, "Pizza", 299);
        FoodItemsDTO foodItem2 = createFoodItem(2, "Burger", 199);
        order.setFoodItemsList(Arrays.asList(foodItem1, foodItem2));

        return order;
    }

    private OrderDTO createSampleOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1001);

        // Create UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUserName("John Doe");
        userDTO.setUserPassword("password");
        userDTO.setAddress("123 Main St");
        userDTO.setCity("Test City");
        orderDTO.setUserDTO(userDTO);

        // Create Restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setId(101);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("Test Address");
        restaurant.setCity("Test City");
        orderDTO.setRestaurant(restaurant);

        // Create FoodItems
        FoodItemsDTO foodItem1 = createFoodItem(1, "Pizza", 299);
        FoodItemsDTO foodItem2 = createFoodItem(2, "Burger", 199);
        orderDTO.setFoodItemsList(Arrays.asList(foodItem1, foodItem2));

        return orderDTO;
    }

    private FoodItemsDTO createFoodItem(Integer id, String name, Integer price) {
        FoodItemsDTO foodItem = new FoodItemsDTO();
        foodItem.setId(id);
        foodItem.setItemName(name);
        foodItem.setItemDescription("Delicious " + name.toLowerCase());
        foodItem.setVeg(true);
        foodItem.setPrice(price);
        foodItem.setRestaurantId(101);
        foodItem.setQuantity(1);
        return foodItem;
    }
}
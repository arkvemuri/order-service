package com.codedecode.order.service;

/*-
 * #%L
 * Order Service
 * %%
 * Copyright (C) 2024 - 2025 CodeDecode
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.codedecode.order.dto.*;
import com.codedecode.order.entity.Order;
import com.codedecode.order.repo.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTestFixed {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    private OrderDTOFromFE sampleOrderFromFE;
    private UserDTO sampleUserDTO;

    @BeforeEach
    void setUp() {
        sampleOrderFromFE = createSampleOrderFromFE();
        sampleUserDTO = createSampleUserDTO();
    }

    @Test
    void saveOrderInDb_ShouldReturnOrderDTO_WhenValidOrderProvided() {
        // Given
        Integer newOrderId = 1001;
        when(sequenceGenerator.generateNextOrderId()).thenReturn(newOrderId);
        when(restTemplate.getForObject(anyString(), eq(UserDTO.class))).thenReturn(sampleUserDTO);
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderDTO result = orderService.saveOrderInDb(sampleOrderFromFE);

        // Then
        assertNotNull(result);
        assertEquals(newOrderId, result.getOrderId());
        assertEquals(sampleUserDTO.getUserId(), result.getUserDTO().getUserId());

        verify(sequenceGenerator, times(1)).generateNextOrderId();
        verify(restTemplate, times(1)).getForObject("http://USER-SERVICE/user/fetchUserById/1", UserDTO.class);
        verify(orderRepo, times(1)).save(any(Order.class));
    }

    private OrderDTOFromFE createSampleOrderFromFE() {
        OrderDTOFromFE orderFromFE = new OrderDTOFromFE();
        orderFromFE.setUserId(1);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(101);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("Test Address");
        restaurant.setCity("Test City");
        orderFromFE.setRestaurant(restaurant);

        FoodItemsDTO foodItem1 = new FoodItemsDTO();
        foodItem1.setId(1);
        foodItem1.setItemName("Pizza");
        foodItem1.setItemDescription("Delicious pizza");
        foodItem1.setVeg(true);
        foodItem1.setPrice(299);
        foodItem1.setRestaurantId(101);
        foodItem1.setQuantity(2);

        orderFromFE.setFoodItemsList(Arrays.asList(foodItem1));
        return orderFromFE;
    }

    private UserDTO createSampleUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUserName("John Doe");
        userDTO.setUserPassword("password");
        userDTO.setAddress("123 Main St");
        userDTO.setCity("Test City");
        return userDTO;
    }
}

package com.codedecode.order.integration;

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
import com.codedecode.order.repo.OrderRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.data.mongodb.host=localhost",
    "spring.data.mongodb.port=0",
    "spring.mongodb.embedded.version=3.5.5"
})
class OrderIntegrationTestFixed {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepo orderRepo;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
    }

    @Test
    void saveOrder_ShouldCreateOrderSuccessfully_WhenValidDataProvided() throws Exception {
        // Given
        OrderDTOFromFE orderFromFE = createSampleOrderFromFE();
        UserDTO mockUserDTO = createSampleUserDTO();

        when(restTemplate.getForObject(anyString(), eq(UserDTO.class))).thenReturn(mockUserDTO);

        // When & Then
        mockMvc.perform(post("/order/saveOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderFromFE)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.userDTO.userId").value(mockUserDTO.getUserId()));

        // Verify order was saved in database
        assertEquals(1, orderRepo.count());
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

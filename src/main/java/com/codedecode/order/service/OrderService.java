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

import com.codedecode.order.OrderMapper;
import com.codedecode.order.dto.FoodItemsDTO;
import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.dto.UserDTO;
import com.codedecode.order.entity.Order;
import com.codedecode.order.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    SequenceGenerator sequenceGenerator;

    @Autowired
    RestTemplate restTemplate;

    public OrderDTO saveOrderInDb(OrderDTOFromFE orderDetails) {
        Integer newOrderID = sequenceGenerator.generateNextOrderId();
        System.out.println("newOrderID --"+newOrderID);
        System.out.println(orderDetails);
        UserDTO userDTO = fetchUserDetailsFromOrderId(orderDetails.getUserId());
        List<FoodItemsDTO> orderFoodItemsList =orderDetails.getFoodItemsList();
        orderFoodItemsList.stream().forEach(System.out::println);

        Order orderToBeSaved =new Order(newOrderID, orderFoodItemsList,orderDetails.getRestaurant(),userDTO);
        orderRepo.save(orderToBeSaved);
        return OrderMapper.INSTANCE.mapOrderToOrderDTO(orderToBeSaved);
    }

    private UserDTO fetchUserDetailsFromOrderId(Integer userId) {

        return restTemplate.getForObject("http://USER-SERVICE/user/fetchUserById/"+userId,UserDTO.class);
    }


}

package com.codedecode.order.service;

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

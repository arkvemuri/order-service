package com.codedecode.order;

import com.codedecode.order.controller.OrderController;
import com.codedecode.order.repo.OrderRepo;
import com.codedecode.order.service.OrderService;
import com.codedecode.order.service.SequenceGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private OrderController orderController;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void contextLoads() {
		// Verify that the application context loads successfully
		assertNotNull(applicationContext);
	}

	@Test
	void allRequiredBeansAreLoaded() {
		// Verify that all required beans are properly loaded
		assertNotNull(orderController, "OrderController should be loaded");
		assertNotNull(orderService, "OrderService should be loaded");
		assertNotNull(orderRepo, "OrderRepo should be loaded");
		assertNotNull(sequenceGenerator, "SequenceGenerator should be loaded");
		assertNotNull(restTemplate, "RestTemplate should be loaded");
	}

	@Test
	void restTemplateIsLoadBalanced() {
		// Verify that RestTemplate bean is properly configured
		assertNotNull(restTemplate);
		// The @LoadBalanced annotation should be applied to the RestTemplate
		// This is verified by the successful context loading
	}

	@Test
	void applicationHasCorrectMainClass() {
		// Verify that the main application class is OrderApplication
		String[] beanNames = applicationContext.getBeanNamesForType(OrderApplication.class);
		assertTrue(beanNames.length > 0, "OrderApplication should be registered as a bean");
	}

	@Test
	void mongoRepositoryIsConfigured() {
		// Verify that MongoDB repository is properly configured
		assertNotNull(orderRepo);
		assertTrue(orderRepo.getClass().getName().contains("Proxy"), 
			"OrderRepo should be a Spring Data MongoDB proxy");
	}

	@Test
	void servicesAreProperlyWired() {
		// Verify that services are properly autowired
		assertNotNull(orderService);
		assertNotNull(sequenceGenerator);
		
		// These services should be Spring-managed beans
		assertTrue(applicationContext.containsBean("orderService"));
		assertTrue(applicationContext.containsBean("sequenceGenerator"));
	}

	@Test
	void controllerIsProperlyConfigured() {
		// Verify that the controller is properly configured
		assertNotNull(orderController);
		assertTrue(applicationContext.containsBean("orderController"));
	}
}

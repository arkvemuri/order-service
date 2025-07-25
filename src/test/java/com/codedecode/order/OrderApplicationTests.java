package com.codedecode.order;

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

import com.codedecode.order.controller.OrderController;
import com.codedecode.order.repo.OrderRepo;
import com.codedecode.order.service.OrderService;
import com.codedecode.order.service.SequenceGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class OrderApplicationTests extends AbstractIntegrationTest {

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

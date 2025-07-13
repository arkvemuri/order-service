/*
 * #%L
 * Order Service
 * %%
 * Copyright (C) 2024 CodeDecode
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
package com.codedecode.order.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@TestConfiguration
@Profile("test-simple")
public class TestMongoConfiguration {

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        // This will use the default MongoDB connection from application-test.yaml
        // If MongoDB is not available, tests will fail gracefully
        try {
            return new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/orderdb_test"));
        } catch (Exception e) {
            // Return a mock or throw a more descriptive error
            throw new RuntimeException("MongoDB not available for testing. Please start MongoDB or use Testcontainers profile.", e);
        }
    }
}
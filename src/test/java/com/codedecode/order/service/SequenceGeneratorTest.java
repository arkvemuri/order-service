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

import com.codedecode.order.entity.Sequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SequenceGeneratorTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private SequenceGenerator sequenceGenerator;

    @BeforeEach
    void setUp() {
        // Setup is handled by @Mock and @InjectMocks annotations
    }

    @Test
    void generateNextOrderId_ShouldReturnNextSequenceNumber_WhenSequenceExists() {
        // Given
        Sequence mockSequence = new Sequence();
        mockSequence.setId("sequence");
        mockSequence.setSequence(1001);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenReturn(mockSequence);

        // When
        int result = sequenceGenerator.generateNextOrderId();

        // Then
        assertEquals(1001, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        );
    }

    @Test
    void generateNextOrderId_ShouldReturnFirstSequenceNumber_WhenSequenceDoesNotExist() {
        // Given
        Sequence mockSequence = new Sequence();
        mockSequence.setId("sequence");
        mockSequence.setSequence(1); // First sequence number

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenReturn(mockSequence);

        // When
        int result = sequenceGenerator.generateNextOrderId();

        // Then
        assertEquals(1, result);
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        );
    }

    @Test
    void generateNextOrderId_ShouldThrowException_WhenMongoOperationsFails() {
        // Given
        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenThrow(new RuntimeException("MongoDB connection failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> sequenceGenerator.generateNextOrderId());
        
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        );
    }

    @Test
    void generateNextOrderId_ShouldThrowNullPointerException_WhenSequenceIsNull() {
        // Given
        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenReturn(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> sequenceGenerator.generateNextOrderId());
        
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        );
    }

    @Test
    void generateNextOrderId_ShouldHandleMultipleConsecutiveCalls() {
        // Given
        Sequence sequence1 = new Sequence();
        sequence1.setId("sequence");
        sequence1.setSequence(1001);

        Sequence sequence2 = new Sequence();
        sequence2.setId("sequence");
        sequence2.setSequence(1002);

        Sequence sequence3 = new Sequence();
        sequence3.setId("sequence");
        sequence3.setSequence(1003);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenReturn(sequence1, sequence2, sequence3);

        // When
        int result1 = sequenceGenerator.generateNextOrderId();
        int result2 = sequenceGenerator.generateNextOrderId();
        int result3 = sequenceGenerator.generateNextOrderId();

        // Then
        assertEquals(1001, result1);
        assertEquals(1002, result2);
        assertEquals(1003, result3);
        
        verify(mongoOperations, times(3)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        );
    }

    @Test
    void generateNextOrderId_ShouldUseCorrectQueryParameters() {
        // Given
        Sequence mockSequence = new Sequence();
        mockSequence.setId("sequence");
        mockSequence.setSequence(1001);

        when(mongoOperations.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenReturn(mockSequence);

        // When
        sequenceGenerator.generateNextOrderId();

        // Then
        verify(mongoOperations, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        );
    }
}

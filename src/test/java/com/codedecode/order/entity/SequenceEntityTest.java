package com.codedecode.order.entity;

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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SequenceEntityTest {

    @Test
    void sequence_ShouldCreateWithAllArgsConstructor() {
        // Given
        String id = "sequence";
        int sequenceValue = 1001;

        // When
        Sequence sequence = new Sequence(id, sequenceValue);

        // Then
        assertEquals(id, sequence.getId());
        assertEquals(sequenceValue, sequence.getSequence());
    }

    @Test
    void sequence_ShouldCreateWithNoArgsConstructor() {
        // When
        Sequence sequence = new Sequence();

        // Then
        assertNotNull(sequence);
        assertNull(sequence.getId());
        assertEquals(0, sequence.getSequence()); // int default value
    }

    @Test
    void sequence_ShouldSetAndGetId() {
        // Given
        Sequence sequence = new Sequence();
        String id = "test-sequence";

        // When
        sequence.setId(id);

        // Then
        assertEquals(id, sequence.getId());
    }

    @Test
    void sequence_ShouldSetAndGetSequence() {
        // Given
        Sequence sequence = new Sequence();
        int sequenceValue = 5000;

        // When
        sequence.setSequence(sequenceValue);

        // Then
        assertEquals(sequenceValue, sequence.getSequence());
    }

    @Test
    void sequence_ShouldHandleNullId() {
        // Given
        Sequence sequence = new Sequence();

        // When
        sequence.setId(null);

        // Then
        assertNull(sequence.getId());
    }

    @Test
    void sequence_ShouldHandleNegativeSequence() {
        // Given
        Sequence sequence = new Sequence();
        int negativeValue = -100;

        // When
        sequence.setSequence(negativeValue);

        // Then
        assertEquals(negativeValue, sequence.getSequence());
    }

    @Test
    void sequence_ShouldHandleZeroSequence() {
        // Given
        Sequence sequence = new Sequence();
        int zeroValue = 0;

        // When
        sequence.setSequence(zeroValue);

        // Then
        assertEquals(zeroValue, sequence.getSequence());
    }

    @Test
    void sequence_ShouldHandleLargeSequenceValues() {
        // Given
        Sequence sequence = new Sequence();
        int largeValue = Integer.MAX_VALUE;

        // When
        sequence.setSequence(largeValue);

        // Then
        assertEquals(largeValue, sequence.getSequence());
    }

    @Test
    void sequence_ShouldSupportEqualsAndHashCode() {
        // Given
        Sequence sequence1 = new Sequence("sequence", 1001);
        Sequence sequence2 = new Sequence("sequence", 1001);
        Sequence sequence3 = new Sequence("different", 1001);
        Sequence sequence4 = new Sequence("sequence", 2002);

        // Then
        assertEquals(sequence1, sequence2);
        assertEquals(sequence1.hashCode(), sequence2.hashCode());
        
        assertNotEquals(sequence1, sequence3);
        assertNotEquals(sequence1, sequence4);
        assertNotEquals(sequence1.hashCode(), sequence3.hashCode());
        assertNotEquals(sequence1.hashCode(), sequence4.hashCode());
    }

    @Test
    void sequence_ShouldSupportToString() {
        // Given
        Sequence sequence = new Sequence("test-sequence", 1001);

        // When
        String toString = sequence.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Sequence"));
        assertTrue(toString.contains("test-sequence"));
        assertTrue(toString.contains("1001"));
    }

    @Test
    void sequence_ShouldHandleEmptyStringId() {
        // Given
        Sequence sequence = new Sequence();
        String emptyId = "";

        // When
        sequence.setId(emptyId);

        // Then
        assertEquals(emptyId, sequence.getId());
        assertTrue(sequence.getId().isEmpty());
    }

    @Test
    void sequence_ShouldIncrementSequence() {
        // Given
        Sequence sequence = new Sequence("sequence", 1000);

        // When
        sequence.setSequence(sequence.getSequence() + 1);

        // Then
        assertEquals(1001, sequence.getSequence());
    }

    @Test
    void sequence_ShouldHandleSequenceOverflow() {
        // Given
        Sequence sequence = new Sequence("sequence", Integer.MAX_VALUE);

        // When - This would cause overflow in real scenario
        // We're just testing the setter behavior
        sequence.setSequence(Integer.MIN_VALUE);

        // Then
        assertEquals(Integer.MIN_VALUE, sequence.getSequence());
    }
}

package com.hotelmanagement.roomallocation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RoomAllocationServiceTest {

    @InjectMocks
    private RoomAllocationServiceImpl service;

    @Test
    void testScenario1() {
        // Given
        final AllocationRequest request = new AllocationRequest(3, 3);

        // When
        final AllocationResult result = service.allocateRooms(request);

        // Then
        assertEquals(3, result.usedPremiumRooms(), "Premium rooms allocated incorrectly.");
        assertEquals(3, result.usedEconomyRooms(), "Economy rooms allocated incorrectly.");
        assertEquals(new BigDecimal("738"), result.totalPremiumRevenue(), "Total premium revenue calculated incorrectly.");
        assertEquals(new BigDecimal("167.99"), result.totalEconomyRevenue(), "Total economy revenue calculated incorrectly.");
    }

    @Test
    void testScenario2() {
        // Given
        final AllocationRequest request = new AllocationRequest(7, 5);

        // When
        final AllocationResult result = service.allocateRooms(request);

        // Then
        assertEquals(6, result.usedPremiumRooms(), "Premium rooms allocated incorrectly.");
        assertEquals(4, result.usedEconomyRooms(), "Economy rooms allocated incorrectly.");
        assertEquals(new BigDecimal("1054"), result.totalPremiumRevenue(), "Total premium revenue calculated incorrectly.");
        assertEquals(new BigDecimal("189.99"), result.totalEconomyRevenue(), "Total economy revenue calculated incorrectly.");
    }

    @Test
    void testScenario3() {
        // Given
        AllocationRequest request = new AllocationRequest(2, 7);

        // When
        AllocationResult result = service.allocateRooms(request);

        // Then
        assertEquals(2, result.usedPremiumRooms(), "Premium rooms allocated incorrectly.");
        assertEquals(4, result.usedEconomyRooms(), "Economy rooms allocated incorrectly.");
        assertEquals(new BigDecimal("583"), result.totalPremiumRevenue(), "Total premium revenue calculated incorrectly.");
        assertEquals(new BigDecimal("189.99"), result.totalEconomyRevenue(), "Total economy revenue calculated incorrectly.");
    }

    @Test
    void testScenario4() {
        // Given
        final AllocationRequest request = new AllocationRequest(7, 1);

        // When
        final AllocationResult result = service.allocateRooms(request);

        // Then
        assertEquals(7, result.usedPremiumRooms(), "Premium rooms allocated incorrectly.");
        assertEquals(1, result.usedEconomyRooms(), "Economy rooms allocated incorrectly.");
        assertEquals(new BigDecimal("1153"), result.totalPremiumRevenue(), "Total premium revenue calculated incorrectly.");
        assertEquals(new BigDecimal("45.0"), result.totalEconomyRevenue(), "Total economy revenue calculated incorrectly.");
    }
}

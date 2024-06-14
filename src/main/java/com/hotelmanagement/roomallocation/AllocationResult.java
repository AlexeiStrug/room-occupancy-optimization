package com.hotelmanagement.roomallocation;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AllocationResult(int usedPremiumRooms,
                               int usedEconomyRooms,
                               BigDecimal  totalPremiumRevenue,
                               BigDecimal  totalEconomyRevenue) {
}

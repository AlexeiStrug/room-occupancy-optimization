package com.hotelmanagement.roomallocation;

import org.springframework.stereotype.Service;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomAllocationServiceImpl implements RoomAllocationService {

    private static final BigDecimal PREMIUM_PRICE_THRESHOLD = BigDecimal.valueOf(100.0);

    private static final List<BigDecimal> BIDS = List.of(
            new BigDecimal("23.0"),
            new BigDecimal("45.0"),
            new BigDecimal("155.0"),
            new BigDecimal("374.0"),
            new BigDecimal("22.0"),
            new BigDecimal("99.99"),
            new BigDecimal("100.0"),
            new BigDecimal("101.0"),
            new BigDecimal("115.0"),
            new BigDecimal("209.0")
    );

    @Override
    public AllocationResult allocateRooms(AllocationRequest request) {
        final List<BigDecimal> premiumBids = filterAndSortBids(true);
        final List<BigDecimal> economyBids = filterAndSortBids(false);

        int usedPremiumRooms = allocateInitialRooms(premiumBids, request.premiumRooms());
        int usedEconomyRooms = allocateInitialRooms(economyBids, request.economyRooms());

        final Pair<Integer, Integer> result = calculatePotencialUpgrades(request,
                usedPremiumRooms,
                usedEconomyRooms,
                economyBids,
                premiumBids);
        usedPremiumRooms = result.getLeft();
        usedEconomyRooms = result.getRight();

        return buildAllocationResult(usedPremiumRooms,
                usedEconomyRooms,
                premiumBids,
                economyBids);
    }

    private Pair<Integer, Integer> calculatePotencialUpgrades(AllocationRequest request,
                                                              int usedPremiumRooms,
                                                              int usedEconomyRooms,
                                                              List<BigDecimal> economyBids,
                                                              List<BigDecimal> premiumBids) {
        final int additionalPremiumNeeded = request.premiumRooms() - usedPremiumRooms;

        if (additionalPremiumNeeded > 0 && !economyBids.isEmpty()) {
            final List<BigDecimal> potentialUpgrades = upgradeEconomyToPremium(economyBids,
                    additionalPremiumNeeded,
                    request.economyRooms());

            premiumBids.addAll(potentialUpgrades);
            economyBids.removeAll(potentialUpgrades);

            usedPremiumRooms = Math.min(request.premiumRooms(), usedPremiumRooms + potentialUpgrades.size());
            usedEconomyRooms = Math.min(request.economyRooms(), economyBids.size());
        }

        return Pair.of(usedPremiumRooms, usedEconomyRooms);
    }

    private List<BigDecimal> upgradeEconomyToPremium(List<BigDecimal> economyBids, int additionalPremiumNeeded, int requestedEconomyRooms) {
        final int maxUpgrades = Math.max(0, economyBids.size() - requestedEconomyRooms);
        final int upgradesCount = Math.min(additionalPremiumNeeded, maxUpgrades);

        return economyBids.stream()
                .limit(upgradesCount)
                .collect(Collectors.toList());
    }

    private List<BigDecimal> filterAndSortBids(boolean premium) {
        return RoomAllocationServiceImpl.BIDS.stream()
                .filter(bid -> isPremium(premium, bid))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private boolean isPremium(boolean premium, BigDecimal bid) {
        return premium == (bid.compareTo(PREMIUM_PRICE_THRESHOLD) >= 0);
    }

    private int allocateInitialRooms(List<BigDecimal> bids, int roomsAvailable) {
        return Math.min(bids.size(), roomsAvailable);
    }

    private BigDecimal calculateTotalRevenue(List<BigDecimal> bids, int roomsUsed) {
        return bids.stream()
                .limit(roomsUsed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private AllocationResult buildAllocationResult(int usedPremiumRooms,
                                                   int usedEconomyRooms,
                                                   List<BigDecimal> premiumBids,
                                                   List<BigDecimal> economyBids) {
        final BigDecimal totalPremiumRevenue = calculateTotalRevenue(premiumBids, usedPremiumRooms)
                .setScale(0, RoundingMode.DOWN);
        final BigDecimal totalEconomyRevenue = calculateTotalRevenue(economyBids, usedEconomyRooms);

        return AllocationResult.builder()
                .usedPremiumRooms(usedPremiumRooms)
                .usedEconomyRooms(usedEconomyRooms)
                .totalPremiumRevenue(totalPremiumRevenue)
                .totalEconomyRevenue(totalEconomyRevenue)
                .build();
    }
}

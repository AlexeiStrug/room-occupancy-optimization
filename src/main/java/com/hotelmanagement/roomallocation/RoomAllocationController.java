package com.hotelmanagement.roomallocation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RoomAllocationController {

    private final RoomAllocationService roomAllocationService;

    @PostMapping("/allocate-rooms")
    public AllocationResult allocateRooms(@RequestBody AllocationRequest request) {
        return roomAllocationService.allocateRooms(request);
    }
}

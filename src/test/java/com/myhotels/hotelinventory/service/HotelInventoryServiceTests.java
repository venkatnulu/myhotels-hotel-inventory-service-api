package com.myhotels.hotelinventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.myhotels.hotelinventory.dto.HotelInventoryRequest;
import com.myhotels.hotelinventory.dto.RoomRequest;
import com.myhotels.hotelinventory.entity.HotelAddress;
import com.myhotels.hotelinventory.entity.HotelInventory;
import com.myhotels.hotelinventory.entity.Room;
import com.myhotels.hotelinventory.entity.RoomType;
import com.myhotels.hotelinventory.exception.HotelInventoryNotFoundException;
import com.myhotels.hotelinventory.exception.RoomInventoryNotFoundException;
import com.myhotels.hotelinventory.repository.HotelInventoryRepository;
import com.myhotels.hotelinventory.repository.RoomRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({HotelInventoryService.class})
@ExtendWith({SpringExtension.class})
class HotelInventoryServiceTests {

    @Autowired
    private HotelInventoryService inventoryService;

    @MockBean
    private HotelInventoryRepository inventoryRepository;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    void testGetInventory() {
        HotelInventory hotelInventory = buildHotelInventory();

        when(inventoryRepository.findAll()).thenReturn(List.of(hotelInventory));

        List<HotelInventory> inventory = inventoryService.getAllInventories();

        assertNotEquals(Collections.emptyList(), inventory);
    }

    @Test
    void testGetInvenotryById() throws HotelInventoryNotFoundException {
        HotelInventory hotelInventory = buildHotelInventory();
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(hotelInventory));
        HotelInventory inventory = inventoryService.getInventoryById(1234);
        assertNotNull(inventory);
    }

    @Test
    void testGetInvenotryByIdWhenHotelInventoryNotFound() {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () -> inventoryService.getInventoryById(1234));
    }

    @Test
    void testSaveHotelInventory() {
        when(inventoryRepository.save(any())).thenReturn(buildHotelInventory());
        HotelInventoryRequest hotelInventoryRequest = buildHotelInventoryRequest();
        assertNotNull(inventoryService.saveHotelInventory(hotelInventoryRequest));
    }

    @Test
    void testUpdateHotelInventoryMetadata() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        when(inventoryRepository.save(any())).thenReturn(buildHotelInventory());
        HotelInventory hotelInventory = inventoryService.updateHotelInventoryMetadata(1234,
                buildHotelInventoryRequest());
        assertNotNull(hotelInventory);
    }

    @Test
    void testUpdateHotelInventoryMetadataWhenNoHotelInventory() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () -> inventoryService.updateHotelInventoryMetadata(1234,
                buildHotelInventoryRequest()));
    }

    @Test
    void testUpdateRoomInventoryByHotelIdAndRoomID() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        when(inventoryRepository.save(any())).thenReturn(buildHotelInventory());
        HotelInventory hotelInventory = inventoryService.updateRoomInventoryByHotelIdAndRoomID(1234, 3,
                buildRoomInventoryRequest());
        assertNotNull(hotelInventory);
    }

    @Test
    void testUpdateRoomInventoryByHotelIdAndRoomIDWhenNoHotelInventory() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () ->
                inventoryService.updateRoomInventoryByHotelIdAndRoomID(1234, 3,
                        buildRoomInventoryRequest()));
    }

    @Test
    void testAddRoomInventoryByHotelId() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        when(roomRepository.save(any())).thenReturn(Room.builder().build());
        HotelInventory hotelInventory = inventoryService.addRoomInventoryByHotelId(1234, buildRoomInventoryRequest());
        assertNotNull(hotelInventory);
    }

    @Test
    void testAddRoomInventoryByHotelIdWhenNoHotelInventory() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () ->
                inventoryService.addRoomInventoryByHotelId(1234, buildRoomInventoryRequest()));
    }

    @Test
    void testDeleteHotelInventoryByHotelId() throws HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        ResponseEntity<String> entity = inventoryService.deleteHotelInventoryByHotelId(1234);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Hotel inventory with hotel id: 1234 is deleted successfully!", entity.getBody());
    }

    @Test
    void testDeleteHotelInventoryByHotelIdWhenNoHotelInventory() {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () ->
                inventoryService.deleteHotelInventoryByHotelId(1234));
    }

    @Test
    void testDeleteRoomInventoryByHotelIdAndRoomID() throws RoomInventoryNotFoundException,
            HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        ResponseEntity<String> entity = inventoryService.deleteRoomInventoryByHotelIdAndRoomID(1234, 3);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Hotel room inventory with hotel id: 1234 and room number: 3 is deleted successfully!",
                entity.getBody());
    }

    @Test
    void testDeleteRoomInventoryByHotelIdAndRoomIDWhenNoHotelInventory() {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () ->
                inventoryService.deleteRoomInventoryByHotelIdAndRoomID(1234, 3));
    }

    @Test
    void testDeleteRoomInventoryByHotelIdAndRoomIDWhenNoRoomInventory() {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        assertThrows(RoomInventoryNotFoundException.class, () ->
                inventoryService.deleteRoomInventoryByHotelIdAndRoomID(1234, 1));
    }

    @Test
    void testGetRoomInventoryByRoomNumber() throws RoomInventoryNotFoundException, HotelInventoryNotFoundException {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        Room room = inventoryService.getRoomInventoryByRoomNumber(1234, 3);
        assertNotNull(room);
    }

    @Test
    void testGetRoomInventoryByRoomNumberWhenNoHotelInventory() {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.empty());
        assertThrows(HotelInventoryNotFoundException.class, () ->
                inventoryService.getRoomInventoryByRoomNumber(1234, 3));
    }

    @Test
    void testGetRoomInventoryByRoomNumberWhenNoRoomInventory() {
        when(inventoryRepository.findByHotelId(anyInt())).thenReturn(Optional.of(buildHotelInventory()));
        assertThrows(RoomInventoryNotFoundException.class, () ->
                inventoryService.getRoomInventoryByRoomNumber(1234, 1));
    }


    private RoomRequest buildRoomInventoryRequest() {
        return RoomRequest.builder().
                roomType(RoomType.SINGLE_BED.name()).
                bookedFrom(LocalDate.now()).
                bookedUntil(LocalDate.now().plusDays(4)).
                build();
    }

    private HotelInventoryRequest buildHotelInventoryRequest() {
        HotelAddress address = HotelAddress.builder().
                address("Hyderabad").
                contactNumbers(Set.of(9999999999L))
                .build();
        RoomRequest room = buildRoomInventoryRequest();
        HotelInventoryRequest hotelInventory = HotelInventoryRequest.builder().
                name("ABC1").
                description("MY Hotel ABC1").
                address(address).
                rooms(Set.of(room)).
                build();
        return hotelInventory;
    }

    private HotelInventory buildHotelInventory() {
        HotelAddress address = HotelAddress.builder().
                address("Bangalore").
                contactNumbers(Set.of(9999999999L, 8888888888L))
                .build();
        Room room = Room.builder().
                roomNumber(3).
                roomType(RoomType.KING_BED).
                bookedFrom(LocalDate.now().plusDays(3)).
                bookedUntil(LocalDate.now().plusDays(7)).
                build();

        HotelInventory hotelInventory = HotelInventory.builder().
                hotelId(1).
                name("ABC").
                description("MY Hotel ABC").
                address(address).
                rooms(Set.of(room)).
                build();
        return hotelInventory;
    }


}

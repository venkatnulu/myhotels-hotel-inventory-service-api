package com.myhotels.hotelinventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.myhotels.hotelinventory.dto.HotelInventoryRequest;
import com.myhotels.hotelinventory.dto.RoomRequest;
import com.myhotels.hotelinventory.entity.HotelAddress;
import com.myhotels.hotelinventory.entity.HotelInventory;
import com.myhotels.hotelinventory.entity.Room;
import com.myhotels.hotelinventory.entity.RoomType;
import com.myhotels.hotelinventory.repository.TestH2Repository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HotelInventoryServiceApiApplicationTests {

    private static String baseUrl = "";
    @LocalServerPort
    private Integer port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestH2Repository testH2Repository;

    @BeforeEach
    public void init() {
        baseUrl = "http://localhost:".concat(String.valueOf(port)).concat("/api/v1/hotelinventory");
    }

    @Test
    void testAddHotelInventory() {
        HotelInventory hotelInventoryDB = restTemplate.postForObject(baseUrl, buildHotelInventoryRequest(),
                HotelInventory.class);

        assertEquals("My Second Hotel", hotelInventoryDB.getName());
        assertEquals(1, hotelInventoryDB.getRooms().size());
        assertEquals("My Second Hotel Updated", hotelInventoryDB.getDescription());
        assertEquals("Bangalore", hotelInventoryDB.getAddress().getAddress());
    }

    @Test
    void testGetAllHotelInventories() {
        List<HashMap<String, Object>> hotelInventories = restTemplate.getForObject(baseUrl, List.class);
        assertTrue(hotelInventories.stream().anyMatch(inventory -> inventory.containsValue("MyHotel101")));
        assertTrue(hotelInventories.stream().anyMatch(inventory -> inventory.containsValue("MyHotel102")));
        assertTrue(hotelInventories.stream().anyMatch(inventory -> inventory.containsValue("MyHotel102")));
    }

    @Test
    void testGetHotelInventory() {
        HotelInventory inventory = restTemplate.getForObject(baseUrl + "/hotel/{hotel_id}", HotelInventory.class, 100);
        assertEquals(100, inventory.getHotelId());
        assertEquals("MyHotel101", inventory.getName());
        assertEquals("My First Hotel", inventory.getDescription());
    }

    @Test
    void testUpdateHotelInventoryMetadata() {
        restTemplate.put(baseUrl + "/hotel/{hotel_id}", buildHotelInventoryRequest(), 200);
        HotelInventory inventory = testH2Repository.findByHotelId(200);
        assertEquals("My Second Hotel Updated", inventory.getDescription());
    }

    @Test
    void testUpdateHotelRoomInventory() {
        RoomRequest room = RoomRequest.builder().
                roomType(RoomType.DOUBLE_BED.name()).
                bookedFrom(LocalDate.now()).
                bookedUntil(LocalDate.now().plusDays(1)).build();
        restTemplate.put(baseUrl + "/hotel/{hotel_id}/room/{room_number}", room, 100, 100102);
        Room roomDB = testH2Repository.getRoomByRoomNumber(100102);
        assertEquals(RoomType.DOUBLE_BED.name(), roomDB.getRoomType().name());
        assertEquals(1, roomDB.getBookedFrom().until(roomDB.getBookedUntil()).getDays());
    }

    @Test
    void testAddRoomByHotelId() {
        RoomRequest room = RoomRequest.builder().
                roomType(RoomType.DOUBLE_BED.name()).
                bookedFrom(LocalDate.now()).
                bookedUntil(LocalDate.now().plusDays(1)).build();
        restTemplate.postForObject(baseUrl + "/hotel/{hotel_id}/room", room, Room.class, 100);
        HotelInventory inventory = testH2Repository.findByHotelId(100);
        assertEquals(6, inventory.getRooms().size());
    }

    @Test
    void testDeleteHotelInventoryByHotelId() {
        restTemplate.delete(baseUrl + "/hotel/{hotel_id}", 300);
        HotelInventory inventory = testH2Repository.findByHotelId(300);
        assertNull(inventory);
    }

    @Test
    void testGetRoomDetailsByRoomNumber() {
        Room room = restTemplate.getForObject(baseUrl + "/hotel/{hotel_id}/room/{room_number}", Room.class, 100,
                100101);
        assertEquals(100101, room.getRoomNumber());
        assertEquals(RoomType.SINGLE_BED.name(), room.getRoomType().name());
    }

    @Test
    void testDeleteHotelRoomInventoryByRoomNumber()  {
        restTemplate.delete(baseUrl + "/hotel/{hotel_id}/room/{room_number}", 200, 200105);
        assertNull(testH2Repository.getRoomByRoomNumber(200105));
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
                address("Bangalore").
                contactNumbers(Set.of(9999999999L, 8888888888L))
                .build();
        RoomRequest room = buildRoomInventoryRequest();
        return HotelInventoryRequest.builder().
                name("My Second Hotel").
                description("My Second Hotel Updated").
                address(address).
                rooms(Set.of(room)).
                build();
    }

}

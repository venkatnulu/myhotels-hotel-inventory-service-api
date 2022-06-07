package com.myhotels.hotelinventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myhotels.hotelinventory.dto.HotelInventoryRequest;
import com.myhotels.hotelinventory.dto.RoomRequest;
import com.myhotels.hotelinventory.entity.HotelAddress;
import com.myhotels.hotelinventory.entity.HotelInventory;
import com.myhotels.hotelinventory.entity.Room;
import com.myhotels.hotelinventory.entity.RoomType;
import com.myhotels.hotelinventory.service.HotelInventoryService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(HotelInventoryController.class)
@ExtendWith({SpringExtension.class})
@Import(HotelInventoryService.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class HotelInventoryControllerTests {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HotelInventoryService inventoryService;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testGetAllHotelInventories() throws Exception {
        when(inventoryService.getAllInventories()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotelinventory")).andExpect(status().isOk());
    }

    @Test
    void testGetHotelInventory() throws Exception {
        when(inventoryService.getInventoryById(1234)).thenReturn(HotelInventory.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotelinventory/hotel/1234")).
                andExpect(status().isOk());
    }

    @Test
    void testSaveHotelInventory() throws Exception {
        when(inventoryService.saveHotelInventory(any())).thenReturn(HotelInventory.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/hotelinventory").contentType(APPLICATION_JSON).
                content(mapper.writeValueAsString(buildHotelInventoryRequest()))).andExpect(status().isCreated());
    }

    @Test
    void testUpdateHotelInventoryMetadata() throws Exception {
        when(inventoryService.updateHotelInventoryMetadata(anyInt(), any())).thenReturn(buildHotelInventory());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/hotelinventory/hotel/1234").contentType(APPLICATION_JSON).
                content(mapper.writeValueAsString(buildHotelInventoryRequest()))).andExpect(status().isOk());
    }

    @Test
    void testUpdateHotelRoomInventory() throws Exception {
        when(inventoryService.updateRoomInventoryByHotelIdAndRoomID(anyInt(), anyInt(), any())).
                thenReturn(HotelInventory.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/hotelinventory/hotel/1234/room/1").
                contentType(APPLICATION_JSON).content(mapper.writeValueAsString(buildRoomInventoryRequest()))).
                andExpect(status().isOk());
    }

    @Test
    void testAddRoomByHotelId() throws Exception {
        when(inventoryService.addRoomInventoryByHotelId(anyInt(), any())).thenReturn(HotelInventory.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/hotelinventory/hotel/1234/room").
                        contentType(APPLICATION_JSON).content(mapper.writeValueAsString(buildRoomInventoryRequest()))).
                andExpect(status().isOk());
    }

    @Test
    void testDeleteHotelInventoryByHotelId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/hotelinventory/hotel/1234")).
                andExpect(status().isOk());
    }

    @Test
    void testGetRoomDetailsByRoomNumber() throws Exception {
        when(inventoryService.getRoomInventoryByRoomNumber(anyInt(), anyInt())).thenReturn(Room.builder().build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/hotelinventory/hotel/1234/room/1")).
                andExpect(status().isOk());
    }

    @Test

    void testDeleteHotelRoomInventoryByRoomNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/hotelinventory/hotel/1234/room/1")).
                andExpect(status().isOk());
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
        return HotelInventoryRequest.builder().
                name("ABC1").
                description("MY Hotel ABC1").
                address(address).
                rooms(Set.of(room)).
                build();
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

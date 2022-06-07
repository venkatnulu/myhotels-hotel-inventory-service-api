package com.myhotels.hotelinventory.controller;

import com.myhotels.hotelinventory.dto.HotelInventoryRequest;
import com.myhotels.hotelinventory.dto.RoomRequest;
import com.myhotels.hotelinventory.entity.HotelInventory;
import com.myhotels.hotelinventory.entity.Room;
import com.myhotels.hotelinventory.exception.HotelInventoryNotFoundException;
import com.myhotels.hotelinventory.exception.RoomInventoryNotFoundException;
import com.myhotels.hotelinventory.service.HotelInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.base-uri}")
@Slf4j
@RefreshScope
@Tag(name = "Hotel Inventory API", description = "Provides API to manage MyHotels inventory like adding rooms, " +
        "deleting rooms, getting inventories, updating the hotel and room inventories, etc..")
public class HotelInventoryController {

    @Autowired
    private HotelInventoryService inventoryService;

    @Operation(summary = "Get All Hotels Inventories", description = "Retrieves all hotel details with all room " +
            "details in each hotel", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(description = "Get Hotel Inventory Success", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "HotelInventory", implementation =
                    HotelInventory.class)))
    })
    @GetMapping
    public ResponseEntity<List<HotelInventory>> getHotelInventories() {
        log.info("Inside get all hotel inventories controller");
        List<HotelInventory> inventory = inventoryService.getAllInventories();

        return ResponseEntity.ok(inventory);
    }

    @Operation(summary = "Get Hotel Inventory", description = "Get hotel inventory of the given hotel id", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(description = "Get Hotel Inventory Success", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "HotelInventory", implementation =
                    HotelInventory.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.get-hotel-inventory}")
    public ResponseEntity<HotelInventory> getHotelInventory(@PathVariable("hotel_id") Integer hotelId) throws
            HotelInventoryNotFoundException {
        log.info("Inside get hotel inventory controller with hotel id: " + hotelId);
        return ResponseEntity.ok(inventoryService.getInventoryById(hotelId));
    }

    @Operation(summary = "Add Hotel Inventory", description = "Add hotel inventory to the MyHotels inventories", tags =
            "Post")
    @ApiResponses(value = {
            @ApiResponse(description = "Add Hotel Inventory Success", responseCode = "201", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "HotelInventory", implementation =
                    HotelInventory.class)))
    })
    @PostMapping
    public ResponseEntity<HotelInventory> saveHotelInventory(@Valid @RequestBody HotelInventoryRequest hotelInventoryRequest) {
        log.info("Inside save hotel inventory controller with hotel inventory: " + hotelInventoryRequest);
        HotelInventory hotelInventoryDB = inventoryService.saveHotelInventory(hotelInventoryRequest);
        return new ResponseEntity<>(hotelInventoryDB, HttpStatus.CREATED);
    }

    @Operation(summary = "Update Hotel Inventory Metadata", description = "Update hotel inventory meta data of the " +
            "given hotel id", tags = "Put")
    @ApiResponses(value = {
            @ApiResponse(description = "Update Hotel Inventory Success", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "HotelInventory", implementation =
                    HotelInventory.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @PutMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.put-hotel-inventory-metadata}")
    public ResponseEntity<HotelInventory> updateHotelInventoryMetadata(@PathVariable("hotel_id") Integer hotelId,
                                                                       @Valid @RequestBody HotelInventoryRequest hotelInventoryRequest)
            throws HotelInventoryNotFoundException {
        log.info(String.format("Inside update hotel inventory metadata controller with hotel id: %s, inventory: %s",
                hotelId,
                hotelInventoryRequest));
        HotelInventory hotelInventoryDB = inventoryService.updateHotelInventoryMetadata(hotelId, hotelInventoryRequest);
        return ResponseEntity.ok(hotelInventoryDB);
    }

    @Operation(summary = "Update Hotel Room Inventory", description = "Update hotel room inventory with " +
            "given hotel id and room number", tags = "Put")
    @ApiResponses(value = {
            @ApiResponse(description = "Update Hotel Room Inventory Success", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "HotelInventory", implementation =
                    HotelInventory.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json")),
            @ApiResponse(description = "Hotel Room Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @PutMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.put-hotel-room-inventory}")
    public ResponseEntity<HotelInventory> updateHotelRoomInventory(@PathVariable("hotel_id") Integer hotelId,
                                                                   @PathVariable("room_number") Integer roomNumber
            , @Valid @RequestBody RoomRequest roomRequest) throws HotelInventoryNotFoundException {
        log.info(String.format("Inside update hotel room inventory controller with hotel id: %s, room number: %s, " +
                        "room info: %s",
                hotelId, roomNumber, roomRequest));
        HotelInventory updatedHotelInventory = inventoryService.updateRoomInventoryByHotelIdAndRoomID(hotelId,
                roomNumber,
                roomRequest);
        return ResponseEntity.ok(updatedHotelInventory);
    }

    @Operation(summary = "Add Hotel Room Inventory", description = "Add hotel room inventory for given hotel id",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(description = "Add Hotel Room Inventory Success", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "HotelInventory", implementation =
                    HotelInventory.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.post-hotel-room-inventory}")
    public ResponseEntity<HotelInventory> addRoomByHotelId(@PathVariable("hotel_id") Integer hotelId,
                                                           @Valid @RequestBody RoomRequest roomRequest)
            throws HotelInventoryNotFoundException {
        log.info(String.format("Inside add hotel room inventory by hotel id controller with hotel id: %s, room " +
                        "room info: %s",
                hotelId, roomRequest));
        HotelInventory updatedHotelInventory = inventoryService.addRoomInventoryByHotelId(hotelId, roomRequest);
        return ResponseEntity.ok(updatedHotelInventory);
    }

    @Operation(summary = "Delete Hotel Inventory", description = "Delete hotel inventory for given hotel id",
            tags = "Delete")
    @ApiResponses(value = {
            @ApiResponse(description = "Add Hotel Room Inventory Success", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "SuccessMessage", implementation =
                    String.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.delete-hotel-inventory}")
    public ResponseEntity<String> deleteHotelInventoryByHotelId(@PathVariable("hotel_id") Integer hotelId)
            throws HotelInventoryNotFoundException {
        log.info(String.format("Inside delete hotel inventory by hotel id controller with hotel id: %s",
                hotelId));
        return inventoryService.deleteHotelInventoryByHotelId(hotelId);
    }

    @Operation(summary = "Get Hotel Room Inventory", description = "Get hotel room inventory for given hotel id and " +
            "room number", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(description = "Hotel Room Inventory Found", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "RoomInventory", implementation =
                    Room.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json")),
            @ApiResponse(description = "Hotel Room Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.get-hotel-room-inventory}")
    public ResponseEntity<Room> getRoomDetailsByRoomNumber(@PathVariable("hotel_id") Integer hotelId,
                                                           @PathVariable("room_number") Integer roomNumber)
            throws HotelInventoryNotFoundException, RoomInventoryNotFoundException {
        log.info(String.format("Inside get hotel room inventory by room number controller with hotel id: %s, room " +
                "number: %s, ", hotelId, roomNumber));

        return ResponseEntity.ok(inventoryService.getRoomInventoryByRoomNumber(hotelId, roomNumber));
    }

    @Operation(summary = "Delete Hotel Room Inventory", description = "Delete hotel room inventory for given hotel id" +
            "and room number", tags = "Delete")
    @ApiResponses(value = {
            @ApiResponse(description = "Hotel Room Inventory Deleted successfully", responseCode = "200", content =
            @Content(mediaType = "application/json", schema = @Schema(name = "SuccessMessage", implementation =
                    String.class))),
            @ApiResponse(description = "Hotel Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json")),
            @ApiResponse(description = "Hotel Room Inventory Not Found", responseCode = "404", content =
            @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = "${myhotel-microservices.hotel-inventory-service.endpoints.delete-hotel-room-inventory}")
    public ResponseEntity<String> deleteHotelRoomInventoryByRoomNumber(@PathVariable("hotel_id") Integer hotelId,
                                                                       @PathVariable("room_number") Integer roomNumber)
            throws HotelInventoryNotFoundException, RoomInventoryNotFoundException {
        log.info(String.format("Inside delete hotel room inventory by room number controller with hotel id: %s, room " +
                "number: %s, ", hotelId, roomNumber));
        return inventoryService.deleteRoomInventoryByHotelIdAndRoomID(hotelId, roomNumber);
    }
}

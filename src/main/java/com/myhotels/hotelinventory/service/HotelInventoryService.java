package com.myhotels.hotelinventory.service;

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
import java.lang.Integer;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HotelInventoryService {

    @Autowired
    private HotelInventoryRepository hotelInventoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    public List<HotelInventory> getAllInventories() {
        log.info("Inside get inventory service");
        return hotelInventoryRepository.findAll();
    }

    public HotelInventory getInventoryById(Integer hotelId) throws HotelInventoryNotFoundException {
        log.info("Inside get hotel inventory service with hotel id: " + hotelId);
        Optional<HotelInventory> hotelInventory = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventory.isPresent()) {
            return hotelInventory.get();
        }
        else {
            throw new HotelInventoryNotFoundException(hotelId);
        }
    }

    public HotelInventory saveHotelInventory(HotelInventoryRequest hotelInventoryRequest) {
        log.info("Inside save hotel inventory service with hotel inventory: " + hotelInventoryRequest);
        Set<Room> rooms = new HashSet<>();
        hotelInventoryRequest.getRooms().forEach(roomRequest -> {
            Room room = Room.builder().
                    roomType(RoomType.valueOf(roomRequest.getRoomType())).
                    bookedFrom(roomRequest.getBookedFrom()).
                    bookedUntil(roomRequest.getBookedUntil()).
                    build();
            rooms.add(room);
        });
        HotelInventory hotelInventory = HotelInventory.builder()
                .name(hotelInventoryRequest.getName())
                .description(hotelInventoryRequest.getDescription())
                .address(hotelInventoryRequest.getAddress())
                .rooms(rooms)
                .build();
        return hotelInventoryRepository.save(hotelInventory);
    }

    @Transactional
    public HotelInventory updateHotelInventoryMetadata(Integer hotelId, HotelInventoryRequest hotelInventoryRequest)
            throws HotelInventoryNotFoundException {
        log.info(String.format("Inside update hotel inventory metadata service with hotel id: %s, inventory: %s",
                hotelId, hotelInventoryRequest));

        Optional<HotelInventory> hotelInventoryDB = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventoryDB.isEmpty()) {
            throw new HotelInventoryNotFoundException(hotelId);
        }
        HotelInventory inventoryDB = hotelInventoryDB.get();

        if(null != hotelInventoryRequest.getName() && !hotelInventoryRequest.getName().isBlank()
                && !hotelInventoryRequest.getName().equals(inventoryDB.getName())) {
            inventoryDB.setName(hotelInventoryRequest.getName());
        }
        if(null != hotelInventoryRequest.getDescription() && !hotelInventoryRequest.getDescription().isBlank()
                && !hotelInventoryRequest.getDescription().equals(inventoryDB.getDescription())) {
            inventoryDB.setDescription(hotelInventoryRequest.getDescription());
        }
        updateAddress(hotelInventoryRequest.getAddress(), inventoryDB.getAddress());

        return hotelInventoryRepository.save(inventoryDB);

    }

    private void updateAddress(HotelAddress hotelAddress, HotelAddress hotelAddressDB) {
        if(null != hotelAddress.getAddress() && !hotelAddressDB.getAddress().isBlank() &&
                !hotelAddress.getAddress().equals(hotelAddressDB.getAddress())) {
            hotelAddressDB.setAddress(hotelAddress.getAddress());
        }
        if(null != hotelAddress.getContactNumbers() && !hotelAddress.getContactNumbers().isEmpty() &&
                !hotelAddress.getContactNumbers().equals(hotelAddressDB.getContactNumbers())) {
            hotelAddressDB.setContactNumbers(hotelAddress.getContactNumbers());
        }
    }

    @Transactional
    public HotelInventory updateRoomInventoryByHotelIdAndRoomID(Integer hotelId, Integer roomNumber,
                                                                RoomRequest roomRequest)
            throws HotelInventoryNotFoundException {
        log.info(String.format("Inside update hotel room inventory service with hotel id: %s, room number: %s, " +
                        "room info: %s",
                hotelId, roomNumber, roomRequest));
        Optional<HotelInventory> hotelInventoryDB = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventoryDB.isEmpty()) {
            throw new HotelInventoryNotFoundException(hotelId);
        }
        HotelInventory inventoryDB = hotelInventoryDB.get();
        inventoryDB.getRooms().forEach( roomDB -> {
            if(Objects.equals(roomNumber, roomDB.getRoomNumber())) {
                roomDB.setRoomType(RoomType.valueOf(roomRequest.getRoomType()));
                roomDB.setBookedFrom(roomRequest.getBookedFrom());
                roomDB.setBookedUntil(roomRequest.getBookedUntil());
            }
        });
        return hotelInventoryRepository.save(inventoryDB);
    }

    @Transactional
    public HotelInventory addRoomInventoryByHotelId(Integer hotelId, RoomRequest roomRequest) throws HotelInventoryNotFoundException {
        log.info(String.format("Inside add hotel room inventory by hotel id service with hotel id: %s, room " +
                        "room info: %s",
                hotelId, roomRequest));
        Optional<HotelInventory> hotelInventoryDB = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventoryDB.isEmpty()) {
            throw new HotelInventoryNotFoundException(hotelId);
        }
        Room room = Room.builder().
                roomType(RoomType.valueOf(roomRequest.getRoomType())).
                bookedFrom(roomRequest.getBookedFrom()).
                bookedUntil(roomRequest.getBookedUntil()).
                build();
        Room roomDB = roomRepository.save(room);
        roomRepository.updateRoomWithHotelId(roomDB.getRoomNumber(), hotelId);
        return hotelInventoryRepository.findByHotelId(hotelId).get();
    }

    @Transactional
    public ResponseEntity<String> deleteHotelInventoryByHotelId(Integer hotelId) throws HotelInventoryNotFoundException {
        log.info(String.format("Inside delete hotel inventory by hotel id service with hotel id: %s", hotelId));
        Optional<HotelInventory> hotelInventoryDB = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventoryDB.isEmpty()) {
            throw new HotelInventoryNotFoundException(hotelId);
        }
        hotelInventoryRepository.deleteByHotelId(hotelId);
        return new ResponseEntity<>(String.format("Hotel inventory with hotel id: %s is deleted " +
                    "successfully!", hotelId), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteRoomInventoryByHotelIdAndRoomID(Integer hotelId, Integer roomNumber)
            throws HotelInventoryNotFoundException, RoomInventoryNotFoundException {
        log.info(String.format("Inside delete hotel room inventory by room number service with hotel id: %s, room " +
                "number: %s, ", hotelId, roomNumber));
        Optional<HotelInventory> hotelInventoryDB = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventoryDB.isEmpty()) {
            throw new HotelInventoryNotFoundException(hotelId);
        }
        Optional<Room> room =
                hotelInventoryDB.get().getRooms().stream().filter(room1 -> Objects.equals(room1.getRoomNumber(),
                roomNumber)).findFirst();
        if(room.isEmpty()) {
            throw new RoomInventoryNotFoundException(hotelId, roomNumber);
        }
        roomRepository.deleteByRoomNumber(roomNumber);
        return new ResponseEntity<>(String.format("Hotel room inventory with hotel id: %s and room number: %s is " +
                "deleted successfully!", hotelId, roomNumber), HttpStatus.OK);
    }

    public Room getRoomInventoryByRoomNumber(Integer hotelId, Integer roomNumber)
            throws HotelInventoryNotFoundException, RoomInventoryNotFoundException {
        log.info(String.format("Inside get hotel room inventory by room number service with hotel id: %s, room " +
                "number: %s, ", hotelId, roomNumber));
        Optional<HotelInventory> hotelInventoryDB = hotelInventoryRepository.findByHotelId(hotelId);
        if(hotelInventoryDB.isEmpty()) {
            throw new HotelInventoryNotFoundException(hotelId);
        }
        HotelInventory inventoryDB = hotelInventoryDB.get();
        Optional<Room> roomInventory = inventoryDB.getRooms().stream().filter(room -> Objects.equals(room.getRoomNumber(),
                roomNumber)).findFirst();
        if(roomInventory.isEmpty()) {
            throw new RoomInventoryNotFoundException(hotelId, roomNumber);
        }
        return roomInventory.get();
    }
}

package com.myhotels.hotelinventory.exception;

public class RoomInventoryNotFoundException extends Exception {

    public RoomInventoryNotFoundException(Integer hotelId, Integer roomNumber) {
        super(String.format("Room with room number: %s is not found in hotel with id: %s", roomNumber, hotelId));
    }
}

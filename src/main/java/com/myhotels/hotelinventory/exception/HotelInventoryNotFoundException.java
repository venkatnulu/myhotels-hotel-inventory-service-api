package com.myhotels.hotelinventory.exception;

import java.math.BigInteger;

public class HotelInventoryNotFoundException extends Exception {
    public HotelInventoryNotFoundException(Integer hotelId) {
        super(String.format("Hotel not found with hotel id: %s", hotelId));
    }
}

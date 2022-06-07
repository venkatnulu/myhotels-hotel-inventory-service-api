package com.myhotels.hotelinventory.repository;

import com.myhotels.hotelinventory.entity.HotelInventory;
import com.myhotels.hotelinventory.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestH2Repository extends JpaRepository<HotelInventory, Integer> {
    HotelInventory findByHotelId(Integer id);

    @Query("FROM Room r where r.roomNumber=:roomNumber")
    Room getRoomByRoomNumber(@Param("roomNumber") Integer roomNumber);
}

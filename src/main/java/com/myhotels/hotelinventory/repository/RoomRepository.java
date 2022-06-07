package com.myhotels.hotelinventory.repository;

import com.myhotels.hotelinventory.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query(value = "UPDATE rooms_tbl SET hotel_id=:hotelId WHERE room_number=:roomNumber", nativeQuery = true)
    @Modifying
    void updateRoomWithHotelId(@Param("roomNumber") Integer roomNumber, @Param("hotelId") Integer hotelId);

    @Query(value = "DELETE FROM rooms_tbl r WHERE r.room_number=:roomNumber", nativeQuery = true)
    @Modifying
    void deleteByRoomNumber(@Param("roomNumber") Integer roomNumber);
}

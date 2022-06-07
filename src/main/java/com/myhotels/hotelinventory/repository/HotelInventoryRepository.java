package com.myhotels.hotelinventory.repository;

import com.myhotels.hotelinventory.entity.HotelInventory;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelInventoryRepository extends JpaRepository<HotelInventory, BigInteger> {
    Optional<HotelInventory> findByHotelId(Integer hotelId);

    void deleteByHotelId(Integer hotelId);

}

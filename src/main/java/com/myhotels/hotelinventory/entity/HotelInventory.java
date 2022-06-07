package com.myhotels.hotelinventory.entity;

import java.math.BigInteger;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "hotel_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class HotelInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer hotelId;
    private String name;
    private String description;
    @Embedded
    private HotelAddress address;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(
            name = "hotel_id",
            referencedColumnName = "hotelId"
    )
    private Set<Room> rooms;


}

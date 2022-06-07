package com.myhotels.hotelinventory.dto;

import com.myhotels.hotelinventory.entity.HotelAddress;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelInventoryRequest {
    @NotBlank(message = "Hotel name should not be blank")
    private String name;
    private String description;
    @Valid
    private HotelAddress address;
    @Valid
    private Set<RoomRequest> rooms;
}

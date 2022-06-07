package com.myhotels.hotelinventory.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.myhotels.hotelinventory.entity.RoomType;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    @NotBlank(message = "Room type should not be empty")
    private String roomType;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate bookedFrom;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    @Future
    private LocalDate bookedUntil;
}

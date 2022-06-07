package com.myhotels.hotelinventory.entity;

import java.util.Objects;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelAddress {
    @NotBlank(message = "Hotel address should not be empty")
    private String address;
    @ElementCollection
    private Set<Long> contactNumbers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelAddress that = (HotelAddress) o;
        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}

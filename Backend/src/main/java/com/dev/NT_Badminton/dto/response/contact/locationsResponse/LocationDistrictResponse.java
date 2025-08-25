package com.dev.NT_Badminton.dto.response.contact.locationsResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDistrictResponse {

    Integer id;

    String name;

     List<LocationWardResponse> wards;

    LocationDistrictResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}

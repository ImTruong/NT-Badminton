package com.dev.NT_Badminton.repositories.contact;

import com.dev.NT_Badminton.dto.response.contact.locationsResponse.LocationCityResponse;
import com.dev.NT_Badminton.dto.response.contact.locationsResponse.LocationDistrictResponse;
import com.dev.NT_Badminton.dto.response.contact.locationsResponse.LocationWardResponse;
import com.dev.NT_Badminton.dto.response.contact.locationsResponse.LocationsResponse;
import com.dev.NT_Badminton.entities.contacts.*;
import com.dev.NT_Badminton.repositories.BaseRepository;
import com.querydsl.core.types.Projections;

import java.util.List;

public class ContactRepositoryImpl extends BaseRepository implements ContactRepositoryCustom {

    @Override
    public LocationsResponse getAllLocations() {
        QCity city = QCity.city;
        QDistrict district = QDistrict.district;
        QWard ward = QWard.ward;
        List<LocationCityResponse> cities = query()
                .select(Projections.bean(LocationCityResponse.class,
                        city.id,
                        city.name))
                .from(city)
                .fetch();

        for (LocationCityResponse c : cities) {
            List<LocationDistrictResponse> districts = query()
                    .select(Projections.bean(LocationDistrictResponse.class,
                            district.id,
                            district.name))
                    .from(district)
                    .where(district.cityId.eq(c.getId()))
                    .fetch();

            for (LocationDistrictResponse d : districts) {
                List<LocationWardResponse> wards = query()
                        .select(Projections.bean(LocationWardResponse.class,
                                ward.id,
                                ward.name))
                        .from(ward)
                        .where(ward.districtId.eq(d.getId()))
                        .fetch();

                d.setWards(wards);
            }

            c.setDistricts(districts);
        }
        return new LocationsResponse(cities);
    }


}

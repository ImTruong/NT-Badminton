package com.dev.NT_Badminton.entities.contacts;

import com.dev.NT_Badminton.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum CityDistrictPair implements BaseEnum<Integer> {
    HANOI(1, Map.of(
            1, "Ba Đình",
            2, "Hoàn Kiếm",
            3, "Tây Hồ",
            4, "Hai Bà Trưng"
    )),
    HO_CHI_MINH(2, Map.of(
            1, "Quận 1",
            2, "Quận 3",
            3, "Quận 5",
            4, "Quận 7"
    )),
    DA_NANG(3, Map.of(
            1, "Hải Châu",
            2, "Thanh Khê",
            3, "Sơn Trà",
            4, "Ngũ Hành Sơn"
    ));

    private final int id;
    private final Map<Integer, String> districts;

    CityDistrictPair(int id, Map<Integer, String> districts) {
        this.id = id;
        this.districts = new HashMap<>(districts);
    }

    public Map<Integer, String> getDistricts() {
        return Collections.unmodifiableMap(districts);
    }

    @JsonCreator
    public static CityDistrictPair fromValue(int id) {
        for (CityDistrictPair city : values()) {
            if (city.getId() == id) {
                return city;
            }
        }
        throw new IllegalArgumentException("City ID không hợp lệ: " + id);
    }

    public static String getDistrictName(int cityId, int districtId) {
        CityDistrictPair city = fromValue(cityId);
        return city.getDistricts().getOrDefault(districtId, "Quận/huyện không hợp lệ");
    }

    @JsonCreator
    @Override
    public Integer toValue() {
        return id;
    }
}

package com.matjipdaehak.fo.place.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Place {

    private int placeId;
    private String kakaoPlaceId;
    private String name;

    /** 도로명 주소*/
    private String address;
    private double latitude;
    private double longitude;
    private String phone;
    private String category;

    public Place(
            int placeId,
            String kakaoPlaceId,
            String name,
            String address,
            double latitude,
            double longitude,
            String phone,
            String category
    ){
        this.placeId = placeId;
        this.kakaoPlaceId = kakaoPlaceId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.category = category;
    }
}

package com.matjipdaehak.fo.place.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Place {

    @JsonProperty("place_id")
    private int placeId;

    @JsonProperty("kakao_place_id")
    private String kakaoPlaceId;
    private String name;

    /** 도로명 주소*/
    private String address;

    private double latitude;
    private double longitude;

    private String phone;
    private String category;
    @JsonProperty("image_url")
    private String imageUrl;
    /**
     * 기존에 DB에 존재하는 Place를 매핑시킬때 사용할 생성자.
     * @param placeId
     * @param kakaoPlaceId
     * @param name
     * @param address
     * @param latitude
     * @param longitude
     * @param phone
     * @param category
     */
    public Place(
            int placeId,
            String kakaoPlaceId,
            String name,
            String address,
            double latitude,
            double longitude,
            String phone,
            String category,
            String imageUrl
    ){
        this(
                kakaoPlaceId,
                name,
                address,
                latitude,
                longitude,
                phone,
                category,
                imageUrl
        );
        this.placeId = placeId;
    }

    /**
     * Place를 DB에 새로 등록할 때 필요한 생성자.
     * Place id는 DB에서 auto increment로 생성된다.
     * @param kakaoPlaceId
     * @param name
     * @param address
     * @param latitude
     * @param longitude
     * @param phone
     * @param category
     */
    public Place(
            String kakaoPlaceId,
            String name,
            String address,
            double latitude,
            double longitude,
            String phone,
            String category,
            String imageUrl
    ){
        this.kakaoPlaceId = kakaoPlaceId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.category = category;
    }
}

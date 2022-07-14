package com.matjipdaehak.fo.place.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * keyword 검색등에서 보여줄 가게의 추가 정보가 필요하다.
 * 추가로 필요한 정보: rating, review_count, like_count
 * 계산된 정보가 이용되므로 select 목적으로 사용하는것을 권장.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedPlace{

    @JsonProperty("place_id")
    private int placeId;

    @JsonProperty("kakao_place_id")
    private String kakaoPlaceId;

    /** 가게 이름 */
    private String name;

    /** 도로명 주소*/
    private String address;

    /** 위도 */
    private double latitude;

    /** 경도 */
    private double longitude;

    /** 대표 전화번호 */
    private String phone;

    /**
     * 카테고리. kakao api의 카테고리 정보를 이용
     */
    private String category;

    @JsonProperty("image_url")
    private String imageUrl;

    /**
     * 리뷰의 평점 평균
     */
    private double rating;

    /**
     * 리뷰 개수
     */
    @JsonProperty("review_count")
    private int reviewCount;

    /**
     * 가게에 대한 좋아요 개수
     */
    @JsonProperty("like_count")
    private int likeCount;

    /**
     * Place객체의 내용을 ExtendedPlace객체로 setting
     * @param place - PLACE id까지 있는 Place 객체
     */
    public void setByPlace(Place place){
        this.setPlaceId(place.getPlaceId());
        this.setKakaoPlaceId(place.getKakaoPlaceId());
        this.setName(place.getName());
        this.setAddress(place.getAddress());
        this.setCategory(place.getCategory());
        this.setLatitude(place.getLatitude());
        this.setLongitude(place.getLongitude());
        this.setImageUrl(place.getImageUrl());
        this.setPhone(place.getPhone());
    }
}

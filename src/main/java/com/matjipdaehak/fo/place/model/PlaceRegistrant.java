package com.matjipdaehak.fo.place.model;
import lombok.*;

/**
 * 특정 place를 등록한 유저의 아이디를 저장하는 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRegistrant {
    private int placeId;
    private String userId;
}

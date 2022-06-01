package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;

public interface PlaceRepository {

    /**
     * Place 객체를 DB에 저장
     * @param place
     */
    void insertPlace(Place place);

    /**
     * place_id로 DB에서 Place조회
     * @param placeId
     * @return Place
     */
    Place selectPlace(int placeId);
}

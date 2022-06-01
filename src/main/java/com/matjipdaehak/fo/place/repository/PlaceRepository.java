package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;

public interface PlaceRepository {

    /**
     * Place 객체를 DB에 저장
     * 이때 place id는 auto increament로 생성되므로 필요없다.
     * @param place - place id만 없는 place객체
     */
    void insertPlace(Place place);

    /**
     * place_id로 DB에서 Place조회
     * @param placeId
     * @return Place
     */
    Place selectPlace(int placeId);
}

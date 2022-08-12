package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.PlaceRegistrant;

import java.util.List;

/**
 * PLACE_REGISTRANT 테이블에 대한 crud
 */
public interface PlaceRegistrantRepository {

    void insertPlaceRegistrant(PlaceRegistrant placeRegistrant);

    List<Integer>  selectPlaceIdByRegistrantId(String userId, int scopeStart, int scopeEnd);
}

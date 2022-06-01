package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.exception.DataAlreadyExistException;
import com.matjipdaehak.fo.place.model.Place;

public interface PlaceService {

    /**
     * 새로운 장소를 등록한다.
     * 이미 같은 장소가 존재할 경우 예외발생
     * @param place
     */
    void createNewPlace(Place place) throws DataAlreadyExistException;
}

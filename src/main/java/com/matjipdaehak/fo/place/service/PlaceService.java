package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.exception.DataAlreadyExistException;
import com.matjipdaehak.fo.place.model.Place;
import java.util.*;

public interface PlaceService {

    /**
     * 새로운 장소를 등록한다.
     * 이미 같은 장소가 존재할 경우 예외발생
     * @param place
     * @return place_id - 생성된 place는 DB에서 id를 받는다. 이를 반환해준다.
     */
    int createNewPlace(Place place) throws DataAlreadyExistException;

    List<Place> searchPlaceByKeyword(String keyword, int collegeId);

    Place getPlaceByPlaceId(int placeId);
}

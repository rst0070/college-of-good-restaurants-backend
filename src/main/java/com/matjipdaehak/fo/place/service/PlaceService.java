package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.exception.DataAlreadyExistException;
import com.matjipdaehak.fo.place.model.Place;
import java.util.*;

public interface PlaceService {

    /**
     * 새로운 장소를 등록한다.
     * 이미 같은 장소가 존재할 경우 예외발생
     * @param place - place의 정보
     * @param userId - place를 등록한 사용자의 아이디
     * @return place_id - 생성된 place는 DB에서 id를 받는다. 이를 반환해준다.
     */
    int createNewPlace(Place place, String userId) throws DataAlreadyExistException;

    List<Place> searchPlaceByKeyword(String keyword, int collegeId);

    Place getPlaceByPlaceId(int placeId);

    /**
     * 등록자의 아이디를 이용해 등록자가 등록한 가게의 아이디를 리스트로 반환
     * @param userId - 등록자 id
     * @return place_id의 리스트
     */
    List<Integer> getPlaceIdByRegistrantId(String userId, int scopeStart, int scopeEnd);
}

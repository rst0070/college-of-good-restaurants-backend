package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;
import java.util.*;

public interface PlaceRepository {

    /**
     * Place 객체를 DB에 저장
     * 이때 place id는 auto increament로 생성되므로 필요없다.
     * @param place - place id만 없는 place객체
     * @return place_id - 생성된 place의 id (place는 DB에서 autoincrement로 id를 받는다.)
     */
    int insertPlace(Place place);

    /**
     * place_id로 DB에서 Place조회
     * @param placeId
     * @return Place
     */
    Place selectPlace(int placeId);

    List<Place> keywordSearchPlace(String keyword, int collegeId);
}

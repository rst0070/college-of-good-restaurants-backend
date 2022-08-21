package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;
import java.util.*;

public interface PlaceRepository {

    /**
     * Place는 가게의 이름과 주소의 조합으로 유일하게 구분된다.
     * 즉 가게의 주소와 이름의 조합이 유일해야하며 이것을 통해 가게가 이미 DB상에 존재하는지 확인할 수 있다.
     * @param address - 가게 주소
     * @param name    - 가게 이름
     * @return true - 존재함. false - 없음(등록가능)
     */
    boolean isPlaceExists(String address, String name);

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

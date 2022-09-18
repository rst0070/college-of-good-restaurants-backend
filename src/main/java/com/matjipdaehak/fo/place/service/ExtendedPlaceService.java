package com.matjipdaehak.fo.place.service;
import com.matjipdaehak.fo.place.model.ExtendedPlace;

import java.util.*;

public interface ExtendedPlaceService {

    /**
     * place id로 ExtendedPlace 반환
     * @param placeId
     * @return ExtendedPlace 반환
     */
    ExtendedPlace getExtendedPlaceByPlaceId(int placeId);

    List<ExtendedPlace> getExtendedPlaceByRegistrantId(String userId, int scopeStart, int scopeEnd);

    /**
     * keyword에 매칭되는 장소를 검색한다.
     * 이때 장소의 이름, 주소, 카테고리 등을 검색하고
     * 리뷰의 내용또한 검색한다.
     * @param collegeId
     * @param keyword
     * @param scopeStart
     * @param scopeEnd
     * @return ExtendedPlace List 반환
     */
    List<ExtendedPlace> keywordSearchExtendedPlace(int collegeId, String keyword, int scopeStart, int scopeEnd);

    /**
     * 특정 college에서 리뷰순으로 상위 10개를 반환한다.
     * @param collegeId
     * @return
     */
    List<ExtendedPlace> getTop10PlaceInCollegeByReviewCount(int collegeId);

}

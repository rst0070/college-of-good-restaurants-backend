package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.exception.UnprocessableEntityException;
import com.matjipdaehak.fo.place.model.ExtendedPlace;
import java.util.*;

/**
 * ExtendedPlace는 PLACE데이터와 REVIEW, PLACE_LIKE데이터를 조합하여 생성한 객체이다.<br/>
 * 따라서 조회하는것이 주 목적. 그중에서도 키워드 검색이 중요하다.
 */
public interface ExtendedPlaceRepository {

    /**
     * Place id로 ExtendedPlace객체 생성
     * @param placeId
     * @return ExtendedPlace
     */
    ExtendedPlace selectExtendedPlace(int placeId);

    /**
     * 가게 이름, 주소, 리뷰 내용의 문자열과 keyword를 비교하여 유사한 가게 리스트를 가져온다.
     * @param collegeId - 찾으려는 가게가 등록된 학교
     * @param keyword - 특정 문자열
     * @param scopeStart - 검색 범위의 시작점: 검색결과는 scopeStart를 포함한다.
     * @param scopeEnd - 검색 범위의 마지막: 검색결과는 scopeEnd를 포함한다.
     * @return List of ExtendedPlace
     */
    List<ExtendedPlace> keywordSearchExtendedPlace(int collegeId, String keyword, int scopeStart, int scopeEnd) throws UnprocessableEntityException;

    List<ExtendedPlace> getTop10PlaceInCollegeByReviewCount(int collegeId);
    //List<ExtendedPlace> keywordSearchExtendedPlaceOrderbyReview

}

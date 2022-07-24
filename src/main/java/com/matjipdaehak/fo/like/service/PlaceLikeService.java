package com.matjipdaehak.fo.like.service;

import com.matjipdaehak.fo.like.model.ExtendedPlaceLike;
import com.matjipdaehak.fo.like.model.PlaceLike;

import java.util.*;

public interface PlaceLikeService {

    /**
     * 해당하는 PLACE_LIKE row가 존재하는지 확인
     * @param placeId
     * @param userId
     * @return true - 존재, false - 없음
     */
    boolean checkLikeExist(int placeId, String userId);

    /**
     * place like를 DB에 등록한다.
     * @param placeId
     * @param userId
     */
    void addLike(int placeId, String userId, Date likeDate);

    /**
     * DB에서 place like를 지운다.
     * @param placeId
     * @param userId
     */
    void removeLike(int placeId, String userId);

    /**
     * 특정 유저의 좋아요 목록을 가져온다.
     * 이때 좋아요 날짜가 내림차순으로 된다.(현재와 가까운 날짜부터)<br/>
     * @param userId - 사용자 id
     * @param scopeStart - 범위 시작점
     * @param scopeEnd - 범위 끝나는 점
     * @return List<PlaceLike>
     */
    List<ExtendedPlaceLike> getLikeListByUserId(String userId, int scopeStart, int scopeEnd);

    /**
     * 특정 PLACE의 좋아요 개수를 세서 리턴
     * @param placeID
     * @return
     */
    int countLikeByPlaceId(int placeID);
}

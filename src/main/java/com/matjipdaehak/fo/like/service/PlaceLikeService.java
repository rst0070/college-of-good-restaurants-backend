package com.matjipdaehak.fo.like.service;

import com.matjipdaehak.fo.like.model.PlaceLike;

import java.util.*;

public interface PlaceLikeService {

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
     * @param userId - 사용자 id
     * @return List<PlaceLike>
     */
    List<PlaceLike> getLikeListByUserId(String userId);

    /**
     * 특정 PLACE의 좋아요 개수를 세서 리턴
     * @param placeID
     * @return
     */
    int countLikeByPlaceId(int placeID);
}

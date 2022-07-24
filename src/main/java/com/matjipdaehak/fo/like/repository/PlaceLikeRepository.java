package com.matjipdaehak.fo.like.repository;

import com.matjipdaehak.fo.like.model.PlaceLike;
import java.util.*;

public interface PlaceLikeRepository {

    /**
     * PLACE_id 와 USER_id를 확인하고 PLACE_LIKE가 존재하는지 확인한다.
     * @param placeId - PLACE_id
     * @param userId - USER_id
     * @return true - 존재함. false - 없음
     */
    boolean isExist(int placeId, String userId);

    void insertPlaceLike(PlaceLike placeLike);

    void deletePlaceLike(int placeId, String userId);

    PlaceLike selectPlaceLike(int placeId, String userId);

    List<PlaceLike> selectPlaceLikeByUserId(String userId, int scopeStart, int scopeEnd);

    int countPlaceLikeByPlaceId(int placeId);
}

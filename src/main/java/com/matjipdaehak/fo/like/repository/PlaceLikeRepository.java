package com.matjipdaehak.fo.like.repository;

import com.matjipdaehak.fo.like.model.PlaceLike;
import java.util.*;

public interface PlaceLikeRepository {

    void insertPlaceLike(PlaceLike placeLike);

    void deletePlaceLike(int placeId, String userId);

    PlaceLike selectPlaceLike(int placeId, String userId);

    List<PlaceLike> selectPlaceLikeByUserId(String userId);

    int countPlaceLikeByPlaceId(int placeId);
}

package com.matjipdaehak.fo.like.service;

import com.matjipdaehak.fo.like.model.PlaceLike;
import com.matjipdaehak.fo.like.repository.PlaceLikeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlaceLikeServiceImpl implements PlaceLikeService{

    private final PlaceLikeRepository placeLikeRepository;

    public PlaceLikeServiceImpl(PlaceLikeRepository placeLikeRepository){
        this.placeLikeRepository = placeLikeRepository;
    }

    @Override
    public void addLike(int placeId, String userId, Date likeDate) {
        placeLikeRepository.insertPlaceLike(
                new PlaceLike(
                        placeId,
                        userId,
                        likeDate
                )
        );
    }

    @Override
    public void removeLike(int placeId, String userId) {
        placeLikeRepository.deletePlaceLike(placeId, userId);
    }

    @Override
    public List<PlaceLike> getLikeListByUserId(String userId) {
        return placeLikeRepository.selectPlaceLikeByUserId(userId);
    }

    @Override
    public int countLikeByPlaceId(int placeId) {
        return placeLikeRepository.countPlaceLikeByPlaceId(placeId);
    }
}

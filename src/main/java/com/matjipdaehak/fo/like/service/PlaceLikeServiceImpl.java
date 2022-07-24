package com.matjipdaehak.fo.like.service;

import com.matjipdaehak.fo.like.model.ExtendedPlaceLike;
import com.matjipdaehak.fo.like.model.PlaceLike;
import com.matjipdaehak.fo.like.repository.PlaceLikeRepository;
import com.matjipdaehak.fo.place.repository.PlaceRepository;
import com.matjipdaehak.fo.place.service.ExtendedPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class PlaceLikeServiceImpl implements PlaceLikeService{

    private final PlaceLikeRepository placeLikeRepository;
    private final ExtendedPlaceService extendedPlaceService;

    @Autowired
    public PlaceLikeServiceImpl(
            PlaceLikeRepository placeLikeRepository,
            ExtendedPlaceService extendedPlaceService
            ){
        this.placeLikeRepository = placeLikeRepository;
        this.extendedPlaceService = extendedPlaceService;
    }

    @Override
    public boolean checkLikeExist(int placeId, String userId) {
        return placeLikeRepository.isExist(placeId, userId);
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
    public List<ExtendedPlaceLike> getLikeListByUserId(String userId, int scopeStart, int scopeEnd) {
        Iterator<PlaceLike> likes = placeLikeRepository.selectPlaceLikeByUserId(userId, scopeStart, scopeEnd).iterator();
        LinkedList<ExtendedPlaceLike> result = new LinkedList<ExtendedPlaceLike>();
        while(likes.hasNext()){
            PlaceLike like = likes.next();
            result.add(
                    new ExtendedPlaceLike(
                            like,
                            extendedPlaceService.getExtendedPlaceByPlaceId(like.getPlaceId())
                    )
            );
        }
        return result;
    }

    @Override
    public int countLikeByPlaceId(int placeId) {
        return placeLikeRepository.countPlaceLikeByPlaceId(placeId);
    }
}

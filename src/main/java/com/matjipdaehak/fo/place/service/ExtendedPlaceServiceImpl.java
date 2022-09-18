package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.place.model.ExtendedPlace;
import com.matjipdaehak.fo.place.repository.ExtendedPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExtendedPlaceServiceImpl implements ExtendedPlaceService{

    private final ExtendedPlaceRepository extendedPlaceRepository;
    private final PlaceService placeService;

    @Autowired
    public ExtendedPlaceServiceImpl(ExtendedPlaceRepository extendedPlaceRepository,
                                    PlaceService placeService){
        this.extendedPlaceRepository = extendedPlaceRepository;
        this.placeService = placeService;
    }

    @Override
    public ExtendedPlace getExtendedPlaceByPlaceId(int placeId) {
        return extendedPlaceRepository.selectExtendedPlace(placeId);
    }

    /**
     * PlaceService에서 registrant id를 이용해 해당 place id들을 가져온다.
     * place id를 이용해 ExtendedPlace를 구성한다.
     * @param userId
     * @param scopeStart
     * @param scopeEnd
     * @return
     */
    @Override
    public List<ExtendedPlace> getExtendedPlaceByRegistrantId(String userId, int scopeStart, int scopeEnd) {

        Iterator<Integer> placeIds = placeService.getPlaceIdByRegistrantId(userId, scopeStart, scopeEnd).iterator();
        LinkedList<ExtendedPlace> extendedPlaces = new LinkedList<ExtendedPlace>();

        while(placeIds.hasNext()){
            extendedPlaces.add(
                    this.getExtendedPlaceByPlaceId(placeIds.next())
            );
        }

        return extendedPlaces;
    }

    @Override
    public List<ExtendedPlace> keywordSearchExtendedPlace(int collegeId, String keyword, int scopeStart, int scopeEnd) {
        return extendedPlaceRepository.keywordSearchExtendedPlace(collegeId, keyword, scopeStart, scopeEnd);
    }

    @Override
    public List<ExtendedPlace> getTop10PlaceInCollegeByReviewCount(int collegeId) {
        return this.extendedPlaceRepository.getTop10PlaceInCollegeByReviewCount(collegeId);
    }
}

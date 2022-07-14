package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.place.model.ExtendedPlace;
import com.matjipdaehak.fo.place.repository.ExtendedPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtendedPlaceServiceImpl implements ExtendedPlaceService{

    private final ExtendedPlaceRepository extendedPlaceRepository;

    @Autowired
    public ExtendedPlaceServiceImpl(ExtendedPlaceRepository extendedPlaceRepository){
        this.extendedPlaceRepository = extendedPlaceRepository;
    }

    @Override
    public ExtendedPlace getExtendedPlaceByPlaceId(int placeId) {
        return extendedPlaceRepository.selectExtendedPlace(placeId);
    }

    @Override
    public List<ExtendedPlace> keywordSearchExtendedPlace(int collegeId, String keyword, int scopeStart, int scopeEnd) {
        return extendedPlaceRepository.keywordSearchExtendedPlace(collegeId, keyword, scopeStart, scopeEnd);
    }
}

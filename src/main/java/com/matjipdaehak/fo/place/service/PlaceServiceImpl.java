package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.exception.DataAlreadyExistException;
import com.matjipdaehak.fo.place.model.Place;
import com.matjipdaehak.fo.place.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService{

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;
    }

    /**
     * 이미 DB에 있는 Place 데이터와 중복되는지 확인후 생성해야함.
     * 방법: 주소 + 이름이 유일해야한다. -> unique key로 지정했으므로 sql exception으로 확인가능하다.
     * @param place
     * @throws DataAlreadyExistException
     */
    @Override
    public void createNewPlace(Place place) throws DataAlreadyExistException {
        try{
            placeRepository.insertPlace(place);
        }catch(DataAccessException ex){
            throw new DataAlreadyExistException(ex.getMessage());
        }
    }

    @Override
    public List<Place> searchPlaceByKeyword(String keyword, int collegeId) {
        return placeRepository.keywordSearchPlace(keyword, collegeId);
    }
}

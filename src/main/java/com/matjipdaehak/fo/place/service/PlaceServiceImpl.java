package com.matjipdaehak.fo.place.service;

import com.matjipdaehak.fo.exception.DataAlreadyExistException;
import com.matjipdaehak.fo.place.model.Place;
import com.matjipdaehak.fo.place.model.PlaceRegistrant;
import com.matjipdaehak.fo.place.repository.PlaceRegistrantRepository;
import com.matjipdaehak.fo.place.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService{

    private final PlaceRepository placeRepository;
    private final PlaceRegistrantRepository placeRegistrantRepository;


    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository,
                            PlaceRegistrantRepository placeRegistrantRepository){
        this.placeRepository = placeRepository;
        this.placeRegistrantRepository = placeRegistrantRepository;
    }

    /**
     * Place는 가게의 이름과 주소의 조합으로 유일하게 구분된다.
     * 즉 가게의 주소와 이름의 조합이 유일해야하며 이것을 통해 가게가 이미 DB상에 존재하는지 확인할 수 있다.
     *
     * @param address - 가게 주소
     * @param name    - 가게 이름
     * @return true - 존재함. false - 없음(등록가능)
     */
    @Override
    public boolean isPlaceExists(String address, String name) {
        return this.placeRepository.isPlaceExists(address, name);
    }

    /**
     * 이미 DB에 있는 Place 데이터와 중복되는지 확인후 생성해야함.
     * 방법: 주소 + 이름이 유일해야한다. -> unique key로 지정했으므로 sql exception으로 확인가능하다.
     * @param place
     * @throws DataAlreadyExistException
     */
    @Override
    @Transactional
    public int createNewPlace(Place place, String userId) throws DataAlreadyExistException {
        try{
            int placeId = placeRepository.insertPlace(place);
            PlaceRegistrant registrant = new PlaceRegistrant(placeId, userId);
            placeRegistrantRepository.insertPlaceRegistrant(registrant);

            return placeId;
        }catch(DataAccessException ex){
            throw new DataAlreadyExistException(ex.getMessage());
        }
    }

    @Override
    public List<Place> searchPlaceByKeyword(String keyword, int collegeId) {
        return placeRepository.keywordSearchPlace(keyword, collegeId);
    }

    @Override
    public Place getPlaceByPlaceId(int placeId) {
        return this.placeRepository.selectPlace(placeId);
    }

    @Override
    public List<Integer> getPlaceIdByRegistrantId(String userId, int scopeStart, int scopeEnd) {
        return placeRegistrantRepository.selectPlaceIdByRegistrantId(userId, scopeStart, scopeEnd);
    }


}

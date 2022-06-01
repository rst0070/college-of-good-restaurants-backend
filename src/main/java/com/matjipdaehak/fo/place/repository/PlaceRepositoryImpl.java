package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlaceRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertPlace(Place place) throws DataAccessException {
        String insertToPlace = "" +
                "insert into PLACE(place_id, place_name, place_address, latitude, longitude, phone) " +
                "values(?, ?, ?, ?, ?, ?) ";

        String insertToKakaoPlace = "" +
                "insert into KAKAO_PLACE(PLACE_id, kakao_place_id, category) " +
                "values(?, ?, ?) ";

        jdbcTemplate.update(insertToPlace,
                place.getPlaceId(),
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getPhone());

        jdbcTemplate.update(insertToKakaoPlace,
                place.getPlaceId(),
                place.getKakaoPlaceId(),
                place.getCategory());
    }

    @Override
    public Place selectPlace(int placeId) {
        String selectPlace = "" +
                "SELECT " +
                "   place_name, place_address, latitude, longitude, phone, kakao_place_id, category " +
                "FROM " +
                "   PLACE inner join KAKAO_PLACE " +
                "   ON PLACE.place_id = KAKAO_PLACE.PLACE_id " +
                "WHERE place_id = ? ";


        return jdbcTemplate.queryForObject(selectPlace,
                (rs, rn) -> new Place(
                            placeId,
                            rs.getString("kakao_place_id"),
                            rs.getString("place_name"),
                            rs.getString("place_address"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getString("phone"),
                            rs.getString("category")
                ),
                placeId);
    }
}

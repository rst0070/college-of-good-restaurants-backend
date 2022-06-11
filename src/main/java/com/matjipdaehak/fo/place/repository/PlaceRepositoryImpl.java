package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlaceRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * place id는 auto increament로 생성된다. 따라서 다음방법으로 db에 저장해야한다.
     *
     * 1. place를 insert한다.
     * 2. 생성한 place의 id를 name과 address로 가져온다.
     * 3. place id를 이용해 kakao place에 insert한다.
     *
     * 2번의 기능을 따로 메서드로 만들어야하는지 고민해보자.
     *
     * @param place - place id만 없는 place객체
     * @throws DataAccessException
     */
    @Override
    public void insertPlace(Place place) throws DataAccessException {
        String insertToPlace = "" +
                "insert into PLACE(place_name, place_address, latitude, longitude, phone) " +
                "values(?, ?, ?, ?, ?) ";

        String getPlaceId = "" +
                "SELECT place_id " +
                "FROM PLACE " +
                "WHERE place_name = ? and place_address = ? ";

        String insertToKakaoPlace = "" +
                "insert into KAKAO_PLACE(PLACE_id, kakao_place_id, category) " +
                "values(?, ?, ?) ";

        jdbcTemplate.update(insertToPlace,
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getPhone());

        int placeId = jdbcTemplate.queryForObject(
                getPlaceId,
                (rs, rn) -> rs.getInt("place_id"),
                place.getName(),
                place.getAddress()
        );

        jdbcTemplate.update(insertToKakaoPlace,
                placeId,
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


    @Override
    public List<Place> keywordSearchPlace(String keyword, int collegeId) {
        String sql = "SELECT * " +
                "FROM PLACE, PLACE_LIST_AT_COLLEGE, KAKAO_PLACE " +
                "WHERE " +
                "place_list_at_college.COLLEGE_id = ? " +
                "   and place_list_at_college.PLACE_id = place.place_id " +
                "   and kakao_place.PLACE_id = place.place_id " +
                "   and ( kakao_place.category like ? or place.place_name like ? ) ";

        keyword = '%' + keyword + '%';
        return jdbcTemplate.query(sql,
                (rs, rn) ->{
                    return new Place(
                            rs.getInt("place_id"),
                            rs.getString("kakao_place_id"),
                            rs.getString("place_name"),
                            rs.getString("place_address"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getString("phone"),
                            rs.getString("category")
                    );
                },
                collegeId,
                keyword,
                keyword);
    }
}

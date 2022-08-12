package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlaceRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * PLACE가 등록되면 근처의 학교들에도 PLACE를 등록해줘야한다.
     * 이때 기준은 학교부터의 거리이므로 제한거리를 계산해 등록할 학교를 구하고
     * 해당 학교들에 PLACE를 등록시켜줘야한다.
     * @param place - place id를 포함한 Place객체
     */
    private void insertPlaceIntoPlaceList(Place place){
        String selectCollege = "" +
                "SELECT college_id, distance_limit_km, " +
                "(6371*acos(cos(radians( latitude ))*cos(radians( ? ))*cos(radians( ? )-radians( longitude ))+sin(radians( latitude ))*sin(radians( ? )))) " +
                " AS dist " +
                "FROM COLLEGE " +
                "GROUP BY college_id " +
                "HAVING dist <= distance_limit_km ";

        String insertPlaceList = "" +
                "INSERT INTO PLACE_LIST_AT_COLLEGE(COLLEGE_id, PLACE_id) " +
                "VALUES(?, ?) ";

        //위도1, *위도2, *경도2, 경도1, 위도1, *위도2
        Iterator<Integer> colleges =
                jdbcTemplate.query(
                        selectCollege,
                        (rs, rn) -> rs.getInt("college_id"),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getLatitude()
                        ).iterator();

        while(colleges.hasNext()){
            jdbcTemplate.update(
                    insertPlaceList,
                    colleges.next(),
                    place.getPlaceId()
            );
        }
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
     * @return int - place의 id - db에서 자동생성
     * @throws DataAccessException
     */
    @Override
    public int insertPlace(Place place) throws DataAccessException {
        String insertPlace = "" +
                "insert into PLACE(place_name, place_address, latitude, longitude, phone) " +
                "values(?, ?, ?, ?, ?) ";

        String getPlaceId = "" +
                "SELECT place_id " +
                "FROM PLACE " +
                "WHERE place_name = ? and place_address = ? ";

        String insertKakaoPlace = "" +
                "insert into KAKAO_PLACE(PLACE_id, kakao_place_id, category, place_image_url) " +
                "values(?, ?, ?, ?) ";

        //PLACE 테이블에 등록
        jdbcTemplate.update(insertPlace,
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getPhone());

        //DB에서 생성된 PLACE id
        int placeId = jdbcTemplate.queryForObject(
                getPlaceId,
                (rs, rn) -> rs.getInt("place_id"),
                place.getName(),
                place.getAddress()
        );

        place.setPlaceId(placeId);

        //KAKAO PLACE 테이블에 등록
        jdbcTemplate.update(insertKakaoPlace,
                place.getPlaceId(),
                place.getKakaoPlaceId(),
                place.getCategory(),
                place.getImageUrl());

        //PLACE LIST 테이블에 등록
        this.insertPlaceIntoPlaceList(place);
        return placeId;
    }



    @Override
    public Place selectPlace(int placeId) {
        String selectPlace = "" +
                "SELECT " +
                "   place_name, place_address, latitude, longitude, phone, kakao_place_id, category, place_image_url " +
                "FROM " +
                "   PLACE inner join KAKAO_PLACE " +
                "   ON PLACE.place_id = KAKAO_PLACE.PLACE_id " +
                "WHERE PLACE.place_id = ? ";


        return jdbcTemplate.queryForObject(selectPlace,
                (rs, rn) -> new Place(
                            placeId,
                            rs.getString("kakao_place_id"),
                            rs.getString("place_name"),
                            rs.getString("place_address"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getString("phone"),
                            rs.getString("category"),
                            rs.getString("place_image_url")
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
                            rs.getString("category"),
                            rs.getString("place_image_url")
                    );
                },
                collegeId,
                keyword,
                keyword);
    }
}

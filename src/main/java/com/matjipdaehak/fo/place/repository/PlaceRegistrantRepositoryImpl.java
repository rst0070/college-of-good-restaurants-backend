package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.place.model.PlaceRegistrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceRegistrantRepositoryImpl implements PlaceRegistrantRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlaceRegistrantRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertPlaceRegistrant(PlaceRegistrant placeRegistrant) {
        String sql = "" +
                "INSERT INTO PLACE_REGISTRANT(PLACE_id, USER_id) " +
                "VALUES(?, ?) ";

        jdbcTemplate.update(sql,
                placeRegistrant.getPlaceId(),
                placeRegistrant.getUserId());
    }

    @Override
    public List<Integer> selectPlaceIdByRegistrantId(String userId, int scopeStart, int scopeEnd) {
        String sql = "" +
                "SELECT PLACE_id " +
                "FROM PLACE_REGISTRANT " +
                "WHERE USER_id = ? " +
                "ORDER BY PLACE_id DESC " +
                "LIMIT ? OFFSET ? ";

        return jdbcTemplate.query(sql,
                (rs, rn) -> rs.getInt("PLACE_id"),
                userId,
                scopeEnd - scopeStart + 1,
                scopeStart - 1
        );
    }
}

package com.matjipdaehak.fo.like.repository;

import com.matjipdaehak.fo.like.model.PlaceLike;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class PlaceLikeRepositoryImpl implements PlaceLikeRepository{

    private final JdbcTemplate jdbcTemplate;

    public PlaceLikeRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertPlaceLike(PlaceLike placeLike) {
        String sql = "" +
                "insert into PLACE_LIKE(PLACE_id, USER_id, like_date) " +
                "values(?, ?, ?) ";
        jdbcTemplate.update(sql,
                placeLike.getPlaceId(),
                placeLike.getUserId(),
                placeLike.getLikeDate());
    }

    @Override
    public void deletePlaceLike(int placeId, String userId) {
        String sql = "" +
                "DELETE FROM PLACE_LIKE " +
                "wHERE PLACE_id = ? and USER_id = ? ";
        jdbcTemplate.update(sql,
                placeId,
                userId);
    }

    @Override
    public PlaceLike selectPlaceLike(int placeId, String userId) {
        String sql = "" +
                "select like_date " +
                "from PLACE_LIKE " +
                "where PLACE_id = ? and USER_id = ? ";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> new PlaceLike(placeId, userId, rs.getDate("like_date")),
                placeId,
                userId
        );
    }

    @Override
    public List<PlaceLike> selectPlaceLikeByUserId(String userId) {
        String sql = "" +
                "select PLACE_id, like_date " +
                "from PLACE_LIKE " +
                "where USER_id = ? ";
        return jdbcTemplate.query(
                sql,
                (rs, rn) -> {
                    int placeId = rs.getInt("PLACE_id");
                    Date likeDate = rs.getDate("like_date");
                    return new PlaceLike(placeId, userId, likeDate);
                },
                userId
        );
    }

    @Override
    public int countPlaceLikeByPlaceId(int placeId) {
        String sql = "" +
                "SELECT COUNT(PLACE_id) as count " +
                "FROM PLACE_LIKE " +
                "WHERE PLACE_id = ? ";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> rs.getInt("count"),
                placeId
        );
    }
}

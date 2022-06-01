package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.review.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertReview(Review review) {
        String sql = "" +
                "INSERT INTO REVIEW(PLACE_id, USER_id, post_date, post_text, rating) " +
                "   VALUES(?, ?, ?, ?, ?) ";

        jdbcTemplate.update(sql,
                review.getPlaceId(),
                review.getUserId(),
                review.getPostDate(),
                review.getPostText(),
                review.getRating());
    }
}

package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.review.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.*;
import java.util.*;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * 두번의 쿼리가 필요하다 이미지 리스트 테이블이 따로 존재하기 때문.
     * @param reviewId
     * @return Review
     * @throws DataAccessException
     */
    @Override
    public Review selectReview(long reviewId) throws DataAccessException {

        //REVIEW테이블 SELECT
        String selectReview = "" +
                "SELECT PLACE_id, USER_id, post_date, post_text, rating " +
                "FROM REVIEW " +
                "WHERE review_id = ? ";

        Review result = jdbcTemplate.queryForObject(
                selectReview,
                (rs, rn) ->{
                    Review review = new Review();
                    review.setReviewId(reviewId);
                    review.setPlaceId(rs.getInt("PLACE_id"));
                    review.setUserId(rs.getString("USER_id"));
                    review.setPostDate(rs.getDate("post_date"));
                    review.setPostText(rs.getString("post_text"));
                    review.setRating(rs.getInt("rating"));
                    return review;
                },
                reviewId
        );

        // REVIEW_IMAGE_LIST테이블 SELECT
        String selectImageList = "" +
                "SELECT image_url " +
                "FROM REVIEW_IMAGE_LIST " +
                "WHERE REVIEW_id = ? ";

        List<String> imageList = jdbcTemplate.query(
                selectImageList,
                (rs, rn) -> {
                    return rs.getString("image_url");
                },
                reviewId
        );

        // SET IMAGE LIST
        result.setImageUrls(imageList);
        return result;
    }


    /**
     * selectReview 메소드를 이용해 place id에 해당하는 Review객체들을 하나씩 생성
     * 이때 scope을 적용해 해당범위의 review들을 리스트로 반환한다.
     * @param placeId
     * @return
     * @throws DataAccessException
     */
    @Override
    public List<Review> selectReviewByPlaceId(int placeId, int scopeStart, int scopeEnd) throws DataAccessException {
        //if(scopeStart < 1 || scopeEnd < scopeStart) throw new DataAccessException();
        //예외 적용이 필요하다.
        String selectReviews = "" +
                "SELECT review_id " +
                "FROM REVIEW " +
                "WHERE PLACE_id = ? " +
                "ORDER BY review_id " +
                "LIMIT ? OFFSET ? ";
        return jdbcTemplate.query(
                selectReviews,
                (rs, rn)->{
                    long reviewId = rs.getLong("review_id");
                    return this.selectReview(reviewId);
                },
                placeId,
                scopeEnd - scopeStart + 1,
                scopeStart - 1
        );
    }

    @Override
    public List<Review> selectReviewByUserId(String userId, int scopeStart, int scopeEnd) throws DataAccessException {
        String sql = "" +
                "SELECT review_id " +
                "FROM REVIEW " +
                "WHERE USER_id = ? " +
                "ORDER BY review_id " +
                "LIMIT ? OFFSET ? ";

        return jdbcTemplate.query(
                sql,
                (rs, rn) -> this.selectReview(rs.getLong("review_id")),
                userId,
                scopeEnd - scopeStart + 1,
                scopeStart - 1
        );
    }

    @Override
    public void insertReview(Review review) {
        //리뷰저장
        String reviewSql = "" +
                "INSERT INTO REVIEW(PLACE_id, USER_id, post_date, post_text, rating) " +
                "   VALUES(?, ?, ?, ?, ?) ";
        jdbcTemplate.update(reviewSql,
                review.getPlaceId(),
                review.getUserId(),
                review.getPostDate(),
                review.getPostText(),
                review.getRating());

        //리뷰 이미지 url 저장
        String imageSql = "" +
                "INSERT INTO REVIEW_IMAGE_LIST(REVIEW_PLACE_id, REVIEW_USER_id, image_url) " +
                "   VALUES(?, ?, ?) ";

        List<String> urlList = review.getImageUrls();
        if(urlList == null) return;
        Iterator<String> urls = urlList.iterator();

        //url이 있을때 이를 저장한다.
        while(urls.hasNext()) {
            jdbcTemplate.update(imageSql,
                    review.getPlaceId(),
                    review.getUserId(),
                    urls.next());
        }

    }

    @Override
    public void deleteReview(long reviewId) throws DataAccessException{
        String deleteUrls = "" +
                "DELETE FROM REVIEW_IMAGE_LIST " +
                "WHERE REVIEW_id = ? ";

        String deleteReview = "" +
                "DELETE FROM REVIEW " +
                "WHERE review_id = ? ";

        jdbcTemplate.update(deleteUrls,
                    reviewId);
        jdbcTemplate.update(deleteReview,
                    reviewId);
    }


}

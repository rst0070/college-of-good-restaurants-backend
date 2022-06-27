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
     * @param placeId - pk1
     * @param userId - pk2
     * @return
     * @throws DataAccessException
     */
    @Override
    public Review selectReview(int placeId, String userId) throws DataAccessException {

        //REVIEW테이블 SELECT
        String selectReview = "" +
                "SELECT PLACE_id, USER_id, post_date, post_text, rating " +
                "FROM REVIEW " +
                "WHERE PLACE_id = ? and USER_id = ? ";

        Review result = jdbcTemplate.queryForObject(
                selectReview,
                (rs, rn) ->{
                    Review review = new Review();
                    review.setPlaceId(rs.getInt("PLACE_id"));
                    review.setUserId(rs.getString("USER_id"));
                    review.setPostDate(rs.getDate("post_date"));
                    review.setPostText(rs.getString("post_text"));
                    review.setRating(rs.getInt("rating"));
                    return review;
                },
                placeId,
                userId
        );

        // REVIEW_IMAGE_LIST테이블 SELECT
        String selectImageList = "" +
                "SELECT image_url " +
                "FROM REVIEW_IMAGE_LIST " +
                "WHERE REVIEW_PLACE_id = ? and REVIEW_USER_id = ? ";

        List<String> imageList = jdbcTemplate.query(
                selectImageList,
                (rs, rn) -> {
                    return rs.getString("image_url");
                },
                placeId,
                userId
        );

        // SET IMAGE LIST
        result.setImageUrls(imageList);
        return result;
    }

    /**
     * selectReview 메소드를 이용해 place id에 해당하는 Review객체들을 하나씩 생성
     * @param placeId
     * @return
     * @throws DataAccessException
     */
    @Override
    public List<Review> selectReviewByPlaceId(int placeId) throws DataAccessException {
        String selectReviews = "" +
                "SELECT USER_id " +
                "FROM REVIEW " +
                "WHERE PLACE_id = ? ";
        return jdbcTemplate.query(
                selectReviews,
                (rs, rn)->{
                    String userId = rs.getString("USER_id");
                    return this.selectReview(placeId, userId);
                },
                placeId
        );
    }

    @Override
    public void insertReview(Review review) {
        try{
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

            while(urls.hasNext()){
                jdbcTemplate.update(imageSql,
                        review.getPlaceId(),
                        review.getUserId(),
                        urls.next());
            }
        }catch(Exception ex){
            //잘못된 데이터가 있을 수 있으니 지우기
            logger.warn(ex.getMessage());
            this.deleteReview(review.getPlaceId(), review.getUserId());
        }

    }

    @Override
    public void deleteReview(int placeId, String userId) throws DataAccessException{
        String deleteUrls = "" +
                "DELETE FROM REVIEW_IMAGE_LIST " +
                "WHERE REVIEW_PLACE_id = ? and REVIEW_USER_id = ? ";

        String deleteReview = "" +
                "DELETE FROM REVIEW " +
                "WHERE PLACE_id = ? and USER_id = ? ";

        jdbcTemplate.update(deleteUrls,
                    placeId, userId);
        jdbcTemplate.update(deleteReview,
                    placeId, userId);
    }


}

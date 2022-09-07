package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.common.service.CommonService;
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
    private final CommonService commonService;

    @Autowired
    public ReviewRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            CommonService commonService
    ){
        this.jdbcTemplate = jdbcTemplate;
        this.commonService = commonService;
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
                "ORDER BY review_id DESC " +
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

    /**
     * review_id 에 해당하는 리뷰 이미지들을 삭제한다.
     * @param reviewId - 지울려는 리뷰 이미지들의 리뷰아이디.
     */
    private void deleteReviewImageList(long reviewId){
        String sql = "" +
                "DELETE FROM REVIEW_IMAGE_LIST " +
                "WHERE REVIEW_id = ? ";
        jdbcTemplate.update(sql, reviewId);
    }

    /**
     * 리뷰의 이미지들을(이미지의 url들) REVIEW_IMAGE_LIST테이블에 저장한다.
     * 이때 이미지 아이디는 commonService에서 랜덤 아이디를 발급받아 사용한다.
     * @param review - review id가 있는 review 객체
     */
    private void insertReviewImageList(Review review){
        //리뷰 이미지 url 저장
        String imageSql = "" +
                "INSERT INTO REVIEW_IMAGE_LIST(image_id, REVIEW_id, image_url) " +
                "   VALUES(?, ?, ?) ";

        List<String> urlList = review.getImageUrls();
        if(urlList == null) return;
        Iterator<String> urls = urlList.iterator();

        //url이 있을때 이를 저장한다.
        while(urls.hasNext()) {
            jdbcTemplate.update(imageSql,
                    commonService.getUniqueIdByCurrentDate(),
                    review.getReviewId(),
                    urls.next());
        }
    }

    /**
     * 리뷰의 아이디가 DB상에 존재하는지 확인한다.
     * @param reviewId - 확인하려는 아이디의 값
     * @return true - DB상에 존재함. false - 존재하지 않음:사용가능
     */
    private boolean isIdExist(long reviewId){
        String sql = "" +
                "SELECT EXISTS( " +
                " SELECT review_id " +
                " FROM REVIEW " +
                " WHERE review_id = ? " +
                ") ";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> rs.getBoolean(1),
                reviewId
        );
    }
    /**
     * 리뷰를 저장한다.
     * 1. 리뷰의 아이디 생성
     * 2. 리뷰저장
     * 3. 리뷰의 이미지들을 저장
     * @param review
     */
    @Override
    public void insertReview(Review review) throws DataAccessException {
        review.setReviewId(this.commonService.getUniqueIdByCurrentDate());

        //리뷰아이디가 이미 DB상에 있으면 계속 새로운 아이디를 생성해본다.
        while( this.isIdExist(review.getReviewId()) )
            review.setReviewId(this.commonService.getUniqueIdByCurrentDate());

        //리뷰저장
        String reviewSql = "" +
                "INSERT INTO REVIEW(review_id, PLACE_id, USER_id, post_date, post_text, rating) " +
                "   VALUES(?, ?, ?, ?, ?, ?) ";
        jdbcTemplate.update(reviewSql,
                review.getReviewId(),
                review.getPlaceId(),
                review.getUserId(),
                review.getPostDate(),
                review.getPostText(),
                review.getRating());
        //리뷰 이미지 url들 저장
        this.insertReviewImageList(review);
    }

    /**
     * updates review
     * 1. UPDATE REVIEW Table
     * 2. DELETE Previous Review images
     * 3. INSERT New images
     * @param review
     * @throws DataAccessException
     */
    @Override
    public void updateReview(Review review) throws DataAccessException {
        String sql = "" +
                "UPDATE REVIEW " +
                "SET post_date = ?, post_text = ?, rating = ? " +
                "WHERE review_id = ? ";

        jdbcTemplate.update(sql,
                review.getPostDate(), review.getPostText(), review.getRating(), review.getReviewId());

        this.deleteReviewImageList(review.getReviewId());
        this.insertReviewImageList(review);
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

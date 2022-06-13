package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.review.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
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

            //리뷰 이미지 저장
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
            //지우기
            //url을 먼저 지워야한다. 참조되기때문
            logger.warn(ex.getMessage());
            String deleteUrls = "" +
                    "DELETE FROM REVIEW_IMAGE_LIST " +
                    "WHERE REVIEW_PLACE_id = ? and REVIEW_USER_id = ? and image_url = ? ";
            String deleteReview = "" +
                    "DELETE FROM REVIEW " +
                    "WHERE PLACE_id = ? and USER_id = ? ";

            if(review.getImageUrls() != null){
                Iterator<String> it = review.getImageUrls().iterator();
                while (it.hasNext()){
                    jdbcTemplate.update(deleteUrls,
                            review.getPlaceId(),
                            review.getUserId(),
                            it.next());
                }
            }
            jdbcTemplate.update(deleteReview
            ,review.getPlaceId(), review.getUserId());

        }

    }
}

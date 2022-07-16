package com.matjipdaehak.fo.place.repository;

import com.matjipdaehak.fo.exception.UnprocessableEntityException;
import com.matjipdaehak.fo.place.model.ExtendedPlace;
import com.matjipdaehak.fo.place.model.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ExtendedPlaceRepositoryImpl implements ExtendedPlaceRepository{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JdbcTemplate jdbcTemplate;
    private final PlaceRepository placeRepository;

    @Autowired
    public ExtendedPlaceRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            PlaceRepository placeRepository){
        this.jdbcTemplate = jdbcTemplate;
        this.placeRepository = placeRepository;
    }

    /**
     * 좋아요수와 리뷰개수, 평점을 계산해야하는데 이것이 해당 데이터가 없는 경우 예외처리 필요
     * @param placeId
     * @return
     */
    @Override
    public ExtendedPlace selectExtendedPlace(int placeId) {
        Place place = this.placeRepository.selectPlace(placeId);
        ExtendedPlace ep = new ExtendedPlace();
        ep.setByPlace(place);

        String selectReview = "" +
                "SELECT COUNT( USER_id ) as review_count, AVG( CAST( rating as double ) ) as rating_avg " +
                "FROM REVIEW " +
                "WHERE PLACE_id = ? " +
                "GROUP BY PLACE_id ";

        String selectLikeCount = "" +
                "SELECT COUNT(USER_id) as like_count " +
                "FROM PLACE_LIKE " +
                "WHERE PLACE_id = ? " +
                "GROUP BY PLACE_id ";

        try{
            jdbcTemplate.queryForObject(
                    selectReview,
                    (rs, rn) -> {
                        ep.setRating(rs.getDouble("rating_avg"));
                        ep.setReviewCount(rs.getInt("review_count"));
                        return ep;
                    },placeId
            );
        }catch(EmptyResultDataAccessException empty){
            logger.info("place_id : {} has no reviews", placeId);
            ep.setRating(0);
            ep.setReviewCount(0);
        }

        try{
            jdbcTemplate.queryForObject(
                    selectLikeCount,
                    (rs, rn) ->{
                        ep.setLikeCount(rs.getInt("like_count"));
                        return ep;
                    }, placeId
            );
        }catch(EmptyResultDataAccessException empty){
            logger.info("place_id : {} has no likes", placeId);
            ep.setLikeCount(0);
        }

        return ep;
    }

    /**
     * 테스트 필요
     * @param collegeId
     * @param keyword
     * @return
     */
    @Override
    public List<ExtendedPlace> keywordSearchExtendedPlace(int collegeId, String keyword, int scopeStart, int scopeEnd) throws UnprocessableEntityException {
        if(scopeStart < 1 || scopeEnd < scopeStart)
            throw new UnprocessableEntityException("range of scope for keyword searching is unpro");

        keyword = '%' + keyword + '%';

        String sql = "" +
                "SELECT p.PLACE_id as place_id, count(r.USER_id) as count " +
                "FROM " +
                "   (" +
                "       REVIEW r inner join " +
                "       (PLACE p inner join KAKAO_PLACE kp on p.place_id = kp.place_id) " +
                "       on r.PLACE_id = p.place_id" +
                "   ) " + // review:place:kakao_place 관계 = n:1:1
                "   inner join PLACE_LIST_AT_COLLEGE l " +
                "   on p.place_id = l.PLACE_id " +
                "where " +
                "   COLLEGE_id = ? and " + //COLLEGE_id는 1개로 제한되기 때문에 review : place : place_list의 관계는 n:1:1의 관계가 된다.
                "   (r.post_text like ? or p.place_name like ? or p.place_address like ? or kp.category = ?) " +
                "GROUP BY PLACE_id " +
                "order by count desc " +
                "LIMIT ? OFFSET ? ";

        Iterator<Integer> placeIds = jdbcTemplate.query(
                sql,
                (rs, rn) -> rs.getInt("place_id"),
                collegeId,
                keyword,
                keyword,
                keyword,
                keyword,
                scopeEnd - scopeStart + 1,
                scopeStart - 1
        ).iterator();

        LinkedList<ExtendedPlace> result = new LinkedList<ExtendedPlace>();
        while(placeIds.hasNext()){
            result.add(selectExtendedPlace(placeIds.next()));
        }

        if(result.isEmpty())//결과가 없으면 키워드에 해당하는것이 없거나 scope에 해당하는것이 없는경우
            throw new UnprocessableEntityException("");

        return result;
    }
}
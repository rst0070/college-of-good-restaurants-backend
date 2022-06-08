package com.matjipdaehak.fo.user.repository;

import com.matjipdaehak.fo.user.model.MatjipDaehakUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MatjipDaehakUserDetailsRepositoryImpl implements MatjipDaehakUserDetailsRepository{

    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
    private final JdbcTemplate jdbcTemplate;
    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/namedparam/NamedParameterJdbcTemplate.html
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MatjipDaehakUserDetailsRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     *
     * @param username - 찾고자하는 사용자 아이디(USER.user_id 해당)
     * @return MatjipDaehakUserDetails. 만약 해당 아이디의 user가 없는 경우 null
     */
    @Override
    public MatjipDaehakUserDetails getUserDetailsByUsername(String username){
        String query = "select * from USER where user_id = :id";

        SqlRowSet row = namedParameterJdbcTemplate.queryForRowSet(
                query,
                Map.of("id", username)
        );

        return mapRow(row, 1);
    }

    @Override
    public void insertUser(MatjipDaehakUserDetails userDetails) {
        String sql = "" +
                "insert into USER(COLLEGE_id, nickname, user_id, password, college_email_address) " +
                "values( ?, ?, ?, ?, ?) ";

        jdbcTemplate.update(sql,
                userDetails.getCollegeId(),
                userDetails.getNickname(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getCollegeEmailAddress()
        );
    }

    /**
     * 쿼리결과를 MatjipDaehakUserDetails에 매핑시켜 반환한다.
     * @param rows - SqlRowSet
     * @param rowNum - mapping 시키려는 row 번호
     * @return MatjipDaehakUserDetails. row가 빈경우 null반환
     */
    private MatjipDaehakUserDetails mapRow(SqlRowSet rows, int rowNum){
        if(!rows.first()) return null;
        MatjipDaehakUserDetails userDetails = new MatjipDaehakUserDetails();

        userDetails.setCollegeId(rows.getInt("COLLEGE_id"));
        userDetails.setNickname(rows.getString("nickname"));
        userDetails.setUsername(rows.getString("user_id"));
        userDetails.setPassword(rows.getString("password"));
        userDetails.setCollegeEmailAddress(rows.getString("college_email_address"));

        return userDetails;
    }
}

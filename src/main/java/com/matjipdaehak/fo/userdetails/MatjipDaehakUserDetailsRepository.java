package com.matjipdaehak.fo.userdetails;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public class MatjipDaehakUserDetailsRepository {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MatjipDaehakUserDetailsRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public MatjipDaehakUserDetails getUserDetailsByUsername(String name){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", name);
        return namedParameterJdbcTemplate.queryForObject("SELECT * FROM USER WHERE user_id = :id", namedParameters ,MatjipDaehakUserDetails.class);
    }
}

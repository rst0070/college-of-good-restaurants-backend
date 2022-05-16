package com.matjipdaehak.fo.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.sql.ResultSet;

@Repository
public class CommonRepositoryImpl implements CommonRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CommonRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public Map<String, Integer> getNumberOfStudentsInEachCollege() {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
        String sql =
                "select COLLEGE.college_name as college_name, count(USER.user_id) as cnt " +
                        "from COLLEGE inner join USER " +
                        "on COLLEGE.college_id = USER.COLLEGE_id " +
                        "group by COLLEGE.college_id";

        jdbcTemplate.query(sql, (rs, rowNum)->{
            String collegeName = rs.getString("college_name");
            int count = rs.getInt("cnt");
            resultMap.put(collegeName, count);
            return 1;
        });
        return resultMap;
    }

}
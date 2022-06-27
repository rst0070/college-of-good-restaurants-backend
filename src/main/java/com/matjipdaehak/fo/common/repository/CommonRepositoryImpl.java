package com.matjipdaehak.fo.common.repository;

import com.matjipdaehak.fo.common.model.CollegeStudentCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

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


    /**
     * 학생이 없는 경우도 가져오도록 left join으로 함.
     * @return
     */
    @Override
    public List<CollegeStudentCount> getNumberOfStudentsInEachCollege() {
        HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
        String sql =
                "select COLLEGE.college_id as college_id, COLLEGE.college_name as college_name, count(USER.user_id) as cnt " +
                        "from COLLEGE left join USER " +
                        "on COLLEGE.college_id = USER.COLLEGE_id " +
                        "group by COLLEGE.college_id";

        return jdbcTemplate.query(sql, (rs, rowNum)->{
            int collegeId = rs.getInt("college_id");
            String collegeName = rs.getString("college_name");
            int count = rs.getInt("cnt");
            return new CollegeStudentCount(
                    collegeId,
                    collegeName,
                    count
            );
        });
    }

}

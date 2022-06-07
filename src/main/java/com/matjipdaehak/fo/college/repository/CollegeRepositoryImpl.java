package com.matjipdaehak.fo.college.repository;
import com.matjipdaehak.fo.college.model.College;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CollegeRepositoryImpl implements CollegeRepository{

    private final JdbcTemplate jdbcTemplate;
    public CollegeRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public College selectByCollegeMailDomain(String collegeMailDomain) {
        String sql = "" +
                "select * " +
                "from COLLEGE inner join COLLEGE_MAIL_DOMAIN " +
                "on COLLEGE.college_id = COLLEGE_MAIL_DOMAIN.COLLEGE_id " +
                "where COLLEGE_MAIL_DOMAIN.college_mail_domain = ? ";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn)->{
                    College college = new College();
                    college.setCollegeId(rs.getInt("college_id"));
                    college.setCollegeName(rs.getString("college_name"));
                    college.setCollegeMailDomain(collegeMailDomain);
                    college.setLatitude(rs.getDouble("latitude"));
                    college.setLongitude(rs.getDouble("longitude"));
                    college.setDistanceLimitKm(rs.getDouble("distance_limit_km"));
                    return college;
                },
                collegeMailDomain);
    }


    @Override
    public College selectByCollegeId(int collegeId) {
        String sql = "" +
                "select * " +
                "from COLLEGE inner join COLLEGE_MAIL_DOMAIN " +
                "on COLLEGE.college_id = COLLEGE_MAIL_DOMAIN.COLLEGE_id " +
                "where COLLEGE.college_id = ? ";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn)->{
                    College college = new College();
                    college.setCollegeId(rs.getInt("college_id"));
                    college.setCollegeName(rs.getString("college_name"));
                    college.setCollegeMailDomain(rs.getString("college_mail_domain"));
                    college.setLatitude(rs.getDouble("latitude"));
                    college.setLongitude(rs.getDouble("longitude"));
                    college.setDistanceLimitKm(rs.getDouble("distance_limit_km"));
                    return college;
                },
                collegeId);
    }
}

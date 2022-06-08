package com.matjipdaehak.fo.user.repository;

import com.matjipdaehak.fo.user.model.EmailAuthCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SignupRepositoryImpl implements SignupRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SignupRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isCollegeExistWithEmailDomain(String emailDomain) {
        return true;
    }

    @Override
    public boolean isEmailTaken(String emailAddr) {
        String sql = "" +
                "select EXISTS( " +
                "select 1 " +
                "from USER " +
                "where college_email_address = ? " +
                ")";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getBoolean(1),
                emailAddr
        );
    }

    @Override
    public boolean isUserIdTaken(String userId){
        String sql = "" +
                "select EXISTS( " +
                "select 1 " +
                "from USER " +
                "where user_id = ? " +
                ")";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getBoolean(1),
                userId
        );
    }

    @Override
    public void insertEmailAuthCode(EmailAuthCode emailAuthCode) throws DataAccessException {
        String sql = "" +
                "insert into " +
                "EMAIL_AUTH_CODE(email_addr, auth_code, exp_date) " +
                "values( ?, ?, ?);";

        jdbcTemplate.update(sql,
                emailAuthCode.getEmailAddr(),
                emailAuthCode.getAuthCode(),
                emailAuthCode.getExpDate() );
    }

    @Override
    public EmailAuthCode selectEmailAuthCode(String emailAddr) {
        String sql = "" +
                "select email_addr, auth_code, exp_date " +
                "from EMAIL_AUTH_CODE " +
                "where email_addr = ?";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> new EmailAuthCode(
                        rs.getString("email_addr"),
                        rs.getString("auth_code"),
                        rs.getDate("exp_date")
                ),
                emailAddr
        );
    }

}

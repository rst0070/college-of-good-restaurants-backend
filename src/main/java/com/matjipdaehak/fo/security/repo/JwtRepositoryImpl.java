package com.matjipdaehak.fo.security.repo;

import com.matjipdaehak.fo.security.model.JwtInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JwtRepositoryImpl implements JwtRepository{

    private final JdbcTemplate jdbcTemplate;

    public JwtRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertJwt(JwtInfo jwt) {
        String sql = "" +
                "INSERT INTO JWT(jwt_id, user_id, jwt) " +
                "VALUES(?, ?, ?) ";

        jdbcTemplate.update(
                sql,
                jwt.getJwtId(),
                jwt.getUserId(),
                jwt.getJwt()
        );
    }

    /**
     * DB에서 jwt를 읽는다. 이미 한번 등록된 jwt이어야 한다.
     * @param jwtId
     * @return
     */
    @Override
    public JwtInfo selectJwt(long jwtId) {
        String sql = "" +
                "SELECT jwt_id, user_id, jwt " +
                "FROM JWT " +
                "WHERE jwt_id = ? ";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) ->
                    new JwtInf(
                            rs.getLong("jwt_id"),
                            rs.getString("user_id"),
                            rs.getString("jwt")
                    )
                ,
                jwtId
        );
    }

    /**
     * 특정 사용자의 jwt를 모두 삭제 한다. 이를 통해 로그아웃을 시킬 수 있음.
     * @param userId
     */
    @Override
    public void deleteJwtByUserId(String userId) {
        String sql = "" +
                "DELETE FROM JWT " +
                "WHERE user_id = ? ";
        jdbcTemplate.update(sql,
                userId);
    }


    private class JwtInf implements JwtInfo {

        private final long jwtId;
        private final String userId;
        private final String jwt;

        JwtInf(long jwtId, String userId, String jwt){
            this.jwtId = jwtId;
            this.userId = userId;
            this.jwt = jwt;
        }

        @Override
        public long getJwtId() {
            return jwtId;
        }

        @Override
        public String getUserId() {
            return userId;
        }

        @Override
        public String getJwt() {
            return jwt;
        }
    }

}

package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.review.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> selectCommentByReviewId(long reviewId) {
        String sql = "" +
                "SELECT comment_id, USER_id, comment_text,  comment_date " +
                "FROM COMMENT " +
                "WHERE REVIEW_id = ? " +
                "ORDER BY comment_date desc ";

        return jdbcTemplate.query(sql,
                (rs, rn) -> {
                    Comment comment = new Comment();
                    comment.setCommentId(rs.getLong("comment_id"));
                    comment.setReviewId(reviewId);
                    comment.setUserId(rs.getString("USER_id"));
                    comment.setCommentText(rs.getString("comment_text"));
                    comment.setCommentDate(rs.getDate("comment_date"));
                    return comment;
                },
                reviewId);
    }
}

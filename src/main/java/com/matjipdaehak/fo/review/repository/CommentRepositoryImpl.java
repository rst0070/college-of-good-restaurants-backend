package com.matjipdaehak.fo.review.repository;

import com.matjipdaehak.fo.common.service.CommonService;
import com.matjipdaehak.fo.review.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository{

    private final JdbcTemplate jdbcTemplate;
    private final CommonService commonService;

    @Autowired
    public CommentRepositoryImpl(
            JdbcTemplate jdbcTemplate,
            CommonService commonService){
        this.jdbcTemplate = jdbcTemplate;
        this.commonService = commonService;
    }

    @Override
    public boolean isIdExist(long commentId) {
        String sql = "" +
                "SELECT EXISTS( " +
                " SELECT comment_id " +
                " FROM COMMENT " +
                " WHERE comment_id = ? " +
                ") ";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> rs.getBoolean(1),
                commentId
        );
    }

    /**
     * 1. id정하기
     * 2. 저장
     * @param comment - 저장하려는 comment객체 이때 comment id는 필요없음
     */
    @Override
    public void insertComment(Comment comment) {
        //id정하기
        comment.setCommentId( this.commonService.getUniqueIdByCurrentDate() );
        //id존재하면 계속 새로운 id시도
        while( this.isIdExist(comment.getCommentId()) )
            comment.setCommentId( this.commonService.getUniqueIdByCurrentDate() );

        //sql문
        String sql = "" +
                "INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) " +
                "VALUES( ?, ?, ?, ?, ?) ";
        //sql실행
        jdbcTemplate.update(sql,
                comment.getCommentId(),
                comment.getReviewId(),
                comment.getUserId(),
                comment.getCommentText(),
                comment.getCommentDate());
    }

    @Override
    public Comment selectComment(long commentId) {
        String sql = "" +
                "SELECT REVIEW_id, USER_id, comment_text,  comment_date " +
                "FROM COMMENT " +
                "WHERE comment_id = ? ";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rn) -> {
                    Comment comment = new Comment();
                    comment.setCommentId( commentId );
                    comment.setReviewId( rs.getLong("REVIEW_id"));
                    comment.setUserId( rs.getString("USER_id"));
                    comment.setCommentText( rs.getString("comment_text"));
                    comment.setCommentDate( rs.getDate("comment_date"));
                    return comment;
                },
                commentId
        );
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

    /**
     * comment_text와 comment_date만 수정한다.
     * @param comment
     */
    @Override
    public void updateComment(Comment comment) {
        String sql = "" +
                "UPDATE COMMENT " +
                "SET comment_text = ?, comment_date = ? " +
                "WHERE comment_id = ? ";
        jdbcTemplate.update(sql, comment.getCommentText(), comment.getCommentDate(), comment.getCommentId());
    }

    /**
     * 특정 댓글을 삭제한다.
     *
     * @param commentId
     */
    @Override
    public void deleteComment(long commentId) {
        String sql = "" +
                "DELETE FROM COMMENT " +
                "WHERE comment_id = ? ";
        jdbcTemplate.update(sql, commentId);
    }

    /**
     * 특정 리뷰에 대한 댓글을 모두삭제한다.
     *
     * @param reviewId
     */
    @Override
    public void deleteCommentByReviewId(long reviewId) {
        String sql = "" +
                "DELETE FROM COMMENT " +
                "WHERE REVIEW_id = ? ";
        jdbcTemplate.update(sql, reviewId);
    }
}

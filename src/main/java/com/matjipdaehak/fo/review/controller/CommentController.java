package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.common.service.CommonService;
import com.matjipdaehak.fo.exception.CustomException;
import com.matjipdaehak.fo.exception.ErrorCode;
import com.matjipdaehak.fo.review.model.Comment;
import com.matjipdaehak.fo.review.service.CommentService;
import com.matjipdaehak.fo.security.model.JwtInfo;
import com.matjipdaehak.fo.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommonService commonService;
    private final JwtService jwtService;

    @Autowired
    public CommentController(
            CommentService commentService,
            CommonService commonService,
            JwtService jwtService
    ){
        this.commentService = commentService;
        this.commonService = commonService;
        this.jwtService = jwtService;
    }

    /**
     * 새로운 리뷰를 작성한다. DB에 저장한다.
     * @param json
     * {
     *     "review_id" : 123123213,
     *     "user_id" : "wonbinkim",
     *     "comment_text" : "review message"
     * }
     */
    @PostMapping("/add-comment")
    public void addComment(@RequestBody JsonNode json){
        //try{
            long reviewId = json.get("review_id").asLong();
            String userId = json.get("user_id").asText();
            String commentText = json.get("comment_text").asText();

            Comment comment = new Comment();
            comment.setReviewId( reviewId );
            comment.setUserId( userId );
            comment.setCommentText( commentText );
            comment.setCommentDate( new Date(this.commonService.getCurrentDate()));

            this.commentService.createNewComment(comment);
        //}catch (Exception e){

        //}
    }

    /**
     *
     * @param req
     * @param json
     * {
     *     "comment_id" : "12313333123",
     *     "review_id" : "123123213",
     *     "user_id" : "wonbinkim",
     *     "comment_text" : "review message"
     * }
     */
    @RequestMapping("/update-comment")
    public void updateComment(HttpServletRequest req, @RequestBody JsonNode json){
        //jwt information inside of header
        final String jwt = req.getHeader("Authorization").substring(7);
        JwtInfo jwtInfo = this.jwtService.getJwtInfoFromJwt(jwt);

        //compare userid in jwt vs user id in comment for update
        //user could only change oneself review
        if( !jwtInfo.getUserId().equals( json.get("user_id").asText() ) ) throw new CustomException(ErrorCode.UNAUTHORIZED);

        Comment comment = new Comment();
        comment.setCommentId( json.get("comment_id").asLong() );
        comment.setReviewId( json.get("review_id").asLong() );
        comment.setUserId( json.get("user_id").asText() );
        comment.setCommentText( json.get("comment_text").asText() );
        comment.setCommentDate( new Date(this.commonService.getCurrentDate()) );

        this.commentService.updateComment(comment);
    }

    /**
     *
     * @param req
     * @param json
     * {
     *     "comment_id" : "123123211312"
     * }
     */
    @RequestMapping("/delete-comment")
    public void deleteComment(HttpServletRequest req, @RequestBody JsonNode json){
        //jwt information inside of header
        final String jwt = req.getHeader("Authorization").substring(7);
        JwtInfo jwtInfo = this.jwtService.getJwtInfoFromJwt(jwt);

        this.commentService.deleteComment(jwtInfo.getUserId(), json.get("comment_id").asLong() );
    }
}

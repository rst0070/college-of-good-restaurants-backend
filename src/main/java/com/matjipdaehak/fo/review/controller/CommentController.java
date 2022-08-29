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
     *  comment의 text만 수정한다.
     * @param req
     * @param json
     * {
     *     "comment_id" : "12313333123",
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
        Comment comment = this.commentService.getCommentByCommentId(json.get("comment_id").asLong());
        if( !jwtInfo.getUserId().equals( comment.getUserId() ) ) throw new CustomException(ErrorCode.UNAUTHORIZED);
        //update data
        comment.setCommentText( json.get("comment_text").asText() );
        comment.setCommentDate( new Date(this.commonService.getCurrentDate()) );
        //update logic
        this.commentService.updateComment(comment);
    }

    /**
     * jwt 정보로 해당 리뷰의 실 소유자가 맞는지 확인한다.
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
        //check jwt user
        Comment comment = this.commentService.getCommentByCommentId(json.get("comment_id").asLong());
        if(!jwtInfo.getUserId().equals(comment.getUserId())) throw new CustomException(ErrorCode.UNAUTHORIZED);
        //delete logic
        this.commentService.deleteComment(json.get("comment_id").asLong() );
    }
}

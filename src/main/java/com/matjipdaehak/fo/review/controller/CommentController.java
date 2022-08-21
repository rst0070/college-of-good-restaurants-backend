package com.matjipdaehak.fo.review.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.matjipdaehak.fo.common.service.CommonService;
import com.matjipdaehak.fo.review.model.Comment;
import com.matjipdaehak.fo.review.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommonService commonService;

    public CommentController(
            CommentService commentService,
            CommonService commonService
    ){
        this.commentService = commentService;
        this.commonService = commonService;
    }

    /**
     * 새로운 리뷰를 작성한다. DB에 저장한다.
     * @param json
     * {
     *     "review_id" : 123123213,
     *     "user_id" : "wonbinkim",
     *     "commet_text" : "review message"
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
}

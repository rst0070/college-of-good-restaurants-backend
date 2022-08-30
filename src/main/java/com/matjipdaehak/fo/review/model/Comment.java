package com.matjipdaehak.fo.review.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.*;

/**
 * 리뷰에 대한 댓글 객체
 */
@Data
public class Comment {

    @JsonProperty("comment_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long commentId;
    @JsonProperty("review_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long reviewId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("comment_text")
    private String commentText;
    @JsonProperty("comment_date")
    private Date commentDate;
}

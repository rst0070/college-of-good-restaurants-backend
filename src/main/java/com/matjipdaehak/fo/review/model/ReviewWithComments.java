package com.matjipdaehak.fo.review.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


/**
 * Review와 해당 comment들을 가지는 객체
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWithComments {

    @JsonProperty("review")
    private Review review;
    @JsonProperty("comments")
    private List<Comment> comments;

}

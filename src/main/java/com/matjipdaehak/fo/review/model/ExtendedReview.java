package com.matjipdaehak.fo.review.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matjipdaehak.fo.place.model.ExtendedPlace;
import lombok.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedReview {

    @JsonProperty("place")
    private ExtendedPlace place;
    @JsonProperty("review")
    private Review review;
    @JsonProperty("comments")
    private List<Comment> comments;
}

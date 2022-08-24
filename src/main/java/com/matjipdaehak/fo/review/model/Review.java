package com.matjipdaehak.fo.review.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
public class Review {

    @JsonProperty("review_id")
    private long reviewId;

    @JsonProperty("place_id")
    private int placeId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("post_date")
    private Date postDate;

    @JsonProperty("post_text")
    private String postText;

    private int rating;

    @JsonProperty("image_urls")
    private List<String> imageUrls;

    /**
     *
     * @param placeId
     * @param userId
     * @param postDate
     * @param postText
     * @param rating
     */
    public Review(
            int placeId,
            String userId,
            Date postDate,
            String postText,
            int rating,
            List<String> imageUrls
    ){
        this.placeId = placeId;
        this.userId = userId;
        this.postDate = postDate;
        this.postText = postText;
        this.rating = rating;
        this.imageUrls = imageUrls;
    }
}

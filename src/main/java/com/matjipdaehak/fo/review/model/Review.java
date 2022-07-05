package com.matjipdaehak.fo.review.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
public class Review {
    /*

	PLACE_id int,
    USER_id varchar(20),
    post_date date not null,
    post_text text not null,
    rating int,
     */
    @JsonProperty("place_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int placeId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("post_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private Date postDate;

    @JsonProperty("post_text")
    private String postText;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
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

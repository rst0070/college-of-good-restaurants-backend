package com.matjipdaehak.fo.review.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private int placeId;
    private String userId;
    private Date postDate;
    private String postText;
    private int rating;

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
            int rating
    ){
        this.placeId = placeId;
        this.userId = userId;
        this.postDate = postDate;
        this.postText = postText;
        this.rating = rating;
    }
}
